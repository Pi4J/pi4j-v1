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
 * Copyright (C) 2012 - 2016 Pi4J
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
