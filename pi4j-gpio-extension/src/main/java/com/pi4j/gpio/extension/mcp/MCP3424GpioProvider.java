package com.pi4j.gpio.extension.mcp;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  MCP3424GpioProvider.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2018 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.gpio.extension.base.AdcGpioProviderBase;
import com.pi4j.gpio.extension.base.AdcGpioProvider;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import java.lang.IllegalArgumentException;
import java.io.IOException;

/**
 *
 * <p>
 * This GPIO provider implements the MCP3424 I2C GPIO expansion board as native Pi4J GPIO pins. It is a 18-bit
 * ADC providing 4 sigma/delta input channels. More information about the board can be found here:
 * http://ww1.microchip.com/downloads/en/DeviceDoc/22088c.pdf
 * </p>
 *
 * <p>
 * The MCP3424 is connected via I2C connection to the Raspberry Pi and provides 4 analog input channels.
 * The values returned are in the range [-131072:131071] (max 18 bit value).
 * </p>
 *
 * @author Alexander Falkenstern
 */
public class MCP3424GpioProvider extends AdcGpioProviderBase implements AdcGpioProvider {

  public static final String NAME = "com.pi4j.gpio.extension.mcp.MCP3424GpioProvider";
  public static final String DESCRIPTION = "MCP3424 GPIO Provider";

  /* Default settings:
   * Conversion: 12 bit
   * Input: Channel 1
   * Mode: Continuous
   */
  private int configuration = 0x90;

  private boolean i2cBusOwner = false;
  private final I2CBus bus;
  private final I2CDevice device;

  public MCP3424GpioProvider(int busNumber, int address) throws UnsupportedBusNumberException, IOException {
    // create I2C communications bus instance
    this(busNumber, address, 12, 1);
  }

  public MCP3424GpioProvider(int busNumber, int address, int resolution, int gain) throws UnsupportedBusNumberException, IOException {
    // create I2C communications bus instance
    this(I2CFactory.getInstance(busNumber), address, resolution, gain);
    i2cBusOwner = true;
  }

  public MCP3424GpioProvider(I2CBus bus, int address) throws UnsupportedBusNumberException, IOException {
    // create I2C communications bus instance
    this(bus, address, 12, 1);
  }

  public MCP3424GpioProvider(I2CBus bus, int address, int resolution, int gain) throws UnsupportedBusNumberException, IOException {
    super(MCP3424Pin.ALL_PINS);

    if((resolution != 12) && (resolution != 14) && (resolution != 16) && (resolution != 18)) {
      throw new IllegalArgumentException("Invalid resolution: " + resolution);
    }

    if((gain != 1) && (gain != 2) && (gain != 4) && (gain != 8)) {
      throw new IllegalArgumentException("Invalid gain: " + gain);
    }

    gain = 31 - Integer.numberOfLeadingZeros(gain);
    configuration = (((resolution - 12) << 1) | gain | configuration) & 0xFF;

    // set reference to I2C communications bus instance
    this.bus = bus;

    // create I2C device instance
    device = bus.getDevice(address);

    // Reset with general call
    byte reset[] = {0x00, 0x06};
    device.write(reset, 0, reset.length);
  }

  public void setGain(int gain) throws IllegalArgumentException {
    if((gain != 1) && (gain != 2) && (gain != 4) && (gain != 8)) {
      throw new IllegalArgumentException("Invalid gain: " + gain);
    }

    configuration = configuration & 0xFC; // Reset gain bits
    configuration = configuration | (31 - Integer.numberOfLeadingZeros(gain)); // Set new gain
    configuration = configuration | 0x80; // Force recalculation in one-shot mode
  }

  public void setResolution(int resolution) throws IllegalArgumentException {
    if((resolution != 12) && (resolution != 14) && (resolution != 16) && (resolution != 18)) {
      throw new IllegalArgumentException("Invalid resolution: " + resolution);
    }

    configuration = configuration & 0xF3; // Reset resolution bits
    configuration = configuration | ((resolution - 12) << 1); // Set new resolution
    configuration = configuration | 0x80; // Force recalculation in one-shot mode
  }

  @Override
  public void shutdown() {
    // prevent reentrant invocation
    if(isShutdown())
      return;

    // perform shutdown login in base
    super.shutdown();

    // if we are the owner of the I2C bus, then close it
    if(i2cBusOwner) {
      try {
        bus.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public String getName() {
    return NAME;
  }

 /**
  * Get the requested analog input pin's conversion value.
  * @param pin to get conversion values for
  * @return analog input pin value in Volt
  */
  public double getAnalogValue(Pin pin) {
    double value = getValue(pin);
    switch (configuration & 0x0C) {
      case 0x00: { // 1mV
        break;
      }
      case 0x04: { // 250.0 uV
        value = value * 0.25;
        break;
      }
      case 0x08: { // 62.5 uV
        value = value * 0.0625;
        break;
      }
      case 0x0C: { // 15.625 uV
        value = value * 0.015625;
        break;
      }
      default: throw new IllegalArgumentException();
    }
    return 0.001 * value;
  }

 /**
  * Get the requested analog input pin's conversion value.
  * @param pin to get conversion values for
  * @return analog input pin value in Volt
  */
  public double getAnalogValue(GpioPinAnalogInput pin) {
    return getAnalogValue(pin.getPin());
  }

  /**
   * This method will perform an immediate data acquisition directly to the ADC chip to get the requested pin's input
   * conversion value.
   *
   * @param pin requested input pin to acquire conversion value
   * @return conversion value for requested analog input pin
   * @throws IOException
   */
  @Override
  public double getImmediateValue(final Pin pin) throws IOException {
    // Pin address to read from to device
    int command = (configuration & 0x9F) | (pin.getAddress() << 5);
    device.write((byte)command); // Write configuration to device

    double rate = 0.0;
    byte data[] = null;
    switch (configuration & 0x0C) {
      case 0x00: {
        data = new byte[3];
    	rate = 176.0 /*240.0*/;
        break;
      }
      case 0x04: {
        data = new byte[3];
        rate = 44.0  /*60.0 */;
        break;
      }
      case 0x08: {
        data = new byte[3];
        rate = 11.0  /*15.0 */;
        break;
      }
      case 0x0C: {
        data = new byte[4];
        rate = 2.75  /*3.75 */;
        break;
      }
      default: throw new IllegalArgumentException();
    }

    // Sleep current thread during conversion
    try {
      Thread.sleep((long)Math.ceil(1000.0 / rate));
	} catch (InterruptedException ex) {
      ex.printStackTrace();
	}

    // Get answer from device
    device.read(data, 0, data.length);

    int answer = data[data.length - 1] & 0xFF;
    double weight = (data[0] & 0x80) == 0 ? 1 : -1;
//    weight = weight / (1 << (answer & 0x03));
    if(((answer & 0x60) == (command & 0x60)) && ((answer & 0x80) == 0)) {
      if(weight < 0) {
        for (int i = 0; i < data.length - 1; i++) {
          data[i] = (byte)(~data[i] & 0xFF);
        }
        data[data.length - 2] = (byte)(data[data.length - 2] + 1);
      }

      switch (configuration & 0x0C) {
        case 0x00: {
          data[0] = (byte)(data[0] & 0x07);
          break;
        }
        case 0x04: {
          data[0] = (byte)(data[0] & 0x1F);
          break;
        }
        case 0x08: {
          data[0] = (byte)(data[0] & 0x7F);
          break;
        }
        case 0x0C: {
          data[0] = (byte)(data[0] & 0x01);
          break;
        }
        default: throw new IllegalArgumentException();
      }

      int buffer = data[0] & 0xFF;
      for (int i = 1; i < data.length - 1; i++) {
        buffer = (buffer << 8) | (data[i] & 0xFF);
      }

      // validate value within acceptable range
      double value = buffer * weight;
	  if (value >= getMinSupportedValue() && value <= getMaxSupportedValue()) {
		getPinCache(pin).setAnalogValue(value);
		return value;
	  }
    }
    return INVALID_VALUE;
  }

  /**
   * Get the minimum supported analog value for the ADC implementation.
   *
   * @return Returns the minimum supported analog value.
   */
  @Override
  public double getMinSupportedValue() {
      switch (configuration & 0x0C) {
      case 0x00:  return -2048.0;
      case 0x04:  return -8192.0;
      case 0x08:  return -32768.0;
      case 0x0C:  return -131072.0;
      default: throw new IllegalArgumentException();
    }
  }

  /**
   * Get the maximum supported analog value for the ADC implementation.
   *
   * (For example, a 10 bit ADC's maximum value is 1023 and a 12-bit ADC's maximum value is 4095.
   *
   * @return Returns the maximum supported analog value.
   */
  @Override
  public double getMaxSupportedValue() {
      switch (configuration & 0x0C) {
      case 0x00:  return 2047.0;
      case 0x04:  return 8191.0;
      case 0x08:  return 32767.0;
      case 0x0C:  return 131071.0;
      default: throw new IllegalArgumentException();
    }
  }
}
