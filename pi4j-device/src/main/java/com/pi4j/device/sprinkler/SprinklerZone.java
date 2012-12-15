package com.pi4j.device.sprinkler;

public interface SprinklerZone {

    String getName();
    void setName(String name);

    boolean isOn();
    boolean isOff();
    
    void on();
    void off();
    void setState(boolean on);
}
