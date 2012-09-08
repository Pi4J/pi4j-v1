/*
 * **********************************************************************
 * This file is part of the pi4j project: http://www.pi4j.com/
 * 
 * pi4j is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * pi4j is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with pi4j. If not,
 * see <http://www.gnu.org/licenses/>.
 * **********************************************************************
 */
package com.pi4j.io.serial;

/**
 * <h1>Serial Interface</h1>
 * 
 * <p>
 * This interface provides a set of functions for 'Serial' communication.
 * </p>
 * 
 * <p>
 * Before using the Pi4J library, you need to ensure that the Java VM in configured with access to
 * the following system libraries:
 * <ul>
 * <li>pi4j</li>
 * <li>wiringPi</li>
 * </ul>
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by
 * Gordon Henderson @ <a href="https://projects.drogon.net/">https://projects.drogon.net/</a>)
 * </blockquote>
 * </p>
 * 
 * @see #SerialFactory
 * @see #SerialDataEvent
 * @see #SerialDataListener
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public interface Serial
{
    /**
     * The default hardware COM port provided via the Raspberry Pi GPIO header.
     * 
     * @see #serialOpen(String,int)
     */
    public static final String DEFAULT_COM_PORT = "/dev/ttyAMA0";
    
    
    int open(String device, int baudRate);    
    
    /**
     * Close the serial port.
     */
    void close();
    
    boolean isOpen();
    void flush();
    char read();
    char readLine();
    void write(char data);
    void write(char data[]);
    void write(byte data);
    void write(byte data[]);
    void write(String data);
    void write(String data, String...args);
    int availableBytes();
    void addListener(SerialDataListener listener);
    void removeListener(SerialDataListener listener);
}
