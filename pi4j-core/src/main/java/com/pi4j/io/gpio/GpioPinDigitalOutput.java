package com.pi4j.io.gpio;

import java.util.concurrent.Future;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPinDigitalOutput.java  
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
 * Gpio digital output pin interface.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public interface GpioPinDigitalOutput extends GpioPinDigital, GpioPinOutput {

    void high();
    void low();    
    void toggle();
    Future<?> blink(long delay);
    Future<?> blink(long delay, PinState blinkState);
    Future<?> blink(long delay, long duration);
    Future<?> blink(long delay, long duration, PinState blinkState);
    Future<?> pulse(long duration);    
    Future<?> pulse(long duration, boolean blocking);    
    Future<?> pulse(long duration, PinState pulseState);
    Future<?> pulse(long duration, PinState pulseState, boolean blocking);
    void setState(PinState state);
    void setState(boolean state);

}
