package com.pi4j.component.lcd;

import com.pi4j.component.Component;

public interface LCD extends Component {
 
    int getLineCount();
    void clear();
    void clearLine(int line);
    void setLine(int line, String data);
    void setLine(int line, char[] data);
    void setLine(int line, byte[] data);
}
