package com.pi4j.gpio.extension.piface;

import java.io.IOException;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.GpioProviderBase;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.PinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.gpio.exception.InvalidPinException;
import com.pi4j.io.gpio.exception.InvalidPinModeException;
import com.pi4j.io.gpio.exception.UnsupportedPinModeException;
import com.pi4j.io.gpio.exception.UnsupportedPinPullResistanceException;
import com.pi4j.wiringpi.Spi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  PiFaceGpioProvider.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * #L%
 */

/**
 * <p>
 * This GPIO provider implements the Pi-FACE GPIO expansion board as native Pi4J GPIO pins.
 * More information about the board can be found here: *
 * http://piface.openlx.org.uk/174770794
 * </p>
 * 
 * <p>
 * The Pi-FACE board used a MCP23S17 connected via SPI connection to the Raspberry Pi and provides 8 
 * GPIO digital input pins and 8 GPIO digital output pins. 
 * </p>
 * 
 * @author Robert Savage
 * 
 */
public class PiFaceGpioProvider extends GpioProviderBase implements GpioProvider {

    public static final String NAME = "com.pi4j.gpio.extension.piface.PiFaceGpioProvider";
    public static final String DESCRIPTION = "Pi-Face GPIO Provider";
    public static final byte DEFAULT_ADDRESS = 0b01000000; // 0x40

    private static final byte REGISTER_IODIR_A = 0x00;
    private static final byte REGISTER_IODIR_B = 0x01;
    private static final byte REGISTER_GPINTEN_A = 0x04;
    private static final byte REGISTER_GPINTEN_B = 0x05;
    private static final byte REGISTER_DEFVAL_A = 0x06;
    private static final byte REGISTER_DEFVAL_B = 0x07;
    private static final byte REGISTER_INTCON_A = 0x08;
    private static final byte REGISTER_INTCON_B = 0x09;
    private static final byte REGISTER_IOCON = 0x0A;
    private static final byte REGISTER_GPPU_A = 0x0C;
    private static final byte REGISTER_GPPU_B = 0x0D;
    private static final byte REGISTER_INTF_A = 0x0E;
    private static final byte REGISTER_INTF_B = 0x0F;
    // private static final byte REGISTER_INTCAP_A = 0x10;
    // private static final byte REGISTER_INTCAP_B = 0x11;
    private static final byte REGISTER_GPIO_A = 0x12;
    private static final byte REGISTER_GPIO_B = 0x13;

    private static final int GPIO_A_OFFSET = 0;
    private static final int GPIO_B_OFFSET = 1000;

    private int currentStatesA = 0b00000000;
    private int currentStatesB = 0b11111111;
    private int currentDirectionA = 0b00000000;
    private int currentDirectionB = 0b11111111;
    private int currentPullupA = 0b00000000;
    private int currentPullupB = 0b11111111;
    private byte address = DEFAULT_ADDRESS;

    private GpioStateMonitor monitor = null;
    
    public static final int SPI_SPEED = 1000000;    
    public static final byte WRITE_FLAG = 0b00000000;    // 0x00
    public static final byte READ_FLAG  = 0b00000001;    // 0x01
    
    public PiFaceGpioProvider(byte spiAddress, int spiChannel) throws IOException {
        this(spiAddress, spiChannel, SPI_SPEED);
    }
    
    public PiFaceGpioProvider(byte spiAddress, int spiChannel, int spiSpeed) throws IOException {

        // setup SPI for communication
        int fd = Spi.wiringPiSPISetup(spiChannel, spiSpeed);
        if (fd <= -1) {
            throw new IOException("SPI port setup failed.");
        }
        
        // enable hardware addressing
        write(REGISTER_IOCON, (byte) 8);
        
        // set all default pins directions
        write(REGISTER_IODIR_A, (byte) currentDirectionA);
        write(REGISTER_IODIR_B, (byte) currentDirectionB);

        // set all default pin interrupts
        write(REGISTER_GPINTEN_A, (byte) currentDirectionA);
        write(REGISTER_GPINTEN_B, (byte) currentDirectionB);

        // set all default pin interrupt default values
        write(REGISTER_DEFVAL_A, (byte) 0x00);
        write(REGISTER_DEFVAL_B, (byte) 0x00);

        // set all default pin interrupt comparison behaviors
        write(REGISTER_INTCON_A, (byte) 0x00);
        write(REGISTER_INTCON_B, (byte) 0x00);

        // set all default pin states
        write(REGISTER_GPIO_A, (byte) currentStatesA);
        write(REGISTER_GPIO_B, (byte) currentStatesB);

        // set all default pin pull up resistors
        write(REGISTER_GPPU_A, (byte) currentPullupA);
        write(REGISTER_GPPU_B, (byte) currentPullupB);
    }

    protected void write(byte register, byte data) {

        // create packet in data buffer
        byte packet[] = new byte[3];
        packet[0] = (byte)(address|WRITE_FLAG);   // address byte
        packet[1] = register;                     // register byte
        packet[2] = data;                         // data byte
           
        // send data packet
        Spi.wiringPiSPIDataRW(0, packet, 3);        
    }

    protected byte read(byte register){
        
        // create packet in data buffer
        byte packet[] = new byte[3];
        packet[0] = (byte)(address|READ_FLAG);   // address byte
        packet[1] = register;                    // register byte
        packet[2] = 0b00000000;                  // data byte
        
        int result = Spi.wiringPiSPIDataRW(0, packet, 3); 
        if(result >= 0)
            return packet[2];
        else
            throw new RuntimeException("Invalid SPI read operation: " + result);
    }    
    
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void export(Pin pin, PinMode mode) {
        // make sure to set the pin mode
        super.export(pin, mode);
        setMode(pin, mode);
    }

    @Override
    public void unexport(Pin pin) {
        super.unexport(pin);
        setMode(pin, PinMode.DIGITAL_OUTPUT);
    }

    @Override
    public void setMode(Pin pin, PinMode mode) {
        // validate
        if (!pin.getSupportedPinModes().contains(mode)) {
            throw new InvalidPinModeException(pin, "Invalid pin mode [" + mode.getName()
                    + "]; pin [" + pin.getName() + "] does not support this mode.");
        }
        // validate
        if (!pin.getSupportedPinModes().contains(mode)) {
            throw new UnsupportedPinModeException(pin, mode);
        }
        // determine A or B port based on pin address
        try {
            if (pin.getAddress() < GPIO_B_OFFSET) {
                setModeA(pin, mode);
            } else {
                setModeB(pin, mode);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // cache mode
        getPinCache(pin).setMode(mode);
        
        // if any pins are configured as input pins, then we need to start the interrupt monitoring
        // thread
        if (currentDirectionA > 0 || currentDirectionB > 0) {
            // if the monitor has not been started, then start it now
            if (monitor == null) {
                // start monitoring thread
                monitor = new GpioStateMonitor(this);
                monitor.start();
            }
        } else {
            // shutdown and destroy monitoring thread since there are no input pins configured
            if (monitor != null) {
                monitor.shutdown();
                monitor = null;
            }
        }
    }

    private void setModeA(Pin pin, PinMode mode) throws IOException {
        // determine register and pin address
        int pinAddress = pin.getAddress() - GPIO_A_OFFSET;

        // determine update direction value based on mode
        if (mode == PinMode.DIGITAL_INPUT) {
            currentDirectionA |= pinAddress;
        } else if (mode == PinMode.DIGITAL_OUTPUT) {
            currentDirectionA &= ~pinAddress;
        }

        // next update direction value
        write(REGISTER_IODIR_A, (byte) currentDirectionA);

        // enable interrupts; interrupt on any change from previous state
        write(REGISTER_GPINTEN_A, (byte) currentDirectionA);
    }

    private void setModeB(Pin pin, PinMode mode) throws IOException {
        // determine register and pin address
        int pinAddress = pin.getAddress() - GPIO_B_OFFSET;

        // determine update direction value based on mode
        if (mode == PinMode.DIGITAL_INPUT) {
            currentDirectionB |= pinAddress;
        } else if (mode == PinMode.DIGITAL_OUTPUT) {
            currentDirectionB &= ~pinAddress;
        }

        // next update direction (mode) value
        write(REGISTER_IODIR_B, (byte) currentDirectionB);

        // enable interrupts; interrupt on any change from previous state
        write(REGISTER_GPINTEN_B, (byte) currentDirectionB);
    }

    @Override
    public PinMode getMode(Pin pin) {
        return super.getMode(pin);
    }

    @Override
    public void setState(Pin pin, PinState state) {
        // validate
        if (hasPin(pin) == false) {
            throw new InvalidPinException(pin);
        }
        // only permit invocation on pins set to DIGITAL_OUTPUT modes
        if (getPinCache(pin).getMode() != PinMode.DIGITAL_OUTPUT) {
            throw new InvalidPinModeException(pin, "Invalid pin mode on pin [" + pin.getName()
                    + "]; cannot setState() when pin mode is ["
                    + getPinCache(pin).getMode().getName() + "]");
        }
        try {
            // determine A or B port based on pin address
            if (pin.getAddress() < GPIO_B_OFFSET) {
                setStateA(pin, state);
            } else {
                setStateB(pin, state);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // cache pin state
        getPinCache(pin).setState(state);
    }

    private void setStateA(Pin pin, PinState state) throws IOException {
        // determine pin address
        int pinAddress = pin.getAddress() - GPIO_A_OFFSET;

        // determine state value for pin bit
        if (state.isHigh()) {
            currentStatesA |= pinAddress;
        } else {
            currentStatesA &= ~pinAddress;
        }

        // update state value
        write(REGISTER_GPIO_A, (byte) currentStatesA);
    }

    private void setStateB(Pin pin, PinState state) throws IOException {
        // determine pin address
        int pinAddress = pin.getAddress() - GPIO_B_OFFSET;

        // determine state value for pin bit
        if (state.isHigh()) {
            currentStatesB |= pinAddress;
        } else {
            currentStatesB &= ~pinAddress;
        }

        // update state value
        write(REGISTER_GPIO_B, (byte) currentStatesB);
    }

    @Override
    public PinState getState(Pin pin) {
        // call super method to perform validation on pin
        PinState result  = super.getState(pin);
        
        // determine A or B port based on pin address 
        if (pin.getAddress() < GPIO_B_OFFSET) {
            result = getStateA(pin); // get pin state
        } else {
            result = getStateB(pin); // get pin state
        }
        
        // return pin state
        return result;
    }
    
    private PinState getStateA(Pin pin){
        
        // determine pin address
        int pinAddress = pin.getAddress() - GPIO_A_OFFSET;
        
        // determine pin state
        PinState state = (currentStatesA & pinAddress) == pinAddress ? PinState.HIGH : PinState.LOW;

        // cache state
        getPinCache(pin).setState(state);
        
        return state;
    }
    
    private PinState getStateB(Pin pin){
        
        // determine pin address
        int pinAddress = pin.getAddress() - GPIO_B_OFFSET;
        
        // determine pin state
        PinState state = (currentStatesB & pinAddress) == pinAddress ? PinState.HIGH : PinState.LOW;

        // cache state
        getPinCache(pin).setState(state);
        
        return state;
    }    

    @Override
    public void setPullResistance(Pin pin, PinPullResistance resistance) {
        // validate
        if (hasPin(pin) == false) {
            throw new InvalidPinException(pin);
        }
        // validate
        if (!pin.getSupportedPinPullResistance().contains(resistance)) {
            throw new UnsupportedPinPullResistanceException(pin, resistance);
        }
        try {
            // determine A or B port based on pin address
            if (pin.getAddress() < GPIO_B_OFFSET) {
                setPullResistanceA(pin, resistance);
            } else {
                setPullResistanceB(pin, resistance);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // cache resistance
        getPinCache(pin).setResistance(resistance);
    }

    private void setPullResistanceA(Pin pin, PinPullResistance resistance) throws IOException {
        // determine pin address
        int pinAddress = pin.getAddress() - GPIO_A_OFFSET;

        // determine pull up value for pin bit
        if (resistance == PinPullResistance.PULL_UP) {
            currentPullupA |= pinAddress;
        } else {
            currentPullupA &= ~pinAddress;
        }

        // next update pull up resistor value
        write(REGISTER_GPPU_A, (byte) currentPullupA);
    }

    private void setPullResistanceB(Pin pin, PinPullResistance resistance) throws IOException {
        // determine pin address
        int pinAddress = pin.getAddress() - GPIO_B_OFFSET;

        // determine pull up value for pin bit
        if (resistance == PinPullResistance.PULL_UP) {
            currentPullupB |= pinAddress;
        } else {
            currentPullupB &= ~pinAddress;
        }

        // next update pull up resistor value
        write(REGISTER_GPPU_B, (byte) currentPullupB);
    }

    @Override
    public PinPullResistance getPullResistance(Pin pin) {
        return super.getPullResistance(pin);
    }
    
    
    @Override
    public void shutdown() {
        
        // prevent reentrant invocation
        if(isShutdown())
            return;
        
        // perform shutdown login in base
        super.shutdown();
        
        // if a monitor is running, then shut it down now
        if (monitor != null) {
            // shutdown monitoring thread
            monitor.shutdown();
            monitor = null;
        }
    }   

    
    /**
     * This class/thread is used to to actively monitor for GPIO interrupts
     * 
     * @author Robert Savage
     * 
     */
    private class GpioStateMonitor extends Thread {
        private PiFaceGpioProvider provider;
        private boolean shuttingDown = false;

        public GpioStateMonitor(PiFaceGpioProvider provider) {
            this.provider = provider;
        }

        public void shutdown() {
            shuttingDown = true;
        }

        public void run() {
            while (!shuttingDown) {
                try {
                    // only process for interrupts if a pin on port A is configured as an input pin
                    if (currentDirectionA > 0) {
                        // process interrupts for port A
                        byte pinInterruptA = provider.read(REGISTER_INTF_A);

                        // validate that there is at least one interrupt active on port A
                        if (pinInterruptA > 0) {
                            // read the current pin states on port A
                            byte pinInterruptState = provider.read(REGISTER_GPIO_A);

                            // loop over the available pins on port B
                            for (Pin pin : PiFacePin.OUTPUTS) {
                                int pinAddressA = pin.getAddress() - GPIO_A_OFFSET;
                                
                                // is there an interrupt flag on this pin?
                                if ((pinInterruptA & pinAddressA) > 0) {
                                    // System.out.println("INTERRUPT ON PIN [" + pin.getName() + "]");
                                    evaluatePinForChangeA(pin, pinInterruptState);
                                }
                            }
                        }
                    }

                    // only process for interrupts if a pin on port B is configured as an input pin
                    if (currentDirectionB > 0) {
                        // process interrupts for port B
                        int pinInterruptB = (int)provider.read(REGISTER_INTF_B);

                        // validate that there is at least one interrupt active on port B
                        if (pinInterruptB > 0) {
                            // read the current pin states on port B
                            int pinInterruptState = (int)provider.read(REGISTER_GPIO_B);

                            // loop over the available pins on port B
                            for (Pin pin : PiFacePin.INPUTS) {
                                int pinAddressB = pin.getAddress() - GPIO_B_OFFSET;

                                // is there an interrupt flag on this pin?
                                if ((pinInterruptB & pinAddressB) > 0) {
                                    //System.out.println("INTERRUPT ON PIN [" + pin.getName() + "]");
                                    evaluatePinForChangeB(pin, pinInterruptState);
                                }
                            }
                        }
                    }

                    // ... lets take a short breather ...
                    Thread.currentThread();
                    Thread.sleep(50);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void evaluatePinForChangeA(Pin pin, int state) {
            if (getPinCache(pin).isExported()) {
                // determine pin address
                int pinAddress = pin.getAddress() - GPIO_A_OFFSET;

                if ((state & pinAddress) != (currentStatesA & pinAddress)) {
                    PinState newState = (state & pinAddress) == pinAddress ? PinState.HIGH
                            : PinState.LOW;

                    // cache state
                    getPinCache(pin).setState(newState);

                    // determine and cache state value for pin bit
                    if (newState.isHigh()) {
                        currentStatesA |= pinAddress;
                    } else {
                        currentStatesA &= ~pinAddress;
                    }

                    // change detected for INPUT PIN
                    // System.out.println("<<< CHANGE >>> " + pin.getName() + " : " + state);
                    dispatchPinChangeEvent(pin.getAddress(), newState);
                }
            }
        }

        private void evaluatePinForChangeB(Pin pin, int state) {
            if (getPinCache(pin).isExported()) {
                // determine pin address
                int pinAddress = pin.getAddress() - GPIO_B_OFFSET;

                if ((state & pinAddress) != (currentStatesB & pinAddress)) {
                    PinState newState = (state & pinAddress) == pinAddress ? PinState.HIGH
                            : PinState.LOW;

                    // cache state
                    getPinCache(pin).setState(newState);

                    // determine and cache state value for pin bit
                    if (newState.isHigh()) {
                        currentStatesB |= pinAddress;
                    } else {
                        currentStatesB &= ~pinAddress;
                    }

                    // change detected for INPUT PIN
                    // System.out.println("<<< CHANGE >>> " + pin.getName() + " : " + state);
                    dispatchPinChangeEvent(pin.getAddress(), newState);
                }
            }
        }

        private void dispatchPinChangeEvent(int pinAddress, PinState state) {
            // iterate over the pin listeners map
            for (Pin pin : listeners.keySet()) {
                // System.out.println("<<< DISPATCH >>> " + pin.getName() + " : " +
                // state.getName());

                // dispatch this event to the listener
                // if a matching pin address is found
                if (pin.getAddress() == pinAddress) {
                    // dispatch this event to all listener handlers
                    for (PinListener listener : listeners.get(pin)) {
                        listener.handlePinEvent(new PinDigitalStateChangeEvent(this, pin, state));
                    }
                }
            }
        }
    }
}
