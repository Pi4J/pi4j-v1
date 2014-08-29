package com.pi4j.io.gpio;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioProviderPinCache.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
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
 * This class provides cache for gpio pin instances.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public class GpioProviderPinCache {

    private final Pin pin;
    private PinMode mode;
    private PinState state;
    private PinPullResistance resistance;
    private double analogValue = -1;
    private int pwmValue = -1;
    private boolean exported = false; 
    
    public GpioProviderPinCache(Pin pin) {
        this.pin = pin;
    }
    
    @Override
    public String toString() {
        return "PIN [" + pin.getName() + "] CACHE :: mode=" + mode.getName() + "; state=" + state.getName();
    }

    public PinMode getMode() {
        return mode;
    }
    
    public void setMode(PinMode mode) {
        this.mode = mode;
    }
    
    public PinState getState() {
        return state;
    }
    
    public void setState(PinState state) {
        this.state = state;
    }
    
    public PinPullResistance getResistance() {
        return resistance;
    }
    
    public void setResistance(PinPullResistance resistance) {
        this.resistance = resistance;
    }
    
    public double getAnalogValue() {
        return analogValue;
    }
    
    public void setAnalogValue(double value) {
        this.analogValue = value;
    }
    
    public int getPwmValue() {
        return pwmValue;
    }
    
    public void setPwmValue(int value) {
        this.pwmValue = value;
    }
    
    public boolean isExported() {
        return exported;
    }
    
    public void setExported(boolean exported) {
        this.exported = exported;
    }
}
