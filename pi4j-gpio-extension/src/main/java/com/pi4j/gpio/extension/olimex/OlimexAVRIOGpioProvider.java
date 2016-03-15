package com.pi4j.gpio.extension.olimex;

import com.pi4j.gpio.extension.serial.SerialCommandQueueProcessingThread;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.GpioProviderBase;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.PinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.io.serial.SerialFactory;

import java.io.IOException;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  OlimexAVRIOGpioProvider.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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
 * This GPIO provider implements the Olimex AVR-IO-M-16 expansion board as native Pi4J GPIO pins.
 * More information about the board can be found here: *
 * https://www.olimex.com/Products/AVR/Development/AVR-IO-M16/
 * </p>
 *
 * <p>
 * The Olimex AVR-IO board is connected via RS232 serial connection to the Raspberry Pi and provides
 * 4 electromechanical RELAYs and 4 opto-isolated INPUT pins.
 * </p>
 *
 * @link http://www.olimex.com/Products/AVR/Development/AVR-IO-M16/
 * @author Robert Savage
 *
 */
public class OlimexAVRIOGpioProvider extends GpioProviderBase implements GpioProvider {

    public static final String NAME = "com.pi4j.gpio.extension.olimex.OlimexAVRIOGpioProvider";
    public static final String DESCRIPTION = "Olimex AVR-IO GPIO Provider";
    private Serial com;
    private int currentStates = 0;
    private SerialCommandQueueProcessingThread queue;

    public OlimexAVRIOGpioProvider(String serialDevice) throws IOException {
        // create serial communications instance
        com = SerialFactory.createInstance();

        // create serial data listener
        SerialExampleListener listener = new SerialExampleListener();

        // add/register the serial data listener
        com.addListener(listener);

        // open serial port for communication
        com.open(serialDevice, 19200);

        // create and start the serial command processing queue thread
        // set the delay time to 100 ms; this works well for the AVR-IO
        queue = new SerialCommandQueueProcessingThread(com, 50);
        queue.start();
        queue.put("?"); // query for current status
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void setMode(Pin pin, PinMode mode) {
        // ALL PIN MODES ARE PREDEFINED
        //
        // an exception will be throw by the base impl
        // if an alternate mode is selected for a pin
        // instance
        super.setMode(pin, mode);
    }

    @Override
    public PinMode getMode(Pin pin) {
        super.getMode(pin);

        // return first mode found; this device has singular fixed pin modes
        for(PinMode mode : pin.getSupportedPinModes())
            return mode;

        return null;
    }

    @Override
    public void setState(Pin pin, PinState state) {
        super.setState(pin, state);

        // turn ON/OFF relay pins
        if (state == PinState.HIGH) {
            queue.put("+" + pin.getAddress());
        } else {
            queue.put("-" + pin.getAddress());
        }
    }

    @Override
    public PinState getState(Pin pin) {
        super.getState(pin);

        // calculate current state from the bitmask value
        int bit = (int)Math.pow(2, (pin.getAddress()-1));
        int state = (currentStates & bit);
        return (state == bit) ? PinState.HIGH : PinState.LOW;
    }


    @Override
    public void shutdown() {

        // prevent reentrant invocation
        if(isShutdown())
            return;

        // perform shutdown login in base
        super.shutdown();

        // if a serial processing queue is running, then shut it down now
        if (queue != null) {
            // shutdown serial data processing thread
            queue.shutdown();
            //queue.interrupt();
            queue = null;
        }

        // close the serial port communication
        try {
            com.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // shutdown any serial data monitoring threads
        SerialFactory.shutdown();
    }

    /**
     * This class implements the serial data listener interface with the callback method for event
     * notifications when data is received on the serial port.
     *
     * @see com.pi4j.io.serial.SerialDataEventListener
     * @author Robert Savage
     */
    class SerialExampleListener implements SerialDataEventListener {
        private StringBuilder buffer = new StringBuilder();

        public void dataReceived(SerialDataEvent event) {

            try {
                String data = event.getAsciiString();

                // append received data into buffer
                if (data != null && !data.isEmpty()) {
                    buffer.append(data);
                }

                int start = buffer.indexOf("$");
                int stop = buffer.indexOf("\n");

                while (stop >= 0) {
                    // process data buffer
                    if (start >= 0 && stop > start) {
                        // get command
                        String command = buffer.substring(start, stop + 1);
                        buffer.delete(start, stop + 1).toString();

                        // remove terminating characters
                        command = command.replace("$", "");
                        command = command.replace("\n", "");
                        command = command.replace("\r", "");

                        // print out the data received to the console
                        //System.out.println("<<< COM RX >>> " + command);

                        int value = Integer.parseInt(command, 16);

                        // process each INPUT pin for changes;
                        // dispatch change events if needed
                        for (Pin pin : OlimexAVRIOPin.INPUTS) {
                            evaluatePinForChange(pin, value);
                        }

                        // update the current value tracking variable
                        currentStates = value;
                    } else if (stop >= 0) {
                        // invalid data command; purge
                        buffer.delete(0, stop + 1);

                        //System.out.println("PURGE >>> " + removed);
                    }

                    // seek to next command in buffer
                    start = buffer.indexOf("$");
                    stop = buffer.indexOf("\n");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }


        private void evaluatePinForChange(Pin pin, int value) {
            int bit = (int)Math.pow(2, (pin.getAddress()-1));
            if ((value & bit) != (currentStates & bit)) {
                // change detected for INPUT PIN
                //System.out.println("<<< CHANGE >>> " + pin.getName());
                dispatchPinChangeEvent(pin.getAddress(), ((value & bit) == bit) ? PinState.HIGH : PinState.LOW);
            }
        }


        private void dispatchPinChangeEvent(int pinAddress, PinState state) {
            // iterate over the pin listeners map
            for (Pin pin : listeners.keySet()) {
                //System.out.println("<<< DISPATCH >>> " + pin.getName() + " : " + state.getName());

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
