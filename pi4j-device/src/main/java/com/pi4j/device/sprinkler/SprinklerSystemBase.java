package com.pi4j.device.sprinkler;

import java.util.ArrayList;
import java.util.List;

import com.pi4j.device.DeviceBase;

public abstract class SprinklerSystemBase extends DeviceBase implements SprinklerSystem {

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
