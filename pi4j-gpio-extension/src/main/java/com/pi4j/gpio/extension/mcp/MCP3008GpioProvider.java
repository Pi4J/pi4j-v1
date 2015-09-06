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

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.PinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.io.spi.SpiMode;
import com.pi4j.wiringpi.Spi;
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
 *
 * Note: This implementation currently only supports single-ended inputs.
 * </p>
 *
 * @author pojd
 */
public class MCP3008GpioProvider extends GpioProviderBase implements GpioProvider {

    public static final int INPUT_COUNT = 8;
	public static final int MAX_VALUE = 1023; // 10-bit ADC can produce values from 0 to 1023
    public static final int MIN_VALUE = 0;    // 10-bit ADC can produce values from 0 to 1023
    public static final int MIN_MONITOR_INTERVAL = 50; // milliseconds

    // default amount the input value has to change before publishing a value change event
    public static final int DEFAULT_THRESHOLD = 5;

    public static final String NAME = "com.pi4j.gpio.extension.mcp.MCP3008GpioProvider";
	public static final String DESCRIPTION = "MCP3008 GPIO Provider";
	public static final int INVALID_VALUE = -1;

	private final SpiDevice device;
    protected ADCMonitor monitor = null;
    protected int conversionDelay = 0;

    // this value defines the sleep time between value reads by the event monitoring thread (in milliseconds)
    protected int monitorInterval = 100;

    // the threshold used to determine if a significant value warrants an event to be raised
    protected double[] threshold = { DEFAULT_THRESHOLD, DEFAULT_THRESHOLD, DEFAULT_THRESHOLD,
                                     DEFAULT_THRESHOLD, DEFAULT_THRESHOLD, DEFAULT_THRESHOLD,
                                     DEFAULT_THRESHOLD, DEFAULT_THRESHOLD};

    // this cache value is used to track last known pin values for raising event
    protected double[] cachedValue = { 0, 0, 0, 0, 0, 0, 0, 0 };

	/**
	 * Create new instance of this MCP3008 provider with background monitoring and pin notification events enabled.
	 *
	 * @param channel
	 *            spi channel the MCP3008 is connected to
	 * @throws IOException
	 *             if an error occurs during initialization of the SpiDevice
	 */
	public MCP3008GpioProvider(SpiChannel channel) throws IOException {
        this(channel, SpiDevice.DEFAULT_SPI_SPEED, SpiDevice.DEFAULT_SPI_MODE, true);
    }

    /**
     * Create new instance of this MCP3008 provider with background monitoring and pin notification events enabled.
     *
     * @param channel
     *            spi channel the MCP3008 is connected to
     * @param speed
     *            spi speed to communicate with MCP3008
     * @throws IOException
     *             if an error occurs during initialization of the SpiDevice
     */
    public MCP3008GpioProvider(SpiChannel channel, int speed) throws IOException {
        this(channel, speed, SpiDevice.DEFAULT_SPI_MODE, true);
    }

    /**
     * Create new instance of this MCP3008 provider with background monitoring and pin notification events enabled.
     *
     * @param channel
     *            spi channel the MCP3008 is connected to
     * @param mode
     *            spi mode to communicate with MCP3008
     * @throws IOException
     *             if an error occurs during initialization of the SpiDevice
     */
    public MCP3008GpioProvider(SpiChannel channel, SpiMode mode) throws IOException {
        this(channel, SpiDevice.DEFAULT_SPI_SPEED, mode, true);
    }

    /**
     * Create new instance of this MCP3008 provider with background monitoring and pin notification events enabled.
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
        this(channel, speed, mode, true);
    }

    /**
     * Create new instance of this MCP3008 provider.  Optionally enable or disable background monitoring
     * and pin notification events.
     *
     * @param channel
     *            spi channel the MCP3008 is connected to
     * @param speed
     *            spi speed to communicate with MCP3008
     * @param mode
     *            spi mode to communicate with MCP3008
     * @param enableBackgroundMonitoring
     *            if enabled, then a background thread will be created
     *            to constantly acquire the ADC input values and publish
     *            pin change listeners if the value change is beyond the
     *            configured threshold.
     * @throws IOException
     *             if an error occurs during initialization of the SpiDevice
     */
    public MCP3008GpioProvider(SpiChannel channel, int speed, SpiMode mode, boolean enableBackgroundMonitoring) throws IOException {
        this.device = SpiFactory.getInstance(channel, speed, mode);

        // start monitoring thread
        if(enableBackgroundMonitoring) {
            monitor = new MCP3008GpioProvider.ADCMonitor();
            monitor.start();
        }
    }

    // ------------------------------------------------------------------------------------------
    // PUBLIC METHODS
    // ------------------------------------------------------------------------------------------
	@Override
	public String getName() {
		return NAME;
	}

    /**
     * Get the requested analog input pin's conversion value.
     *
     * If you have the background monitoring thread enabled, then
     * this function will return the last cached value.  If you have the
     * background monitoring thread disabled, then this function will
     * will perform an immediate data acquisition directly to the ADC chip
     * to get the requested pin's input conversion value. (via getImmediateValue())
     *
     * @param pin to get conversion values for
     * @return analog input pin conversion value (10-bit: 0 to 1023)
     */
	@Override
	public double getValue(Pin pin) {
        // if we are not actively monitoring the ADC input values,
        // then interrogate the ADC chip and return the acquired input conversion value
        if(monitor == null) {
            // do not return, only let parent handle whether this pin is OK
            super.getValue(pin);
            try {
                return getImmediateValue(pin);
            } catch (IOException e) {
                return INVALID_VALUE;
            }
        }
        else{
            // if we are actively monitoring the ADC input values,
            // the simply return the last cached input value
            return super.getValue(pin);
        }
    }

    /**
     * This method is used by the framework to shutdown the
     * background monitoring thread if needed when the program exits.
     */
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
     * Get the event threshold value for a given analog input pin.
     *
     * The event threshold value determines how much change in the
     * analog input pin's conversion value must occur before the
     * framework issues an analog input pin change event.  A threshold
     * is necessary to prevent a significant number of analog input
     * change events from getting propagated and dispatched for input
     * values that may have an expected range of drift.
     *
     * see the DEFAULT_THRESHOLD constant for the default threshold value.
     *
     * @param pin analog input pin
     * @return event threshold value for requested analog input pin
     */
    public double getEventThreshold(Pin pin){
        return threshold[pin.getAddress()];
    }

    /**
     * Get the event threshold value for a given analog input pin.
     *
     * The event threshold value determines how much change in the
     * analog input pin's conversion value must occur before the
     * framework issues an analog input pin change event.  A threshold
     * is necessary to prevent a significant number of analog input
     * change events from getting propagated and dispatched for input
     * values that may have an expected range of drift.
     *
     * see the DEFAULT_THRESHOLD constant for the default threshold value.
     *
     * @param pin analog input pin
     * @return event threshold value for requested analog input pin
     */
    public double getEventThreshold(GpioPin pin){
        return getEventThreshold(pin.getPin());
    }

    /**
     * Set the event threshold value for a given analog input pin.
     *
     * The event threshold value determines how much change in the
     * analog input pin's conversion value must occur before the
     * framework issues an analog input pin change event.  A threshold
     * is necessary to prevent a significant number of analog input
     * change events from getting propagated and dispatched for input
     * values that may have an expected range of drift.
     *
     * see the DEFAULT_THRESHOLD constant for the default threshold value.
     *
     * @param threshold value between 0 and 1023.
     * @param pin analog input pin (vararg, one or more inputs can be defined.)
     */
    public void setEventThreshold(double threshold, Pin...pin){
        for(Pin p : pin){
            this.threshold[p.getAddress()] = threshold;
        }
    }

    /**
     * Set the event threshold value for a given analog input pin.
     *
     * The event threshold value determines how much change in the
     * analog input pin's conversion value must occur before the
     * framework issues an analog input pin change event.  A threshold
     * is necessary to prevent a significant number of analog input
     * change events from getting propagated and dispatched for input
     * values that may have an expected range of drift.
     *
     * see the DEFAULT_THRESHOLD constant for the default threshold value.
     *
     * @param threshold value between 0 and 1023.
     * @param pin analog input pin (vararg, one or more inputs can be defined.)
     */
    public void setEventThreshold(double threshold, GpioPin...pin){
        for(GpioPin p : pin){
            setEventThreshold(threshold, p.getPin());
        }
    }

    /**
     * Get the background monitoring thread's rate of data acquisition. (in milliseconds)
     *
     * The default interval is 100 milliseconds.
     * The minimum supported interval is 50 milliseconds.
     *
     * @return monitoring interval in milliseconds
     */
    public int getMonitorInterval(){
        return monitorInterval;
    }

    /**
     * Change the background monitoring thread's rate of data acquisition. (in milliseconds)
     *
     * The default interval is 100 milliseconds.
     * The minimum supported interval is 50 milliseconds.
     *
     * @param monitorInterval
     */
    public void setMonitorInterval(int monitorInterval){
        this.monitorInterval = monitorInterval;

        // enforce a minimum interval threshold.
        if(monitorInterval < MIN_MONITOR_INTERVAL)
            monitorInterval = MIN_MONITOR_INTERVAL;
    }

    /**
     * This method will perform an immediate data acquisition directly to the ADC chip to get the
     * requested pin's input conversion value.
     *
     * @param pin requested input pin to acquire conversion value
     * @return conversion value for requested analog input pin
     * @throws IOException
     */
    public double getImmediateValue(Pin pin) throws IOException {
        double value = isInitiated() ? readAnalog(toCommand((short) pin.getAddress())) : INVALID_VALUE;
        getPinCache(pin).setAnalogValue(value);
        return value;
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

                                // no need to continue if we received an invalid value from the ADC chip.
                                if(newValue <= INVALID_VALUE){ break; }

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
