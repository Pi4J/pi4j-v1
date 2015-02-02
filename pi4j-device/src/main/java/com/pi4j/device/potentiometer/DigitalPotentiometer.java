package com.pi4j.device.potentiometer;

import java.io.IOException;

import com.pi4j.device.Device;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Digital Potentiometer Device Abstractions
 * FILENAME      :  DigitalPotentiometer.java  
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

/**
 * A digital potentiometer device
 */
public interface DigitalPotentiometer extends Device {

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
