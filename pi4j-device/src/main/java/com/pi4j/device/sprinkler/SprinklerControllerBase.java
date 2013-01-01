package com.pi4j.device.sprinkler;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  SprinklerControllerBase.java  
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


import java.util.ArrayList;
import java.util.List;

import com.pi4j.device.DeviceBase;

public abstract class SprinklerControllerBase extends DeviceBase implements SprinklerController {

    protected List<SprinklerZone> zones = new ArrayList<SprinklerZone>();
    
    @Override
    public int getZoneCount() {
        return zones.size();
    }

    @Override
    public List<SprinklerZone> getZones() {
        return zones;
    }

    @Override
    public boolean isOn() {
        for(SprinklerZone zone : zones)
            if(zone.isOn())
                return true;
        return false;
    }

    @Override
    public boolean isOff() {
        return !(isOn());
    }

    @Override
    public boolean isOn(int zone)
    {
        return zones.get(zone).isOn();
    }

    @Override
    public boolean isOff(int zone)
    {
        return zones.get(zone).isOff();
    }

    @Override
    public void on(int zone)
    {
        zones.get(zone).on();
    }

    @Override
    public void onAllZones()
    {
        for(SprinklerZone zone : zones)
            zone.on();
    }

    @Override
    public void off(int zone)
    {
        zones.get(zone).off();
    }

    @Override
    public void offAllZones()
    {
        for(SprinklerZone zone : zones)
            zone.off();
    }
    
    @Override
    public void setState(int zone, boolean on)
    {
        zones.get(zone).setState(on);
    }

    @Override
    public abstract boolean isRaining();
}
