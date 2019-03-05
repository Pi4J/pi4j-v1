package com.pi4j.io.gpio;

import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.gpio.exception.InvalidPinException;
import com.pi4j.io.gpio.exception.InvalidPinModeException;
import com.pi4j.io.gpio.exception.UnsupportedPinModeException;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioInterruptEvent;
import com.pi4j.wiringpi.GpioInterruptListener;
import com.pi4j.wiringpi.GpioUtil;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  WiringPiGpioProviderBase.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * WiringPiGpioProviderBase {@link GpioProvider} implementation.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public abstract class WiringPiGpioProviderBase extends GpioProviderBase implements GpioProvider, GpioInterruptListener {

    // the pin cache should support the maximum number of pins supported by wiringPi plus some
    // additional overhead for virtual analog input pins used by providers
    protected static short MAX_PIN_CACHE = Gpio.NUM_PINS + 5;

    // need enough space in array for maximum number of pins.
    // Currently the Computer module supports the highest number of pins.
    protected static short pinSupportedCache[] = new short[MAX_PIN_CACHE];
    protected static PinMode pinModeCache[] = new PinMode[MAX_PIN_CACHE];

    public abstract String getName();


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
            if(GpioUtil.isPinSupported(pin.getAddress()) > 0) {
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
        if(!GpioUtil.isExported(pin.getAddress())){
            GpioUtil.export(pin.getAddress(), direction);
        }
        // if the pin is already exported, then check its current configured direction
        // if the direction does not match, then set the new direction for the pin
        else if(GpioUtil.getDirection(pin.getAddress()) != mode.getDirection().getValue()){
            GpioUtil.setDirection(pin.getAddress(), direction);
        }

        // set the pin input/output mode
        setMode(pin, mode);
    }

    @Override
    public boolean isExported(Pin pin) {
        super.isExported(pin);

        // return the pin exported state
        return GpioUtil.isExported(pin.getAddress());
    }

    @Override
    public void unexport(Pin pin) {
        super.unexport(pin);

        // unexport the pins
        GpioUtil.unexport(pin.getAddress());
    }

    @Override
    public void setMode(Pin pin, PinMode mode) {
        super.setMode(pin, mode);

        // local pin mode cache
        pinModeCache[pin.getAddress()] = mode;

        if (!pin.getSupportedPinModes().contains(mode)) {
            throw new InvalidPinModeException(pin, "Invalid pin mode [" + mode.getName() + "]; pin [" + pin.getName() + "] does not support this mode.");
        }

        if (!pin.getSupportedPinModes().contains(mode)) {
            throw new UnsupportedPinModeException(pin, mode);
        }

        // cache mode
        getPinCache(pin).setMode(mode);

        // set pin mode on hardware
        com.pi4j.wiringpi.Gpio.pinMode(pin.getAddress(), mode.getValue());
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
        // validate pin
        if (!hasPin(pin)) {
            throw new InvalidPinException(pin);
        }

        // get pin configured mode
        PinMode mode = getMode(pin);

        // validate mode; set PWM value based on pin mode
        if (mode == PinMode.PWM_OUTPUT) {
            // set pin hardware PWM value
            com.pi4j.wiringpi.Gpio.pwmWrite(pin.getAddress(), value);
        }
        else if(mode == PinMode.SOFT_PWM_OUTPUT) {
            // set pin software emulated PWM value
            com.pi4j.wiringpi.SoftPwm.softPwmWrite(pin.getAddress(), value);
        }
        else {
            // unsupported pin mode
            throw new InvalidPinModeException(pin, "Invalid pin mode [" + mode.getName() + "]; unable to setPwm(" + value + ")");
        }

        // cache updated pin PWM value
        getPinCache(pin).setPwmValue(value);
    }

    @Override
    public void setPwmRange(Pin pin, int range){
        // validate pin
        if (!hasPin(pin)) {
            throw new InvalidPinException(pin);
        }

        // get pin configured mode
        PinMode mode = getMode(pin);

        // validate mode; set PWM value based on pin mode
        if (mode == PinMode.PWM_OUTPUT) {
            // set pin hardware PWM value
            com.pi4j.wiringpi.Gpio.pwmSetRange(range);
        }
        else if(mode == PinMode.SOFT_PWM_OUTPUT) {
            // first, stop the software emulated PWM driver for this pin
            com.pi4j.wiringpi.SoftPwm.softPwmStop(pin.getAddress());
            // update the PWM range for this pin
            com.pi4j.wiringpi.SoftPwm.softPwmCreate(pin.getAddress(), 0, range);
        }
        else {
            // unsupported operation
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public int getPwm(Pin pin) {
        return super.getPwm(pin);
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
    protected void updateInterruptListener(Pin pin) {
        // enable or disable single static listener with the native impl
        if (listeners.size() > 0) {
            // setup interrupt listener native thread and enable callbacks
            if (!com.pi4j.wiringpi.GpioInterrupt.hasListener(this)) {
                com.pi4j.wiringpi.GpioInterrupt.addListener(this);
            }

            // only configure WiringPi interrupts for digital input pins
            if(pinModeCache[pin.getAddress()] == PinMode.DIGITAL_INPUT) {
                // enable or disable the individual pin listener
                if(listeners.containsKey(pin) && listeners.get(pin).size() > 0) {
                    // enable interrupt listener for this pin
                    com.pi4j.wiringpi.GpioInterrupt.enablePinStateChangeCallback(pin.getAddress());
                }
                else {
                    // disable interrupt listener for this pin
                    com.pi4j.wiringpi.GpioInterrupt.disablePinStateChangeCallback(pin.getAddress());
                }
            }
        }
        else {
            // disable interrupt listener for this pins
            com.pi4j.wiringpi.GpioInterrupt.disablePinStateChangeCallback(pin.getAddress());

            // remove interrupt listener, disable native thread and callbacks
            if (com.pi4j.wiringpi.GpioInterrupt.hasListener(this)) {
                com.pi4j.wiringpi.GpioInterrupt.removeListener(this);
            }
        }
    }
}
