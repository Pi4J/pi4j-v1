package com.pi4j.component.temperature;

import com.pi4j.component.Component;
import com.pi4j.temperature.TemperatureScale;

public interface TemperatureSensor extends Component {

    double getTemperature();
    double getTemperature(TemperatureScale scale);
    TemperatureScale getScale();
    
}
