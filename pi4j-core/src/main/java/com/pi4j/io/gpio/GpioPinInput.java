package com.pi4j.io.gpio;

import com.pi4j.io.gpio.trigger.GpioTrigger;

import java.util.Collection;
import java.util.List;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinInput.java  
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
 * Gpio input pin interface. This interface is extension of {@link com.pi4j.io.gpio.GpioPin} interface
 * with listeners and triggers support..
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public interface GpioPinInput extends GpioPin {

    Collection<GpioTrigger> getTriggers();
    void addTrigger(GpioTrigger... trigger);
    void addTrigger(List<? extends GpioTrigger> triggers);

    void removeTrigger(GpioTrigger... trigger);
    void removeTrigger(List<? extends GpioTrigger> triggers);
    void removeAllTriggers();

}
