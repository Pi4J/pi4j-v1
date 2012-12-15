package com.pi4j.device.access;

import com.pi4j.component.Component;

public interface Opener extends Component {
    
    void open();
    void close();
    boolean isOpen();
    boolean isOpening();
    boolean isClosed();
    boolean isClosing();
    OpenerState getState();
    boolean isLocked();
}
