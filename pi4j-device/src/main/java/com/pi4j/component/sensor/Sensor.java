package com.pi4j.component.sensor;

import com.pi4j.component.Component;

public interface Sensor extends Component {
    
    boolean isOpen();
    boolean isClosed();
    SensorState getState();
    
}
