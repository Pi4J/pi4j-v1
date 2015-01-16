package com.pi4j.component.lcd;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  LCD.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.pi4j.component.Component;

public interface LCD extends Component {
 
    int getRowCount();
    int getColumnCount();

    void clear();
    void clear(int row);
    void clear(int row, int column, int length);
    
    void setCursorHome();
    void setCursorPosition(int row);
    void setCursorPosition(int row, int column);
    
    void write(String data);
    void write(String data, Object...arguments);
    void write(char[] data);
    void write(byte[] data);
    void write(char data);
    void write(byte data);

    void write(int row, String data, LCDTextAlignment alignment);
    void write(int row, String data, LCDTextAlignment alignment, Object...arguments);
    void write(int row, String data);
    void write(int row, String data, Object...arguments);
    void write(int row, char[] data);
    void write(int row, byte[] data);
    void write(int row, char data);
    void write(int row, byte data);
    
    void write(int row, int column, String data);
    void write(int row, int column, String data, Object...arguments);
    void write(int row, int column, char[] data);
    void write(int row, int column, byte[] data);
    void write(int row, int column, char data);
    void write(int row, int column, byte data);
    
    void writeln(int row, String data);
    void writeln(int row, String data, Object...arguments);
    void writeln(int row, String data, LCDTextAlignment alignment);
    void writeln(int row, String data, LCDTextAlignment alignment, Object...arguments);    
}
