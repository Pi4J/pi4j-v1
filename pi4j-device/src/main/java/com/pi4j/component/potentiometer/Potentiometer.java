package com.pi4j.component.potentiometer;

import com.pi4j.component.Component;

import java.io.IOException;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  Potentiometer.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2017 Pi4J
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
 * A digital potentiometer component
 */
public interface Potentiometer extends Component {

	/**
	 * @return The maximum wiper-value supported by the device
	 */
	int getMaxValue();

	/**
	 * @return Whether the device is a potentiometer or a rheostat
	 */
	boolean isRheostat();

	/**
	 * Set wiper's value. Values from 0 to 'maxValue' are valid. Values above or
	 * below this boundaries are corrected quietly.
	 *
	 * @param value The wiper's value.
	 * @throws IOException If communication with the device fails
	 * @see #getMaxValue()
	 */
	void setCurrentValue(final int value) throws IOException;

	/**
	 * The implementation should cache to wiper's value and therefore should
	 * avoid accessing the device to often.
	 *
	 * @return The wiper's current value.
	 * @throws IOException If communication with the device fails
	 */
	int getCurrentValue() throws IOException;

	/**
	 * Increase the wiper's value by one step. It is not an error if the wiper
	 * already hit the upper boundary. In this situation the wiper doesn't change.
	 *
	 * @throws IOException If communication with the device fails
	 * @see #getMaxValue()
	 */
	void increase() throws IOException;

	/**
	 * Increase the wiper's value by n steps. It is not an error if the wiper
	 * hits or already hit the upper boundary. In such situations the wiper
	 * sticks to the upper boundary or doesn't change.
	 *
	 * @param steps How many steps to increase.
	 * @throws IOException If communication with the device fails
	 */
	void increase(final int steps) throws IOException;

	/**
	 * Decrease the wiper's value by one step. It is not an error if the wiper
	 * already hit the lower boundary (0). In this situation the wiper doesn't change.
	 *
	 * @throws IOException If communication with the device fails
	 */
	void decrease() throws IOException;

	/**
	 * Decrease the wiper's value by n steps. It is not an error if the wiper
	 * hits or already hit the lower boundary (0). In such situations the wiper
	 * sticks to the lower boundary or doesn't change.
	 *
	 * @param steps How many steps to decrease.
	 * @throws IOException If communication with the device fails
	 */
	void decrease(final int steps) throws IOException;

}
