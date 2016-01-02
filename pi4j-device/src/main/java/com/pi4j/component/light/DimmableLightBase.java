package com.pi4j.component.light;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  DimmableLightBase.java  
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

import com.pi4j.component.ComponentListener;
import com.pi4j.component.ObserveableComponentBase;

public abstract class DimmableLightBase extends ObserveableComponentBase implements Light, DimmableLight {

    @Override
    public abstract int getMinLevel();

    @Override
    public abstract int getMaxLevel();

    @Override
    public abstract int getLevel();

    @Override
    public abstract void setLevel(int level);

    @Override
    public float getLevelPercentage()
    {
        return getLevelPercentage(getLevel());
    }

    @Override
    public float getLevelPercentage(int level)
    {
        int min = Math.min(getMinLevel(),getMaxLevel());
        int max = Math.max(getMinLevel(),getMaxLevel());
        int range = max - min;
        float percentage = ((level * 100) / range);  
        return percentage;
    }
    
    @Override
    public void on() {
        setLevel(getMaxLevel()); 
    }

    @Override
    public void off() {
        setLevel(getMinLevel());
    }

    @Override
    public boolean isOn() {
        return (getLevel() > getMinLevel());
    }

    @Override
    public boolean isOff() {
        return getLevel() <= getMinLevel();
    }
 
    @Override
    public void addListener(LightListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(LightListener... listener) {
        super.removeListener(listener);
    }

    @Override
    public void addListener(DimmableLightListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(DimmableLightListener... listener) {
        super.removeListener(listener);
    }

    protected synchronized void notifyListeners(LightStateChangeEvent event) {
        for(ComponentListener listener : super.listeners) {
            ((LightListener)listener).onStateChange(event);
        }
    }
    
    protected synchronized void notifyListeners(LightLevelChangeEvent event) {
        for(ComponentListener listener : super.listeners) {
            ((DimmableLightListener)listener).onLevelChange(event);
        }
    }       
}
