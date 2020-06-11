package com.pi4j.gpio.extension.ads;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  ADS1x15GpioProvider.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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


import java.io.IOException;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.GpioProviderBase;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.event.PinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

/**
 * <p>
 * This GPIO provider implements the TI ADS1x15 analog to digital converter chip as native Pi4J GPIO pins.
 *
 * More information about the board can be found here: *
 *
 * >> ADS1115
 * http://www.ti.com/lit/ds/symlink/ads1115.pdf
 * http://adafruit.com/datasheets/ads1115.pdf
 *
 * >> ADS1015
 * http://www.ti.com/lit/ds/symlink/ads1015.pdf
 * http://adafruit.com/datasheets/ads1015.pdf
 *
 * </p>
 *
 * <p>
 * The ADS1x15 is connected via I2C connection to the Raspberry Pi and provides
 * 2 GPIO pins that can be used for analog input pins.
 * </p>
 *
 * TODO: Add support for ALARM pin using a GPIO to notify for events.
 *       This would be more efficient than constantly polling each
 *       ADB pin in the monitoring thread.
 *
 *
 * @author Robert Savage
 *
 */
public abstract class ADS1x15GpioProvider extends GpioProviderBase implements GpioProvider {

    public static final String NAME = "com.pi4j.gpio.extension.ads.ADS1x15GpioProvider";
    public static final String DESCRIPTION = "ADS1x15 GPIO Provider";

    protected boolean i2cBusOwner = false;
    protected I2CBus bus;
    protected I2CDevice device;
    protected ADCMonitor monitor = null;
    protected Pin[] allPins = null;
    protected int conversionDelay = 0;
    protected short bitShift = 0;

    // minimum allowed background monitoring interval in milliseconds
    public static int MIN_MONITOR_INTERVAL = 1;

    // default background monitoring interval in milliseconds
    public static int DEFAULT_MONITOR_INTERVAL = 100;

    // =======================================================================
    // POINTER REGISTER
    // =======================================================================
    protected static final int ADS1x15_REG_POINTER_MASK      = 0x03;
    protected static final int ADS1x15_REG_POINTER_CONVERT   = 0x00;
    protected static final int ADS1x15_REG_POINTER_CONFIG    = 0x01;
    protected static final int ADS1x15_REG_POINTER_LOWTHRESH = 0x02;
    protected static final int ADS1x15_REG_POINTER_HITHRESH  = 0x03;

    // =======================================================================
    // CONFIG REGISTER
    // =======================================================================
    protected static final int  ADS1x15_REG_CONFIG_OS_MASK      = 0x8000;
    protected static final int  ADS1x15_REG_CONFIG_OS_SINGLE    = 0x8000;  // Write: Set to start a single-conversion
    protected static final int  ADS1x15_REG_CONFIG_OS_BUSY      = 0x0000;  // Read: Bit = 0 when conversion is in progress
    protected static final int  ADS1x15_REG_CONFIG_OS_NOTBUSY   = 0x8000;  // Read: Bit = 1 when device is not performing a conversion

    protected static final int  ADS1x15_REG_CONFIG_MUX_MASK     = 0x7000;
    protected static final int  ADS1x15_REG_CONFIG_MUX_DIFF_0_1 = 0x0000;  // Differential P = AIN0, N = AIN1 (default)
    protected static final int  ADS1x15_REG_CONFIG_MUX_DIFF_0_3 = 0x1000;  // Differential P = AIN0, N = AIN3
    protected static final int  ADS1x15_REG_CONFIG_MUX_DIFF_1_3 = 0x2000;  // Differential P = AIN1, N = AIN3
    protected static final int  ADS1x15_REG_CONFIG_MUX_DIFF_2_3 = 0x3000;  // Differential P = AIN2, N = AIN3
    protected static final int  ADS1x15_REG_CONFIG_MUX_SINGLE_0 = 0x4000;  // Single-ended AIN0
    protected static final int  ADS1x15_REG_CONFIG_MUX_SINGLE_1 = 0x5000;  // Single-ended AIN1
    protected static final int  ADS1x15_REG_CONFIG_MUX_SINGLE_2 = 0x6000;  // Single-ended AIN2
    protected static final int  ADS1x15_REG_CONFIG_MUX_SINGLE_3 = 0x7000;  // Single-ended AIN3

    protected static final int  ADS1x15_REG_CONFIG_PGA_MASK     = 0x0E00;
    protected static final int  ADS1x15_REG_CONFIG_PGA_6_144V   = 0x0000;  // +/-6.144V range
    protected static final int  ADS1x15_REG_CONFIG_PGA_4_096V   = 0x0200;  // +/-4.096V range
    protected static final int  ADS1x15_REG_CONFIG_PGA_2_048V   = 0x0400;  // +/-2.048V range (default)
    protected static final int  ADS1x15_REG_CONFIG_PGA_1_024V   = 0x0600;  // +/-1.024V range
    protected static final int  ADS1x15_REG_CONFIG_PGA_0_512V   = 0x0800;  // +/-0.512V range
    protected static final int  ADS1x15_REG_CONFIG_PGA_0_256V   = 0x0A00;  // +/-0.256V range

    protected static final int  ADS1x15_REG_CONFIG_MODE_MASK    = 0x0100;
    protected static final int  ADS1x15_REG_CONFIG_MODE_CONTIN  = 0x0000;  // Continuous conversion mode
    protected static final int  ADS1x15_REG_CONFIG_MODE_SINGLE  = 0x0100;  // Power-down single-shot mode (default)

    protected static final int  ADS1x15_REG_CONFIG_DR_MASK      = 0x00E0;
    protected static final int  ADS1x15_REG_CONFIG_DR_128SPS    = 0x0000;  // 128 samples per second
    protected static final int  ADS1x15_REG_CONFIG_DR_250SPS    = 0x0020;  // 250 samples per second
    protected static final int  ADS1x15_REG_CONFIG_DR_490SPS    = 0x0040;  // 490 samples per second
    protected static final int  ADS1x15_REG_CONFIG_DR_920SPS    = 0x0060;  // 920 samples per second
    protected static final int  ADS1x15_REG_CONFIG_DR_1600SPS   = 0x0080;  // 1600 samples per second (default)
    protected static final int  ADS1x15_REG_CONFIG_DR_2400SPS   = 0x00A0;  // 2400 samples per second
    protected static final int  ADS1x15_REG_CONFIG_DR_3300SPS   = 0x00C0;  // 3300 samples per second

    protected static final int  ADS1x15_REG_CONFIG_CMODE_MASK   = 0x0010;
    protected static final int  ADS1x15_REG_CONFIG_CMODE_TRAD   = 0x0000;  // Traditional comparator with hysteresis (default)
    protected static final int  ADS1x15_REG_CONFIG_CMODE_WINDOW = 0x0010;  // Window comparator

    protected static final int  ADS1x15_REG_CONFIG_CPOL_MASK    = 0x0008;
    protected static final int  ADS1x15_REG_CONFIG_CPOL_ACTVLOW = 0x0000;  // ALERT/RDY pin is low when active (default)
    protected static final int  ADS1x15_REG_CONFIG_CPOL_ACTVHI  = 0x0008;  // ALERT/RDY pin is high when active

    protected static final int  ADS1x15_REG_CONFIG_CLAT_MASK    = 0x0004;  // Determines if ALERT/RDY pin latches once asserted
    protected static final int  ADS1x15_REG_CONFIG_CLAT_NONLAT  = 0x0000;  // Non-latching comparator (default)
    protected static final int  ADS1x15_REG_CONFIG_CLAT_LATCH   = 0x0004;  // Latching comparator

    protected static final int  ADS1x15_REG_CONFIG_CQUE_MASK    = 0x0003;
    protected static final int  ADS1x15_REG_CONFIG_CQUE_1CONV   = 0x0000;  // Assert ALERT/RDY after one conversions
    protected static final int  ADS1x15_REG_CONFIG_CQUE_2CONV   = 0x0001;  // Assert ALERT/RDY after two conversions
    protected static final int  ADS1x15_REG_CONFIG_CQUE_4CONV   = 0x0002;  // Assert ALERT/RDY after four conversions
    protected static final int  ADS1x15_REG_CONFIG_CQUE_NONE    = 0x0003;  // Disable the comparator and put ALERT/RDY in high state (default)


    public enum ProgrammableGainAmplifierValue{
        PGA_6_144V(6.144,ADS1x15_REG_CONFIG_PGA_6_144V),  // +/-6.144V range
        PGA_4_096V(4.096,ADS1x15_REG_CONFIG_PGA_4_096V),  // +/-4.096V range
        PGA_2_048V(2.048,ADS1x15_REG_CONFIG_PGA_2_048V),  // +/-2.048V range (default)
        PGA_1_024V(1.024,ADS1x15_REG_CONFIG_PGA_1_024V),  // +/-1.024V range
        PGA_0_512V(0.512,ADS1x15_REG_CONFIG_PGA_0_512V),  // +/-0.512V range
        PGA_0_256V(0.256,ADS1x15_REG_CONFIG_PGA_0_256V);   // +/-0.256V range

        private double voltage = 6.144;
        private int configValue = ADS1x15_REG_CONFIG_PGA_6_144V;

        private ProgrammableGainAmplifierValue(double voltage, int configValue){
          this.voltage = voltage;
          this.configValue = configValue;
        }

        public double getVoltage(){
            return this.voltage;
        }

        public int getConfigValue(){
            return this.configValue;
        }
    }

    // defines the PGA used when reading the analog input value
    protected ProgrammableGainAmplifierValue[] pga = { ProgrammableGainAmplifierValue.PGA_6_144V,
                                                       ProgrammableGainAmplifierValue.PGA_6_144V,
                                                       ProgrammableGainAmplifierValue.PGA_6_144V,
                                                       ProgrammableGainAmplifierValue.PGA_6_144V};

    // the threshold used to determine if a significant value warrants an event to be raised
    protected double[] threshold = { 500, 500, 500, 500 };

    // this cache value is used to track last known pin values for raising event
    protected double[] cachedValue = { 0, 0, 0, 0 };

    // this value defines the sleep time between value reads by the event monitoring thread
    protected int monitorInterval = DEFAULT_MONITOR_INTERVAL;

    public ADS1x15GpioProvider(int busNumber, int address) throws UnsupportedBusNumberException, IOException {

        // create I2C communications bus instance
        this(I2CFactory.getInstance(busNumber), address);
        i2cBusOwner = true;
    }

    public ADS1x15GpioProvider(I2CBus bus, int address) throws IOException {

        // set reference to I2C communications bus instance
        this.bus = bus;

        // create I2C device instance
        device = bus.getDevice(address);

        // The first byte sent by the master should be the ADS1113/4/5 address followed
        // by a bit that instructs the ADS1113/4/5 to listen for a subsequent byte.
        // The second byte is the register pointer.
        // The third and fourth bytes sent from the master are written to the
        // register indicated in the second byte.

        // Write to Config register:
        // - First byte: 0b10010000 (first 7-bit I2C address followed by a low read/write bit)
        // - Second byte: 0b00000001 (points to Config register)
        // - Third byte: 0b10000100 (MSB of the Config register to be written)
        // - Fourth byte: 0b10000011 (LSB of the Config register to be written)

        // Write to Pointer register:
        // - First byte: 0b10010000 (first 7-bit I2C address followed by a low read/write bit)
        // - Second byte: 0b00000000 (points to Conversion Register)

        // Read Conversion register:
        // - First byte: 0b10010001 (first 7-bit I2C address followed by a high read/write bit)
        // - Second byte: the ADS1113/4/5 response with the MSB of the Conversion register
        // - Third byte: the ADS1113/4/5 response with the LSB of the Conversion register


        // set all default pin cache states to match documented chip power up states
        //for (Pin pin : allPins) {
          //  getPinCache(pin).setState(PinState.HIGH);
            //currentStates.set(pin.getAddress(), true);
        //}

        // start monitoring thread
        monitor = new ADS1x15GpioProvider.ADCMonitor(device);
        monitor.start();
    }



    public ProgrammableGainAmplifierValue getProgrammableGainAmplifier(Pin pin){
        return pga[pin.getAddress()];
    }

    public ProgrammableGainAmplifierValue getProgrammableGainAmplifier(GpioPin pin){
        return getProgrammableGainAmplifier(pin.getPin());
    }

    public void setProgrammableGainAmplifier(ProgrammableGainAmplifierValue pga, Pin...pin){
        for(Pin p : pin){
            this.pga[p.getAddress()] = pga;
        }
    }

    public void setProgrammableGainAmplifier(ProgrammableGainAmplifierValue pga, GpioPin...pin){
        for(GpioPin p : pin){
            this.pga[p.getPin().getAddress()] = pga;
        }
    }

    public double getEventThreshold(Pin pin){
        return threshold[pin.getAddress()];
    }

    public double getEventThreshold(GpioPin pin){
        return getEventThreshold(pin.getPin());
    }

    public void setEventThreshold(double threshold, Pin...pin){
        for(Pin p : pin){
            this.threshold[p.getAddress()] = threshold;
        }
    }

    public void setEventThreshold(double threshold, GpioPin...pin){
        for(GpioPin p : pin){
            setEventThreshold(threshold, p.getPin());
        }
    }

    public int getMonitorInterval(){
        return monitorInterval;
    }

    public void setMonitorInterval(int monitorInterval){
        this.monitorInterval = monitorInterval;
        if(monitorInterval < MIN_MONITOR_INTERVAL)
            monitorInterval = DEFAULT_MONITOR_INTERVAL;
    }

    @Override
    public abstract String getName();


    public double getImmediateValue(Pin pin) throws IOException {

        // Start with default values
        int config = ADS1x15_REG_CONFIG_CQUE_NONE    | // Disable the comparator (default val)
                     ADS1x15_REG_CONFIG_CLAT_NONLAT  | // Non-latching (default val)
                     ADS1x15_REG_CONFIG_CPOL_ACTVLOW | // Alert/Rdy active low   (default val)
                     ADS1x15_REG_CONFIG_CMODE_TRAD   | // Traditional comparator (default val)
                     ADS1x15_REG_CONFIG_DR_1600SPS   | // 1600 samples per second (default)
                     ADS1x15_REG_CONFIG_MODE_SINGLE;   // Single-shot mode (default)

        // Set PGA/voltage range
        config |= pga[pin.getAddress()].getConfigValue();  // +/- 6.144V range (limited to VDD +0.3V max!)

        // Set single-ended input channel
        switch (pin.getAddress())
        {
          case (0):
            config |= ADS1x15_REG_CONFIG_MUX_SINGLE_0;
            break;
          case (1):
            config |= ADS1x15_REG_CONFIG_MUX_SINGLE_1;
            break;
          case (2):
            config |= ADS1x15_REG_CONFIG_MUX_SINGLE_2;
            break;
          case (3):
            config |= ADS1x15_REG_CONFIG_MUX_SINGLE_3;
            break;
        }

        // Set 'start single-conversion' bit
        config |= ADS1x15_REG_CONFIG_OS_SINGLE;

        // Write config register to the ADC
        writeRegister(ADS1x15_REG_POINTER_CONFIG, config);

        // Wait for the conversion to complete
        try{
            if(conversionDelay > 0){
                Thread.sleep(conversionDelay);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        // read the conversion results
        int value = readRegister(ADS1x15_REG_POINTER_CONVERT);

        getPinCache(pin).setAnalogValue(value);
        return value;

    };

    // Writes 16-bits to the specified destination register
    protected void writeRegister(int register, int value) throws IOException {

        // create packet in data buffer
        byte packet[] = new byte[3];
        packet[0] = (byte)(register);     // register byte
        packet[1] = (byte)(value>>8);     // value MSB
        packet[2] = (byte)(value & 0xFF); // value LSB

        // write data to I2C device
        device.write(packet, 0, 3);
    }

    // Writes 16-bits to the specified destination register
    protected int readRegister(int register) throws IOException {

        device.write((byte)register);

        // create data buffer for receive data
        byte buffer[] = new byte[2];  // receive 16 bits (2 bytes)
        int byteCount = 0;
        try
        {
            byteCount = device.read(buffer, 0, 2);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if(byteCount == 2){

            //System.out.println("-----------------------------------------------");
            //System.out.println("[RX] " + bytesToHex(buffer));
            //System.out.println("-----------------------------------------------");
            short value = getShort(buffer, 0);

            // Shift 12-bit results right 4 bits for the ADS1015
            // No-shift required for the ADS1115
            if(bitShift > 0){
                value = (short) (value >> bitShift);
            }

            return value;
        }
        else{
            return 0;
        }
    }

    protected static short getShort(byte[] arr, int off) {
        return (short) (arr[off]<<8 &0xFF00 | arr[off+1]&0xFF);
    }

    protected static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
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

            // if we are the owner of the I2C bus, then close it
            if(i2cBusOwner) {
                // close the I2C bus communication
                bus.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * This class/thread is used to to actively monitor for GPIO interrupts
     *
     * @author Robert Savage
     *
     */
    private class ADCMonitor extends Thread {

        private I2CDevice device;
        private boolean shuttingDown = false;

        public ADCMonitor(I2CDevice device) {
            this.device = device;
        }

        public void shutdown() {
            shuttingDown = true;
        }

        public void run() {
            while (!shuttingDown) {
                try {
                    // read device pins state
                    byte[] buffer = new byte[1];
                    device.read(buffer, 0, 1);

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
