package com.pi4j.component.light.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  GpioDimmableLightComponent.java  
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


import com.pi4j.component.light.DimmableLightBase;
import com.pi4j.component.light.LightLevelChangeEvent;
import com.pi4j.component.light.LightStateChangeEvent;
import com.pi4j.io.gpio.GpioPinPwmOutput;

public class GpioDimmableLightComponent extends DimmableLightBase {
    
    // internal class members
    GpioPinPwmOutput pin = null;
    int min = 0;
    int max = 0;
    
    /**
     * default constructor  
     *  
     * @param pin GPIO digital output pin
     * @param min PWM pin value to set when LIGHT is fully ON
     * @param max PWM pin value to set when LIGHT is fully OFF
     */
    public GpioDimmableLightComponent(GpioPinPwmOutput pin, int min, int max) {
        this.pin = pin;
        this.min = min;
        this.max = max;
    }

    @Override
    public int getMinLevel()
    {
        return min; 
    }

    @Override
    public int getMaxLevel()
    {
        return max;
    }

    @Override
    public int getLevel()
    {
        return pin.getPwm();
    }

    @Override
    public void setLevel(int level)
    {
        boolean isOnBeforeChange = isOn();

        // turn the light fully OFF by setting the PWM GPIO to min value
        pin.setPwm(level);
        
        boolean isOnAfterChange = isOn();

        // notify any power state change listeners
        notifyListeners(new LightLevelChangeEvent(this, level));
        if(isOnBeforeChange != isOnAfterChange)
            notifyListeners(new LightStateChangeEvent(this, isOnAfterChange));
    }
}
