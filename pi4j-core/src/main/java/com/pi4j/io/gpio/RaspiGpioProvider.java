package com.pi4j.io.gpio;

import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.gpio.exception.InvalidPinException;
import com.pi4j.io.gpio.exception.InvalidPinModeException;
import com.pi4j.wiringpi.GpioInterruptEvent;
import com.pi4j.wiringpi.GpioInterruptListener;
import com.pi4j.wiringpi.GpioUtil;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  RaspiGpioProvider.java
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

/**
 * Raspberry PI {@link GpioProvider} implementation.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public class RaspiGpioProvider extends GpioProviderBase implements GpioProvider, GpioInterruptListener {

    public static final String NAME = "RaspberryPi GPIO Provider";

    // need enough space in array for maximum number of pins.
    // Currently the Computer module supports the highest number of pins.
    protected static short pinSupportedCache[] = new short[50];
    protected static PinMode pinModeCache[] = new PinMode[50];

    public RaspiGpioProvider() {
        // set wiringPi interface for internal use
        // we will use the (default) WiringPi pin number scheme with the wiringPi library
        this(RaspiPinNumberingScheme.DEFAULT_PIN_NUMBERING);
    }

    public RaspiGpioProvider(RaspiPinNumberingScheme pinNumberingScheme) {
        // set wiringPi interface for internal use
        switch(pinNumberingScheme){
            case BROADCOM_PIN_NUMBERING: {
                // we will use the raw/direct Broadcom GPIO pin number scheme with the wiringPi library
                com.pi4j.wiringpi.Gpio.wiringPiSetupGpio();
                break;
            }
            case DEFAULT_PIN_NUMBERING: {
                // we will use the WiringPi pin number scheme with the wiringPi library
                com.pi4j.wiringpi.Gpio.wiringPiSetup();
                break;
            }
            default: {
                throw new RuntimeException("Unsupported pin numbering scheme: " + pinNumberingScheme.name());
            }
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean hasPin(Pin pin) {
        if(pinSupportedCache[pin.getAddress()] == 1) {
            return true;
        }
        else if(pinSupportedCache[pin.getAddress()] == -1) {
            return false;
        }
        else{
            // add pin support to cache
            if(com.pi4j.wiringpi.GpioUtil.isPinSupported(pin.getAddress()) > 0) {
                pinSupportedCache[pin.getAddress()] = 1;
                return true;
            }
            else{
                pinSupportedCache[pin.getAddress()] = -1;
                return false;
            }
        }
    }

    @Override
    public void export(Pin pin, PinMode mode) {
        export(pin, mode, null);
    }

    @Override
    public void export(Pin pin, PinMode mode, PinState defaultState) {
        super.export(pin, mode);

        //System.out.println("-- EXPORTING PIN [" + pin.getAddress() + "] to mode [" + mode.getName() + "]");

        // get mode configured direction value
        int direction = mode.getDirection().getValue();

        // if a default state was provided and the direction is OUT, then override the
        // pin direction to include initial state value
        if(defaultState != null && mode.getDirection() == PinDirection.OUT){
            if(defaultState == PinState.LOW)
                direction = GpioUtil.DIRECTION_LOW;
            else if(defaultState == PinState.HIGH)
                direction = GpioUtil.DIRECTION_HIGH;
        }

        // if not already exported, export the pin and set the pin direction
        if(!com.pi4j.wiringpi.GpioUtil.isExported(pin.getAddress())){
            com.pi4j.wiringpi.GpioUtil.export(pin.getAddress(), direction);
        }
        // if the pin is already exported, then check its current configured direction
        // if the direction does not match, then set the new direction for the pin
        else if(com.pi4j.wiringpi.GpioUtil.getDirection(pin.getAddress()) != mode.getDirection().getValue()){
            com.pi4j.wiringpi.GpioUtil.setDirection(pin.getAddress(), direction);
        }

        // set the pin input/output mode
        setMode(pin, mode);
    }

    @Override
    public boolean isExported(Pin pin) {
        super.isExported(pin);

        // return the pin exported state
        return com.pi4j.wiringpi.GpioUtil.isExported(pin.getAddress());
    }

    @Override
    public void unexport(Pin pin) {
        super.unexport(pin);

        // unexport the pins
        com.pi4j.wiringpi.GpioUtil.unexport(pin.getAddress());
    }

    @Override
    public void setMode(Pin pin, PinMode mode) {
        super.setMode(pin, mode);

        // local pin mode cache
        pinModeCache[pin.getAddress()] = mode;

        // set pin mode on hardware
        com.pi4j.wiringpi.Gpio.pinMode(pin.getAddress(), mode.getValue());

        // if this is an input pin, then configure edge detection
        if (PinMode.allInputs().contains(mode)) {
            com.pi4j.wiringpi.GpioUtil.setEdgeDetection(pin.getAddress(), PinEdge.BOTH.getValue());
        }
    }

    @Override
    public PinMode getMode(Pin pin) {
        return pinModeCache[pin.getAddress()];
    }

    @Override
    public void setPullResistance(Pin pin, PinPullResistance resistance) {
        super.setPullResistance(pin, resistance);

        com.pi4j.wiringpi.Gpio.pullUpDnControl(pin.getAddress(), resistance.getValue());
    }

//    @Override
//    public PinPullResistance getPullResistance(Pin pin) {
//        // TODO : get actual pin pull resistance from native impl
//        return super.getPullResistance(pin);
//    }

    @Override
    public void setState(Pin pin, PinState state) {

        // validate pin
        if (!hasPin(pin)) {
            throw new InvalidPinException(pin);
        }

        // only permit invocation on pins set to DIGITAL_OUTPUT modes
        if (pinModeCache[pin.getAddress()] != PinMode.DIGITAL_OUTPUT) {
            throw new InvalidPinModeException(pin, "Invalid pin mode on pin [" + pin.getName() + "]; cannot setState() when pin mode is [" + pinModeCache[pin.getAddress()].getName() + "]");
        }

        // control GPIO pin
        com.pi4j.wiringpi.Gpio.digitalWrite(pin.getAddress(), state.getValue());

        // for digital output pins, we will echo the event feedback
        dispatchPinDigitalStateChangeEvent(pin, state);

        // for the Raspberry pi, we will not cache pin state since we never use the cache to get state.
    }

    @Override
    public PinState getState(Pin pin) {
        super.getState(pin);

        // return pin state
        PinState state = null;
        int ret = com.pi4j.wiringpi.Gpio.digitalRead(pin.getAddress());
        if (ret >= 0) {
            state = PinState.getState(ret);
        }
        return state;
    }

    @Override
    public void setValue(Pin pin, double value) {
        super.setValue(pin, value);
        throw new RuntimeException("This GPIO provider does not support analog pins.");
    }

    @Override
    public double getValue(Pin pin) {
        super.getValue(pin);
        throw new RuntimeException("This GPIO provider does not support analog pins.");
    }

    @Override
    public void setPwm(Pin pin, int value) {

        // if its not a PWM pin, then superclass implementation will throw an exception
        super.setPwm(pin, value);

        // set PWM to hardware
        setPwmValue(pin, value);
    }

    @Override
    public int getPwm(Pin pin) {
        return super.getPwm(pin);
    }

    // internal
    private void setPwmValue(Pin pin, int value) {
        // set pin PWM value
        com.pi4j.wiringpi.Gpio.pwmWrite(pin.getAddress(), value);
    }

    @Override
    public void pinStateChange(GpioInterruptEvent event) {
        // iterate over the pin listeners map
        for (Pin pin : listeners.keySet()) {
            // dispatch this event to the listener
            // if a matching pin address is found
            if (pin.getAddress() == event.getPin()) {
                dispatchPinDigitalStateChangeEvent(pin, PinState.getState(event.getState()));
            }
        }
    }

    @Override
    public void addListener(Pin pin, PinListener listener) {
        super.addListener(pin, listener);

        // update the native interrupt listener thread for callbacks
        updateInterruptListener(pin);
    }

    @Override
    public void removeListener(Pin pin, PinListener listener) {
        super.removeListener(pin, listener);

        // update the native interrupt listener thread for callbacks
        updateInterruptListener(pin);
    }

    // internal
    private void updateInterruptListener(Pin pin) {
        if (listeners.size() > 0) {
            // setup interrupt listener native thread and enable callbacks
            if (!com.pi4j.wiringpi.GpioInterrupt.hasListener(this)) {
                com.pi4j.wiringpi.GpioInterrupt.addListener(this);
            }
            com.pi4j.wiringpi.GpioInterrupt.enablePinStateChangeCallback(pin.getAddress());
        } else {
            // remove interrupt listener, disable native thread and callbacks
            com.pi4j.wiringpi.GpioInterrupt.disablePinStateChangeCallback(pin.getAddress());
            if (com.pi4j.wiringpi.GpioInterrupt.hasListener(this)) {
                com.pi4j.wiringpi.GpioInterrupt.removeListener(this);
            }
        }
    }
}
