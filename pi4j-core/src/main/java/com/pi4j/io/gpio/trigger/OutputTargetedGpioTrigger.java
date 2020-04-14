package com.pi4j.io.gpio.trigger;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  OutputTargetedGpioTrigger.java
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

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import java.util.List;

@SuppressWarnings("unused")
public abstract class OutputTargetedGpioTrigger extends GpioTriggerBase {

	protected final GpioPinDigitalOutput targetPin;

	public OutputTargetedGpioTrigger(GpioPinDigitalOutput targetPin) {
		super();
		this.targetPin = targetPin;
	}

	public OutputTargetedGpioTrigger(PinState state, GpioPinDigitalOutput targetPin) {
		super(state);
		this.targetPin = targetPin;
	}

	public OutputTargetedGpioTrigger(PinState[] states, GpioPinDigitalOutput targetPin) {
		super(states);
		this.targetPin = targetPin;
	}

	public OutputTargetedGpioTrigger(List<PinState> states, GpioPinDigitalOutput targetPin) {
		super(states);
		this.targetPin = targetPin;
	}

	public GpioPinDigitalOutput getTargetPin() {
		return targetPin;
	}
}
