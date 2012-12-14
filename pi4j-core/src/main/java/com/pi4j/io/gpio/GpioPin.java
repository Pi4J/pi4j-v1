package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioPin.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
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


import java.util.Map;

/**
 * Gpio pin interface. This interface describes all operations over single GPIO pin.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public interface GpioPin {

    GpioProvider getProvider();
    Pin getPin();
    
    void setName(String name);
    String getName();

    void setProperty(String key, String value);
    boolean hasProperty(String key);
    String getProperty(String key);
    Map<String,String> getProperties();
    void removeProperty(String key);
    void clearProperties();
    
    void export(PinMode mode);
    void unexport();
    boolean isExported();
    
    void setMode(PinMode mode);
    PinMode getMode();
    boolean isMode(PinMode mode);
    
    void setPullResistance(PinPullResistance resistance);
    PinPullResistance getPullResistance();
    boolean isPullResistance(PinPullResistance resistance);

    GpioPinShutdown getShutdownOptions();
    void setShutdownOptions(GpioPinShutdown options);
    void setShutdownOptions(Boolean unexport);
    void setShutdownOptions(Boolean unexport, PinState state);
    void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance);
    void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance, PinMode mode);
}
