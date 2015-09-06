package com.pi4j.gpio.extension.mcp;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  MCP3008GpioProvider.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.GpioProviderBase;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.event.PinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.io.spi.SpiMode;
import sun.security.provider.ConfigFile;

import java.io.IOException;

/**
 *
 * <p>
 * This GPIO provider implements the MCP3008 SPI GPIO expansion board as native Pi4J GPIO pins. It is a 10-bit ADC providing 8 input
 * channels. More information about the board can be found here: http://ww1.microchip.com/downloads/en/DeviceDoc/21295d.pdf
 * </p>
 *
 * <p>
 * The MCP3008 is connected via SPI connection to the Raspberry Pi and provides 8 GPIO pins that can be used for analog input pins. The
 * values returned are in the range 0-1023 (max 10 bit value).
 * </p>
 *
 * @author pojd
 */
public class MCP3008GpioProvider extends GpioProviderBase implements GpioProvider {

	public static final String NAME = "com.pi4j.gpio.extension.mcp.MCP3008GpioProvider";
	public static final String DESCRIPTION = "MCP3008 GPIO Provider";
	public static final int INVALID_VALUE = -1;

	private final SpiDevice device;
    protected ADCMonitor monitor = null;
    protected int conversionDelay = 0;

    // this value defines the sleep time between value reads by the event monitoring thread
    protected int monitorInterval = 100;

    // the threshold used to determine if a significant value warrants an event to be raised
    protected double[] threshold = { 500, 500, 500, 500 };

    // this cache value is used to track last known pin values for raising event
    protected double[] cachedValue = { 0, 0, 0, 0 };

	/**
	 * Create new instance of this MCP3008 provider.
	 *
	 * @param channel
	 *            spi channel the MCP3008 is connected to
	 * @throws IOException
	 *             if an error occurs during initialization of the SpiDevice
	 */
	public MCP3008GpioProvider(SpiChannel channel) throws IOException {
		this.device = SpiFactory.getInstance(channel);

        // start monitoring thread
        monitor = new MCP3008GpioProvider.ADCMonitor();
        monitor.start();
    }

    /**
     * Create new instance of this MCP3008 provider.
     *
     * @param channel
     *            spi channel the MCP3008 is connected to
     * @param speed
     *            spi speed to communicate with MCP3008
     * @throws IOException
     *             if an error occurs during initialization of the SpiDevice
     */
    public MCP3008GpioProvider(SpiChannel channel, int speed) throws IOException {
        this.device = SpiFactory.getInstance(channel, speed);

        // start monitoring thread
        monitor = new MCP3008GpioProvider.ADCMonitor();
        monitor.start();
    }

    /**
     * Create new instance of this MCP3008 provider.
     *
     * @param channel
     *            spi channel the MCP3008 is connected to
     * @param mode
     *            spi mode to communicate with MCP3008
     * @throws IOException
     *             if an error occurs during initialization of the SpiDevice
     */
    public MCP3008GpioProvider(SpiChannel channel, SpiMode mode) throws IOException {
        this.device = SpiFactory.getInstance(channel, mode);

        // start monitoring thread
        monitor = new MCP3008GpioProvider.ADCMonitor();
        monitor.start();
    }

    /**
     * Create new instance of this MCP3008 provider.
     *
     * @param channel
     *            spi channel the MCP3008 is connected to
     * @param speed
     *            spi speed to communicate with MCP3008
     * @param mode
     *            spi mode to communicate with MCP3008
     * @throws IOException
     *             if an error occurs during initialization of the SpiDevice
     */
    public MCP3008GpioProvider(SpiChannel channel, int speed, SpiMode mode) throws IOException {
        this.device = SpiFactory.getInstance(channel, speed);

        // start monitoring thread
        monitor = new MCP3008GpioProvider.ADCMonitor();
        monitor.start();
    }

    // ------------------------------------------------------------------------------------------
    // PUBLIC METHODS
    // ------------------------------------------------------------------------------------------
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public double getValue(Pin pin) {
		// do not return, only let parent handle whether this pin is OK
		super.getValue(pin);
        try {
            return getImmediateValue(pin);
        } catch (IOException e) {
            return INVALID_VALUE;
        }
    }

    // ------------------------------------------------------------------------------------------
    // internal methods
    // ------------------------------------------------------------------------------------------
	private short toCommand(short channel) {
		short command = (short) ((channel + 8) << 4);
		return command;
	}


	private boolean isInitiated() {
		return device != null;
	}


	private int readAnalog(short channelCommand) {
		// send 3 bytes command - "1", channel command and some extra byte 0
		// http://hertaville.com/2013/07/24/interfacing-an-spi-adc-mcp3008-chip-to-the-raspberry-pi-using-c
		short[] data = new short[] { 1, channelCommand, 0 };
		short[] result;
		try {
			result = device.write(data);
		} catch (IOException e) {
			return INVALID_VALUE;
		}

		// now take 8 and 9 bit from second byte (& with 0b11 and shift) and the whole last byte to form the value
		int analogValue = ((result[1] & 3) << 8) + result[2];
		return analogValue;
	}

    public double getImmediateValue(Pin pin) throws IOException {
        double value = isInitiated() ? readAnalog(toCommand((short) pin.getAddress())) : INVALID_VALUE;
        getPinCache(pin).setAnalogValue(value);
        return value;
    }

    @Override
    public void shutdown() {

        // prevent reentrant invocation
        if(isShutdown())
            return;

        // perform shutdown login in base
        super.shutdown();

        try {
            // if a monitor is running, then shut it down now
            if (monitor != null) {
                // shutdown monitoring thread
                monitor.shutdown();
                monitor = null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This class/thread is used to to actively monitor ADC input changes
     *
     * @author Robert Savage
     *
     */
    private class ADCMonitor extends Thread {

        private boolean shuttingDown = false;
        protected Pin[] allPins = null;

        public ADCMonitor() {
            allPins = MCP3008Pin.ALL;
        }

        public void shutdown() {
            shuttingDown = true;
        }

        public void run() {
            while (!shuttingDown) {
                try {
                    // determine if there is a pin state difference
                    if(allPins != null && allPins.length > 0){
                        for (Pin pin : allPins) {

                            try{
                                // get current cached value
                                double oldValue = cachedValue[pin.getAddress()];

                                // get actual value from ADC chip
                                double newValue = getImmediateValue(pin);

                                // check to see if the pin value exceeds the event threshold
                                if(Math.abs(oldValue - newValue) > threshold[pin.getAddress()]){

                                    // cache new value (both in local event comparison cache variable and pin state cache)
                                    cachedValue[pin.getAddress()] = newValue;
                                    getPinCache(pin).setAnalogValue(newValue);

                                    // only dispatch events for analog input pins
                                    if (getMode(pin) == PinMode.ANALOG_INPUT) {
                                        dispatchPinChangeEvent(pin.getAddress(), newValue);
                                    }
                                }

                                // Wait for the conversion to complete
                                try{
                                    if(conversionDelay > 0){
                                        Thread.sleep(conversionDelay);
                                    }
                                }
                                catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            catch(IOException ex){
                                // I2C read error
                            }
                        }
                    }

                    // ... lets take a short breather ...
                    Thread.currentThread();
                    Thread.sleep(monitorInterval);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void dispatchPinChangeEvent(int pinAddress, double value) {
            // iterate over the pin listeners map
            for (Pin pin : listeners.keySet()) {
                // dispatch this event to the listener
                // if a matching pin address is found
                if (pin.getAddress() == pinAddress) {
                    // dispatch this event to all listener handlers
                    for (PinListener listener : listeners.get(pin)) {
                        listener.handlePinEvent(new PinAnalogValueChangeEvent(this, pin, value));
                    }
                }
            }
        }
    }
}
