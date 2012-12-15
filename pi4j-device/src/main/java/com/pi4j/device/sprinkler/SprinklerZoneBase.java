package com.pi4j.device.sprinkler;

public abstract class SprinklerZoneBase implements SprinklerZone {

    protected String name = null;
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isOff() {
        return (!isOn());
    }

    @Override
    public void on() {
        setState(true);
    }

    @Override
    public void off() {
        setState(false);
    }

    @Override
    public abstract boolean isOn();
    
    @Override
    public abstract void setState(boolean on);
}
