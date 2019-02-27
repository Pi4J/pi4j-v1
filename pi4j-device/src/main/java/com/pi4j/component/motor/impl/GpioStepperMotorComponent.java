package com.pi4j.component.motor.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  GpioStepperMotorComponent.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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

import com.pi4j.component.motor.MotorState;
import com.pi4j.component.motor.StepperMotorBase;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

public class GpioStepperMotorComponent extends StepperMotorBase {

    // internal class members
    private GpioPinDigitalOutput pins[];
    private PinState onState = PinState.HIGH;
    private PinState offState = PinState.LOW;
    private MotorState currentState = MotorState.STOP;
    private GpioStepperMotorControl controlThread = new GpioStepperMotorControl();
    private int sequenceIndex = 0;

    /**
     * using this constructor requires that the consumer
     *  define the STEP ON and STEP OFF pin states
     *
     * @param pins GPIO digital output pins for each controller in the stepper motor
     * @param onState pin state to set when MOTOR STEP is ON
     * @param offState pin state to set when MOTOR STEP is OFF
     */
    public GpioStepperMotorComponent(GpioPinDigitalOutput pins[], PinState onState, PinState offState) {
        this.pins = pins;
        this.onState = onState;
        this.offState = offState;
    }

    /**
     * default constructor; using this constructor assumes that:
     *  (1) a pin state of HIGH is MOTOR STEP ON
     *  (2) a pin state of LOW  is MOTOR STEP OFF
     *
     * @param pins GPIO digital output pins for each controller in the stepper motor
     */
    public GpioStepperMotorComponent(GpioPinDigitalOutput pins[]) {
        this.pins = pins;
    }

    /**
     * Return the current motor state
     *
     * @return MotorState
     */
    @Override
    public MotorState getState() {
        return currentState;
    }

    /**
     * change the current stepper motor state
     *
     * @param state new motor state to apply
     */
    @Override
    public void setState(MotorState state) {

        switch(state) {
            case STOP: {
                // set internal tracking state
                currentState = MotorState.STOP;

                // turn all GPIO pins to OFF state
                for(GpioPinDigitalOutput pin : pins)
                    pin.setState(offState);

                break;
            }
            case FORWARD: {
                // set internal tracking state
                currentState = MotorState.FORWARD;

                // start control thread if not already running
                if(!controlThread.isAlive()) {
                    controlThread = new GpioStepperMotorControl();
                    controlThread.start();
                }

                break;
            }
            case REVERSE: {
                // set internal tracking state
                currentState = MotorState.REVERSE;

                // start control thread if not already running
                if(!controlThread.isAlive()) {
                    controlThread = new GpioStepperMotorControl();
                    controlThread.start();
                }

                break;
            }
            default: {
                throw new UnsupportedOperationException("Cannot set motor state: " + state.toString());
            }
        }
    }

    private class GpioStepperMotorControl extends Thread {
        public void run() {

            // continuous loop until stopped
            while(currentState != MotorState.STOP) {

                // control direction
                if(currentState == MotorState.FORWARD)
                    doStep(true);
                else if(currentState == MotorState.REVERSE)
                    doStep(false);
            }

            // turn all GPIO pins to OFF state
            for(GpioPinDigitalOutput pin : pins)
                pin.setState(offState);
        }
    }

    @Override
    public void step(long steps)
    {
        // validate parameters
        if (steps == 0) {
            setState(MotorState.STOP);
            return;
        }

        // perform step in positive or negative direction from current position
        if (steps > 0){
            for(long index = 1; index <= steps; index++)
                doStep(true);
        }
        else {
            for(long index = steps; index < 0; index++)
                doStep(false);
        }

        // stop motor movement
        this.stop();
    }

    /**
     * this method performs the calculations and work to control the GPIO pins
     * to move the stepper motor forward or reverse
     * @param forward
     */
    private void doStep(boolean forward) {

        // increment or decrement sequence
        if(forward)
            sequenceIndex++;
        else
            sequenceIndex--;

        // check sequence bounds; rollover if needed
        if(sequenceIndex >= stepSequence.length)
            sequenceIndex = 0;
        else if(sequenceIndex < 0)
            sequenceIndex = (stepSequence.length - 1);

        // start cycling GPIO pins to move the motor forward or reverse
        for(int pinIndex = 0; pinIndex < pins.length; pinIndex++) {
            // apply step sequence
            double nib = Math.pow(2, pinIndex);
            if((stepSequence[sequenceIndex] & (int)nib) > 0)
                pins[pinIndex].setState(onState);
            else
                pins[pinIndex].setState(offState);
        }
        try {
            Thread.sleep(stepIntervalMilliseconds, stepIntervalNanoseconds);
        }
        catch (InterruptedException e) {}
    }
}
