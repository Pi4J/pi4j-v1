package com.pi4j.device.sprinkler;

import java.util.List;

public interface SprinklerSystem {

    int getZoneCount();
    List<SprinklerZone> getZones();

    boolean isOn();
    boolean isOff();
    boolean isOn(int zone);
    boolean isOff(int zone);
    
    void on(int zone);
    void onAllZones();
    void off(int zone);
    void offAllZones();
    void setState(int zone, boolean on);
    
    boolean isRaining();
}
