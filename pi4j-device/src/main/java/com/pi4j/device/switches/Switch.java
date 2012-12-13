package com.pi4j.device.switches;

import com.pi4j.device.Pi4JDevice;

public interface Switch extends Pi4JDevice
{
    boolean isOn();
    boolean isOff();
    SwitchState getState();
}
