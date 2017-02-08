package com.pi4j.gpio.extension.mcp;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.PinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.gpio.exception.InvalidPinException;
import com.pi4j.io.gpio.exception.UnsupportedPinPullResistanceException;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

import java.io.IOException;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  MCP23S17GpioProvider.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2017 Pi4J
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

/**
 * <p>
 * This GPIO provider implements the MCP23S17 SPI GPIO expansion board as native Pi4J GPIO pins.
 * More information about the board can be found here: *
 * http://ww1.microchip.com/downloads/en/DeviceDoc/21952b.pdf
 * </p>
 *
 * <p>
 * The MCP23S17 is connected via SPI connection to the Raspberry Pi and provides 16 GPIO pins that
 * can be used for either digital input or digital output pins.
 * </p>
 *
 * @author Robert Savage
 *
 */
public class MCP23S17GpioProvider extends GpioProviderBase implements GpioProvider {

    public static final String NAME = "com.pi4j.gpio.extension.mcp.MCP23S17GpioProvider";
    public static final String DESCRIPTION = "MCP23S17 GPIO Provider";

    // SPI Address Register  0b[0 1 0 0 A2 A1 A0 x]
    public static final byte ADDRESS_0 = 0b01000000; // 0x40 [0100 0000] [A0 = 0 | A1 = 0 | A2 = 0]
    public static final byte ADDRESS_1 = 0b01000010; // 0x42 [0100 0010] [A0 = 1 | A1 = 0 | A2 = 0]
    public static final byte ADDRESS_2 = 0b01000100; // 0x44 [0100 0100] [A0 = 0 | A1 = 1 | A2 = 0]
    public static final byte ADDRESS_3 = 0b01000110; // 0x46 [0100 0110] [A0 = 1 | A1 = 1 | A2 = 0]
    public static final byte ADDRESS_4 = 0b01001000; // 0x48 [0100 1000] [A0 = 0 | A1 = 0 | A2 = 1]
    public static final byte ADDRESS_5 = 0b01001010; // 0x4A [0100 1010] [A0 = 1 | A1 = 0 | A2 = 1]
    public static final byte ADDRESS_6 = 0b01001100; // 0x4C [0100 1100] [A0 = 0 | A1 = 1 | A2 = 1]
    public static final byte ADDRESS_7 = 0b01001110; // 0x4E [0100 1110] [A0 = 1 | A1 = 1 | A2 = 1]
    public static final byte DEFAULT_ADDRESS = ADDRESS_0;

    private static final byte REGISTER_IODIR_A = 0x00;
    private static final byte REGISTER_IODIR_B = 0x01;
    private static final byte REGISTER_GPINTEN_A = 0x04;
    private static final byte REGISTER_GPINTEN_B = 0x05;
    private static final byte REGISTER_DEFVAL_A = 0x06;
    private static final byte REGISTER_DEFVAL_B = 0x07;
    private static final byte REGISTER_INTCON_A = 0x08;
    private static final byte REGISTER_INTCON_B = 0x09;
    private static final byte REGISTER_IOCON_A = 0x0A;
    private static final byte REGISTER_IOCON_B = 0x0B;
    private static final byte REGISTER_GPPU_A = 0x0C;
    private static final byte REGISTER_GPPU_B = 0x0D;
    private static final byte REGISTER_INTF_A = 0x0E;
    private static final byte REGISTER_INTF_B = 0x0F;
    private static final byte REGISTER_INTCAP_A = 0x10;
    private static final byte REGISTER_INTCAP_B = 0x11;
    private static final byte REGISTER_GPIO_A = 0x12;
    private static final byte REGISTER_GPIO_B = 0x13;

    private static final int GPIO_A_OFFSET = 0;
    private static final int GPIO_B_OFFSET = 1000;

    private static final byte IOCON_UNUSED    = (byte)0x01;
    private static final byte IOCON_INTPOL    = (byte)0x02;
    private static final byte IOCON_ODR       = (byte)0x04;
    private static final byte IOCON_HAEN      = (byte)0x08;
    private static final byte IOCON_DISSLW    = (byte)0x10;
    private static final byte IOCON_SEQOP     = (byte)0x20;
    private static final byte IOCON_MIRROR    = (byte)0x40;
    private static final byte IOCON_BANK_MODE = (byte)0x80;

    private int currentStatesA = 0;
    private int currentStatesB = 0;
    private int currentDirectionA = 0;
    private int currentDirectionB = 0;
    private int currentPullupA = 0;
    private int currentPullupB = 0;
    private byte address = DEFAULT_ADDRESS;

    private GpioStateMonitor monitor = null;
    private final SpiDevice spi;

    public static final int SPI_SPEED = 1000000;
    public static final byte WRITE_FLAG = 0b00000000;    // 0x00
    public static final byte READ_FLAG  = 0b00000001;    // 0x01

    public MCP23S17GpioProvider(byte spiAddress, int spiChannel) throws IOException {
        this(spiAddress, spiChannel, SPI_SPEED);
    }

    public MCP23S17GpioProvider(byte spiAddress, SpiChannel spiChannel) throws IOException {
        this(spiAddress, spiChannel, SPI_SPEED);
    }

    public MCP23S17GpioProvider(byte spiAddress, int spiChannel, int spiSpeed) throws IOException {
        this(spiAddress, SpiChannel.getByNumber(spiChannel), spiSpeed);
    }

    public MCP23S17GpioProvider(byte spiAddress, SpiChannel spiChannel, int spiSpeed) throws IOException {

        // IOCON – I/O EXPANDER CONFIGURATION REGISTER
        //
        // bit 7 BANK: Controls how the registers are addressed
        //     1 = The registers associated with each port are separated into different banks
        //     0 = The registers are in the same bank (addresses are sequential)
        // bit 6 MIRROR: INT Pins Mirror bit
        //     1 = The INT pins are internally connected
        //     0 = The INT pins are not connected. INTA is associated with PortA and INTB is associated with PortB
        // bit 5 SEQOP: Sequential Operation mode bit.
        //     1 = Sequential operation disabled, address pointer does not increment.
        //     0 = Sequential operation enabled, address pointer increments.
        // bit 4 DISSLW: Slew Rate control bit for SDA output.
        //     1 = Slew rate disabled.
        //     0 = Slew rate enabled.
        // bit 3 HAEN: Hardware Address Enable bit (MCP23S17 only).
        //     Address pins are always enabled on MCP23017.
        //     1 = Enables the MCP23S17 address pins.
        //     0 = Disables the MCP23S17 address pins.
        // bit 2 ODR: This bit configures the INT pin as an open-drain output.
        //     1 = Open-drain output (overrides the INTPOL bit).
        //     0 = Active driver output (INTPOL bit sets the polarity).
        // bit 1 INTPOL: This bit sets the polarity of the INT output pin.
        //     1 = Active-high.
        //     0 = Active-low.
        // bit 0 Unimplemented: Read as ‘0’.
        //
        this(spiAddress ,spiChannel ,spiSpeed, (byte) 0x00001000);
    }

    public MCP23S17GpioProvider(byte spiAddress, int spiChannel, int spiSpeed, byte iocon) throws IOException {
        this(spiAddress, SpiChannel.getByNumber(spiChannel), spiSpeed, iocon);
    }

    public MCP23S17GpioProvider(byte spiAddress, SpiChannel spiChannel, int spiSpeed, byte iocon) throws IOException {

        // create SPi object instance SPI for communication
        spi = SpiFactory.getInstance(spiChannel, spiSpeed);

        // set SPI chip address
        this.address = spiAddress;

        // IOCON – I/O EXPANDER CONFIGURATION REGISTER
        //
        // bit 7 BANK: Controls how the registers are addressed
        //     1 = The registers associated with each port are separated into different banks
        //     0 = The registers are in the same bank (addresses are sequential)
        // bit 6 MIRROR: INT Pins Mirror bit
        //     1 = The INT pins are internally connected
        //     0 = The INT pins are not connected. INTA is associated with PortA and INTB is associated with PortB
        // bit 5 SEQOP: Sequential Operation mode bit.
        //     1 = Sequential operation disabled, address pointer does not increment.
        //     0 = Sequential operation enabled, address pointer increments.
        // bit 4 DISSLW: Slew Rate control bit for SDA output.
        //     1 = Slew rate disabled.
        //     0 = Slew rate enabled.
        // bit 3 HAEN: Hardware Address Enable bit (MCP23S17 only).
        //     Address pins are always enabled on MCP23017.
        //     1 = Enables the MCP23S17 address pins.
        //     0 = Disables the MCP23S17 address pins.
        // bit 2 ODR: This bit configures the INT pin as an open-drain output.
        //     1 = Open-drain output (overrides the INTPOL bit).
        //     0 = Active driver output (INTPOL bit sets the polarity).
        // bit 1 INTPOL: This bit sets the polarity of the INT output pin.
        //     1 = Active-high.
        //     0 = Active-low.
        // bit 0 Unimplemented: Read as ‘0’.
        //

        // write IO configuration
        write(REGISTER_IOCON_A, (byte)(IOCON_SEQOP|IOCON_HAEN));  // enable hardware address
        write(REGISTER_IOCON_B, (byte)(IOCON_SEQOP|IOCON_HAEN));  // enable hardware address

        // read initial GPIO pin states
        // (include the '& 0xFF' to ensure the bits in the unsigned byte are cast properly)
        currentStatesA = read(REGISTER_GPIO_A);
        currentStatesB = read(REGISTER_GPIO_B);

        // set all default pins directions
        // (1 = Pin is configured as an input.)
        // (0 = Pin is configured as an output.)
        write(REGISTER_IODIR_A, (byte) currentDirectionA);
        write(REGISTER_IODIR_B, (byte) currentDirectionB);

        // set all default pin states
        write(REGISTER_GPIO_A, (byte) currentStatesA);
        write(REGISTER_GPIO_B, (byte) currentStatesB);

        // set all default pin pull up resistors
        // (1 = Pull-up enabled.)
        // (0 = Pull-up disabled.)
        write(REGISTER_GPPU_A, (byte) currentPullupA);
        write(REGISTER_GPPU_B, (byte) currentPullupB);

        // set all default pin interrupts
        // (if pin direction is input (1), then enable interrupt for pin)
        // (1 = Enable GPIO input pin for interrupt-on-change event.)
        // (0 = Disable GPIO input pin for interrupt-on-change event.)
        write(REGISTER_GPINTEN_A, (byte) currentDirectionA);
        write(REGISTER_GPINTEN_B, (byte) currentDirectionB);

        // set all default pin interrupt default values
        // (comparison value registers are not used in this implementation)
        write(REGISTER_DEFVAL_A, (byte) 0x00);
        write(REGISTER_DEFVAL_B, (byte) 0x00);

        // set all default pin interrupt comparison behaviors
        // (1 = Controls how the associated pin value is compared for interrupt-on-change.)
        // (0 = Pin value is compared against the previous pin value.)
        write(REGISTER_INTCON_A, (byte) 0x00);
        write(REGISTER_INTCON_B, (byte) 0x00);

        // reset/clear interrupt flags
        if(currentDirectionA > 0)
            read(REGISTER_INTCAP_A);
        if(currentDirectionB > 0)
            read(REGISTER_INTCAP_B);
    }

    protected synchronized void write(byte register, byte data) throws IOException {
        // create packet in data buffer
        byte packet[] = new byte[3];
        packet[0] = (byte)(address|WRITE_FLAG);   // address byte
        packet[1] = register;                     // register byte
        packet[2] = data;                         // data byte

        // send data packet
        spi.write(packet);
    }

    protected synchronized int read(byte register) throws IOException {
        // create packet in data buffer
        byte packet[] = new byte[3];
        packet[0] = (byte) (address | READ_FLAG);   // address byte
        packet[1] = register;                    // register byte
        packet[2] = 0b00000000;                  // data byte

        byte[] result = spi.write(packet);

        // (include the '& 0xFF' to ensure the bits in the unsigned byte are cast properly)
        return result[2] & 0xFF;
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
        super.setMode(pin, mode);

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
        super.setState(pin, state);

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
        private MCP23S17GpioProvider provider;
        private boolean shuttingDown = false;

        public GpioStateMonitor(MCP23S17GpioProvider provider) {
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
                        int pinInterruptA = provider.read(REGISTER_INTF_A);

                        // validate that there is at least one interrupt active on port A
                        if (pinInterruptA > 0) {
                            // read the current pin states on port A
                            int pinInterruptState = provider.read(REGISTER_GPIO_A);

                            // loop over the available pins on port B
                            for (Pin pin : MCP23S17Pin.ALL_A_PINS) {
                                int pinAddressA = pin.getAddress() - GPIO_A_OFFSET;

                                // is there an interrupt flag on this pin?
                                //if ((pinInterruptA & pinAddressA) > 0) {
                                    // System.out.println("INTERRUPT ON PIN [" + pin.getName() + "]");
                                    evaluatePinForChangeA(pin, pinInterruptState);
                                //}
                            }
                        }
                    }

                    // only process for interrupts if a pin on port B is configured as an input pin
                    if (currentDirectionB > 0) {
                        // process interrupts for port B
                        int pinInterruptB = provider.read(REGISTER_INTF_B);

                        // validate that there is at least one interrupt active on port B
                        if (pinInterruptB > 0) {

                            // read the current pin states on port B
                            int pinInterruptState = provider.read(REGISTER_GPIO_B);

                            // loop over the available pins on port B
                            for (Pin pin : MCP23S17Pin.ALL_B_PINS) {
                                int pinAddressB = pin.getAddress() - GPIO_B_OFFSET;

                                // is there an interrupt flag on this pin?
                                //if ((pinInterruptB & pinAddressB) > 0) {
                                    //System.out.println("INTERRUPT ON PIN [" + pin.getName() + "]");
                                    evaluatePinForChangeB(pin, pinInterruptState);
                                //}
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
