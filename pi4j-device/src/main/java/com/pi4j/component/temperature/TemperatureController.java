package com.pi4j.component.temperature;

import com.pi4j.temperature.TemperatureScale;


public interface TemperatureController extends TemperatureSensor {

    void setTemperature(double temperature);
    void setTemperature(double temperature, TemperatureScale scale);

}
