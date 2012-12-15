package com.pi4j.component.light;

import com.pi4j.component.Component;

public interface Light extends Component {
    
    void on();
    void off();
    boolean isOn();
    boolean isOff();
}
