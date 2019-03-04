package com.pi4j.io.gpio.trigger;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPulseStateTrigger.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
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

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import java.util.List;

@SuppressWarnings("unused")
public class GpioPulseStateTrigger extends OutputTargetedGpioTrigger {

	private final long milliseconds;

	public GpioPulseStateTrigger(GpioPinDigitalOutput targetPin, long milliseconds) {
		super(targetPin);
		this.milliseconds = milliseconds;
	}

	public GpioPulseStateTrigger(PinState state, GpioPinDigitalOutput targetPin, long milliseconds) {
		super(state, targetPin);
		this.milliseconds = milliseconds;
	}

	public GpioPulseStateTrigger(PinState[] states, GpioPinDigitalOutput targetPin, long milliseconds) {
		super(states, targetPin);
		this.milliseconds = milliseconds;
	}

	public GpioPulseStateTrigger(List<PinState> states, GpioPinDigitalOutput targetPin, long milliseconds) {
		super(states, targetPin);
		this.milliseconds = milliseconds;
	}

	@Override
	public void invoke(GpioPin pin, PinState state) {
		if (targetPin != null) {
			targetPin.pulse(milliseconds);
		}
	}
}
