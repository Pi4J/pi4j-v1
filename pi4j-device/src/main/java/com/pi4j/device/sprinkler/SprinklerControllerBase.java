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
