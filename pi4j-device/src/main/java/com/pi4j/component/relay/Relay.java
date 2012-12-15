package com.pi4j.component.relay;

import com.pi4j.component.Component;

public interface Relay extends Component {
    
    void open();
    void close();
    boolean isOpen();
    boolean isClosed();
    RelayState getState();
    void setState(RelayState state);
    
}
