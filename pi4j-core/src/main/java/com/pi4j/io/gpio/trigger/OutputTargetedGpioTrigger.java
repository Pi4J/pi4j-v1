package com.pi4j.io.gpio.trigger;

import java.util.List;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

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
