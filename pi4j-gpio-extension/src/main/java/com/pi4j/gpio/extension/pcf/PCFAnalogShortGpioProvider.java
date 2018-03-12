package com.pi4j.gpio.extension.pcf;

import java.io.IOException;

import com.pi4j.gpio.helpers.GpioHelpers;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.GpioProviderBase;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.event.PinAnalogShortValueChangeEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

public class PCFAnalogShortGpioProvider extends GpioProviderBase implements GpioProvider {

    public static final String NAME = "com.pi4j.gpio.extension.ti.PCFAnalogShortGpioProvider";
    public static final String DESCRIPTION = "PCF Analog Short GPIO Provider";

    //these addresses belong to PCF8574(P)
    public static final int PCF8574_0x20 = 0x48; // 000
    public static final int PCF8574_0x21 = 0x21; // 001
    public static final int PCF8574_0x22 = 0x22; // 010
    public static final int PCF8574_0x23 = 0x23; // 011
    public static final int PCF8574_0x24 = 0x24; // 100
    public static final int PCF8574_0x25 = 0x25; // 101
    public static final int PCF8574_0x26 = 0x26; // 110
    public static final int PCF8574_0x27 = 0x27; // 111
    //these addresses belong to PCF8574A(P)
    public static final int PCF8574A_0x38 = 0x38; // 000
    public static final int PCF8574A_0x39 = 0x39; // 001
    public static final int PCF8574A_0x3A = 0x3A; // 010
    public static final int PCF8574A_0x3B = 0x3B; // 011
    public static final int PCF8574A_0x3C = 0x3C; // 100
    public static final int PCF8574A_0x3D = 0x3D; // 101
    public static final int PCF8574A_0x3E = 0x3E; // 110
    public static final int PCF8574A_0x3F = 0x3F; // 111
    //these addresses belong to PCF8591
    public static final int PCF8591_0x48 = 0x48;

    public static final int PCF_MAX_IO_PINS = 8;

    private boolean i2cBusOwner = false;
    private I2CBus bus;
    private I2CDevice device;
    private GpioValueMonitor monitor = null;

    private short[] threshold = { 1, 1, 1, 1 , 1, 1, 1, 1 };
    private short defaultThreshold = 1;
    private short[] cachedValue = { 0, 0, 0, 0 , 0, 0, 0, 0 };
    private int monitorInterval = 50;

    public PCFAnalogShortGpioProvider(int busNumber, int address) throws UnsupportedBusNumberException, IOException {
        // create I2C communications bus instance
        this(I2CFactory.getInstance(busNumber), address);
        i2cBusOwner = true;
    }

    public PCFAnalogShortGpioProvider(I2CBus bus, int address) throws IOException {

        // set reference to I2C communications bus instance
        this.bus = bus;

        // create I2C device instance
        device = bus.getDevice(address);

        // start monitoring thread
        monitor = new GpioValueMonitor(device);
        monitor.start();
    }

    public short getDefaultThreshold() {
        return defaultThreshold;
    }

    public void setDefaultThreshold(short defaultThreshold) {
        for (int i = 0; i < PCF_MAX_IO_PINS; i++) {
            threshold[i] = defaultThreshold;
        }

        this.defaultThreshold = defaultThreshold;
    }

    public short getThreshold(Pin pin) {
        return threshold[pin.getAddress()];
    }

    public void setThreshold(Pin pin, short threshold) {
        this.threshold[pin.getAddress()] = threshold;
    }

    public int getMonitorInterval() {
        return monitorInterval;
    }

    public void setMonitorInterval(int monitorInterval) {
        this.monitorInterval = monitorInterval;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public double getValue(Pin pin) {
        return (cachedValue[pin.getAddress()]);
    }

    public short getShortValue(Pin pin) {
        return cachedValue[pin.getAddress()];
    }

    public short getImmediateShortValue(Pin pin) {
        try {
            byte[] buffer = new byte[1];
            device.read(pin.getAddress(), buffer, 0, 1);

            return GpioHelpers.bytesToShort(buffer, 0);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    private class GpioValueMonitor extends Thread {

        private I2CDevice device;
        private boolean shuttingDown = false;

        public GpioValueMonitor(I2CDevice device) {
            this.device = device;
        }

        public void shutdown() {
            shuttingDown = true;
        }

        public void run() {
            while (!shuttingDown) {
                try {
                    for (Pin pin : PCFAnalogShortPin.ALL) {
                        short oldValue = cachedValue[pin.getAddress()];
                        short newValue = getImmediateShortValue(pin);

                        if (Math.abs(oldValue - newValue) > threshold[pin.getAddress()]) {
                            cachedValue[pin.getAddress()] = newValue;

                            if (getMode(pin) == PinMode.ANALOG_INPUT) {
                                dispatchPinChangeEvent(pin.getAddress(), newValue);
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

        private void dispatchPinChangeEvent(int pinAddress, short value) {
            // iterate over the pin listeners map
            for (Pin pin : listeners.keySet()) {
                // dispatch this event to the listener
                // if a matching pin address is found
                if (pin.getAddress() == pinAddress) {
                    // dispatch this event to all listener handlers
                    for (PinListener listener : listeners.get(pin)) {
                        listener.handlePinEvent(new PinAnalogShortValueChangeEvent(this, pin, value));
                    }
                }
            }
        }
    }
}