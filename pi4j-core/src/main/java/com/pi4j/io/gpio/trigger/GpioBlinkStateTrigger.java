package com.pi4j.io.gpio.trigger;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioBlinkStateTrigger.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import java.util.List;

@SuppressWarnings("unused")
public class GpioBlinkStateTrigger extends OutputTargetedGpioTrigger {

	private final long milliseconds;

	public GpioBlinkStateTrigger(GpioPinDigitalOutput targetPin, long milliseconds) {
		super(targetPin);
		this.milliseconds = milliseconds;
	}

	public GpioBlinkStateTrigger(PinState state, GpioPinDigitalOutput targetPin, long milliseconds) {
		super(state, targetPin);
		this.milliseconds = milliseconds;
	}

	public GpioBlinkStateTrigger(PinState[] states, GpioPinDigitalOutput targetPin, long milliseconds) {
		super(states, targetPin);
		this.milliseconds = milliseconds;
	}

	public GpioBlinkStateTrigger(List<PinState> states, GpioPinDigitalOutput targetPin, long milliseconds) {
		super(states, targetPin);
		this.milliseconds = milliseconds;
	}

	@Override
	public void invoke(GpioPin pin, PinState state) {
		if (targetPin != null) {
			targetPin.blink(milliseconds);
		}
	}
}
