package com.pi4j.component.motor;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  EasyDriver.java
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

import com.pi4j.io.gpio.*;

/**
 * Created by Jiamin on 12/11/2015.
 */
/*
        Pin definition copied from Easy Driver's official website

        A and B : (four pins) These are the motor connections. See below diagrams for how to hook these up. A and B are the two coils of the motor, and can swap the two wires for a given coil (it will just reverse the direction of the motor). Make CERTAIN that this connection to the motor is solid, and NOT through a connector that has any chance of intermittent contact (which will fry the motor driver chip).
        STEP : This needs to be a 0V to 5V (or 0V to 3.3V if you've set your Easy Driver that way) digital signal. Each rising edge of this signal will cause one step (or microstep) to be taken.
        DIR (Direction) : This needs to be a 0V to 5V (or 0V to 3.3V if you've set your Easy Driver up that way) digital signal. The level if this signal (high/low) is sampled on each rising edge of STEP to determine which direction to take the step (or microstep).

        MS1/MS2 : These digital inputs control the microstepping mode. Possible settings are (MS1/MS2) :
            full step (0,0), half step (1,0), 1/4 step (0,1), and 1/8 step (1,1 : default).
        RST (reset) : This normally high input signal will reset the internal translator and disable all output drivers when pulled low.
        SLP (sleep) : This normally high input signal will minimize power consumption by disabling internal circuitry and the output drivers when pulled low.
        ENABLE : This normally low input signal will disable all outputs when pulled high.
        PFD : This one is complicated - please see the datasheet for more information. We default it to slow decay mode, but you can over-ride with your own voltage on this pin. (or by populating R17)
        5V : This is an OUTPUT pin that will provide either 5V (default) or 3.3V from the voltage regulator, at a small amount of current (say 50mA - depends on input voltage) to power a circuit that you may need powered. If you cut jumper APWR (SJ1) then you can use the 5V pin as a VCC input to the Easy Driver, powering it with your own VCC supply.
*/

public class EasyDriver {

    public static final int FORWARD = 0;
    public static final int BACKWARD = 1;
    public static final int FULL_STEP = 0;
    public static final int HALF_STEP = 1;
    public static final int ONE_FOURTH_STEP = 2;
    public static final int ONE_EIGHTH_STEP = 3;

    private int mDrivingMode;
    private GpioPinDigitalOutput mStepPin;
    private GpioPinDigitalOutput mDirPin;
    private GpioPinDigitalOutput mSleepPin;
    private GpioPinDigitalOutput mEnablePin;
    private GpioPinDigitalOutput mMs1Pin;
    private GpioPinDigitalOutput mMs2Pin;
    private GpioPinDigitalOutput mResetPin;

    public EasyDriver(int drivingMode, Pin stepPin, Pin dirPin, Pin sleepPin,
                      Pin enablePin, Pin ms1Pin, Pin ms2Pin, Pin resetPin) {
        mDrivingMode = drivingMode;

        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #01 as an output pin and turn on
        mStepPin = gpio.provisionDigitalOutputPin(stepPin, "Step Pin", PinState.LOW);
        mDirPin = gpio.provisionDigitalOutputPin(dirPin, "Direction Pin", PinState.LOW);
        mSleepPin = gpio.provisionDigitalOutputPin(sleepPin, "Sleep Pin", PinState.HIGH);
        mEnablePin = gpio.provisionDigitalOutputPin(enablePin, "Enable Pin", PinState.LOW);
        mMs1Pin = gpio.provisionDigitalOutputPin(ms1Pin, "MS1 Pin", PinState.HIGH);
        mMs2Pin = gpio.provisionDigitalOutputPin(ms2Pin, "MS2 Pin", PinState.HIGH);
        mResetPin = gpio.provisionDigitalOutputPin(resetPin, "Reset Pin", PinState.HIGH);

        setDrivingMode(drivingMode);


        mStepPin.setShutdownOptions(true, PinState.LOW);
        mDirPin.setShutdownOptions(true, PinState.LOW);
        mSleepPin.setShutdownOptions(true, PinState.HIGH);
        mEnablePin.setShutdownOptions(true, PinState.LOW);
        mMs1Pin.setShutdownOptions(true, PinState.HIGH);
        mMs2Pin.setShutdownOptions(true, PinState.HIGH);
        mResetPin.setShutdownOptions(true, PinState.HIGH);
    }

    public EasyDriver(Pin stepPin, Pin dirPin, Pin sleepPin,
                      Pin enablePin, Pin ms1Pin, Pin ms2Pin, Pin resetPin) {
        new EasyDriver(ONE_EIGHTH_STEP, stepPin, dirPin, sleepPin, enablePin, ms1Pin, ms2Pin, resetPin);
    }

    public EasyDriver(Pin stepPin) {
        mDrivingMode = ONE_EIGHTH_STEP;

        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #01 as an output pin and turn on
        mStepPin = gpio.provisionDigitalOutputPin(stepPin, "MyLED", PinState.LOW);
    }

    public void setDrivingMode(int drivingMode) {
        switch (drivingMode) {
            case FULL_STEP: {
                mMs1Pin.low();
                mMs2Pin.low();
                break;
            }
            case HALF_STEP: {
                mMs1Pin.high();
                mMs2Pin.low();
                break;
            }
            case ONE_FOURTH_STEP: {
                mMs1Pin.low();
                mMs2Pin.high();
                break;
            }
            case ONE_EIGHTH_STEP: {
                mMs1Pin.high();
                mMs2Pin.high();
                break;
            }
        }
    }

    public void rotate(double degrees, int interval, int drivingMode) throws InterruptedException {
        move(getStepsFromDegrees(degrees, drivingMode), interval, drivingMode);
    }

    public void rotate(double degrees, int interval) throws InterruptedException {
        rotate(degrees, interval, mDrivingMode);
    }

    public void move(int distance, int interval, int drivingMode) throws InterruptedException {
        if (drivingMode != mDrivingMode) {
            setDrivingMode(drivingMode);
        }

        if (distance < 0) {
            setDirection(BACKWARD);
        } else {
            setDirection(FORWARD);
        }

        for (int i = 0; i < Math.abs(distance); i++) {
            mStepPin.high();
            Thread.sleep(interval);
            mStepPin.low();
            Thread.sleep(interval);
        }
    }

    public void move(int steps, int interval) throws InterruptedException {
        move(steps, interval, mDrivingMode);
    }

    public void sleep() {
        mSleepPin.low();
    }

    public void wake() {
        mSleepPin.high();
    }

    public void reset() {
        mResetPin.low();
    }

    public void enable() {
        mEnablePin.low();
    }

    public void disable() {
        mEnablePin.high();
    }

    public void shutdown() {
        final GpioController gpio = GpioFactory.getInstance();
        gpio.shutdown();
    }

    public void setDirection(int direction) {
        if (direction == FORWARD) {
            mDirPin.low();
        } else if (direction == BACKWARD) {
            mDirPin.high();
        }
    }

    public static double getDegreesFromStep(int steps, int drivingMode) {
        switch (drivingMode) {
            case FULL_STEP:
                return steps * 1.8;
            case HALF_STEP:
                return steps * 0.9;
            case ONE_FOURTH_STEP:
                return steps * 0.45;
            case ONE_EIGHTH_STEP:
                return steps * 0.225;
            default:
                return 0.0;
        }
    }

    public static int getStepsFromDegrees(double degrees, int drivingMode) {
        switch (drivingMode) {
            case FULL_STEP:
                return (int) (degrees / 1.8);
            case HALF_STEP:
                return (int) (degrees / 0.9);
            case ONE_FOURTH_STEP:
                return (int) (degrees / 0.45);
            case ONE_EIGHTH_STEP:
                return (int) (degrees / 0.225);
            default:
                return 0;
        }
    }
}
