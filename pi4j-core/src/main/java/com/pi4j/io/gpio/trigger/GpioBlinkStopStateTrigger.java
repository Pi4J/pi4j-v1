package com.pi4j.io.gpio.trigger;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioBlinkStopStateTrigger.java
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
public class GpioBlinkStopStateTrigger extends OutputTargetedGpioTrigger {

	public GpioBlinkStopStateTrigger(GpioPinDigitalOutput targetPin) {
		super(targetPin);
	}

	public GpioBlinkStopStateTrigger(PinState state, GpioPinDigitalOutput targetPin) {
		super(state, targetPin);
	}

	public GpioBlinkStopStateTrigger(GpioPin pin, PinState state, GpioPinDigitalOutput targetPin) {
		super(state, targetPin);
	}

	public GpioBlinkStopStateTrigger(GpioPin pin, PinState[] states, GpioPinDigitalOutput targetPin) {
		super(states, targetPin);
	}

	public GpioBlinkStopStateTrigger(GpioPin pin, List<PinState> states, GpioPinDigitalOutput targetPin) {
		super(states, targetPin);
	}

	public GpioBlinkStopStateTrigger(GpioPin[] pins, PinState[] states, GpioPinDigitalOutput targetPin) {
		super(states, targetPin);
	}

	public GpioBlinkStopStateTrigger(List<GpioPin> pins, List<PinState> states, GpioPinDigitalOutput targetPin) {
		super(states, targetPin);
	}

	@Override
	public void invoke(GpioPin pin, PinState state) {
		if (targetPin != null) {
			targetPin.blink(0);
			targetPin.low();
		}
	}
}
