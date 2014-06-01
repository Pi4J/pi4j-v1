package com.pi4j.io.gpio.trigger;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioInverseSyncStateTrigger.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
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

import java.util.List;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

public class GpioInverseSyncStateTrigger extends OutputTargetedGpioTrigger {

	public GpioInverseSyncStateTrigger(GpioPinDigitalOutput targetPin) {
		super(targetPin);
	}

	public GpioInverseSyncStateTrigger(PinState state, GpioPinDigitalOutput targetPin) {
		super(state, targetPin);
	}

	public GpioInverseSyncStateTrigger(PinState[] states, GpioPinDigitalOutput targetPin) {
		super(states, targetPin);
	}

	public GpioInverseSyncStateTrigger(List<PinState> states, GpioPinDigitalOutput targetPin) {
		super(states, targetPin);
	}

	@Override
	public void invoke(GpioPin pin, PinState state) {
		if (targetPin != null) {
			if (state == PinState.HIGH) {
				targetPin.setState(PinState.LOW);
			} else {
				targetPin.setState(PinState.HIGH);
			}
		}
	}
}
