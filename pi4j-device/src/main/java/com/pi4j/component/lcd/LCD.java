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
 * Copyright (C) 2012 - 2016 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
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
