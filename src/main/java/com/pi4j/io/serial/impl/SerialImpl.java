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
package com.pi4j.io.serial.impl;

import java.util.Vector;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataListener;

/**
 * <h1>Serial Implementation Class</h1>
 * 
 * <p>
 * This implementation class implements the 'Serial' interface using the WiringPi Serial library.
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
 * @see #Serial
 * @see #SerialDataEvent
 * @see #SerialDataListener
 * @see #SerialFactory
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class SerialImpl implements Serial
{
    private int fileDescriptor = -1;
    private Vector<SerialDataListener> listeners = new Vector<SerialDataListener>();
    private SerialDataMonitorThread monitor;

    public int open(String device, int baudRate)
    {
        fileDescriptor = com.pi4j.wiringpi.Serial.serialOpen(device, baudRate);
        return fileDescriptor;
    }

    public boolean isOpen()
    {
        return (fileDescriptor >= 0);
    }

    public void close()
    {
        com.pi4j.wiringpi.Serial.serialClose(fileDescriptor);
    }

    public void flush()
    {
        com.pi4j.wiringpi.Serial.serialFlush(fileDescriptor);
    }

    public char read()
    {
        return (char) com.pi4j.wiringpi.Serial.serialGetchar(fileDescriptor);
    }

    public char readLine()
    {
        return ' ';
    }

    public void write(char data)
    {
        com.pi4j.wiringpi.Serial.serialPutchar(fileDescriptor, data);
    }

    public void write(char data[])
    {
        write(new String(data));
    }

    public void write(byte data)
    {
        com.pi4j.wiringpi.Serial.serialPutchar(fileDescriptor, (char) data);
    }

    public void write(byte data[])
    {
        write(new String(data));
    }

    public void write(String data)
    {
        // break data into packets of 1024 bytes
        int position = 0;
        while (position < data.length())
        {
            int length = 1024;
            if (position + 1024 > data.length())
                com.pi4j.wiringpi.Serial.serialPuts(fileDescriptor, data.substring(position));
            else
                com.pi4j.wiringpi.Serial.serialPuts(fileDescriptor,
                                                    data.substring(position, (position + length)));
            position += length;
        }
    }

    public void write(String data, String... args)
    {
        write(String.format(data, (Object[]) args));
    }

    public int availableBytes()
    {
        return com.pi4j.wiringpi.Serial.serialDataAvail(fileDescriptor);
    }

    /**
     * <h1>Add Serial Event Listener</h1>
     * 
     * <p>
     * Java consumer code can call this method to register itself as a listener for serial data
     * events.
     * </p>
     * 
     * @see #SerialListener
     * @see #SerialDataEvent
     * 
     * @param listener <p>
     *            A class instance that implements the SerialListener interface.
     *            </p>
     */
    public synchronized void addListener(SerialDataListener listener)
    {
        // add the new listener to the list of listeners
        listeners.addElement(listener);

        // if there is not a current listening monitor thread running,
        // then lets start it now
        if (monitor == null || monitor.isAlive() == false)
        {            
            monitor = new SerialDataMonitorThread(this, listeners);
            monitor.start();
        }
    }

    /**
     * <h1>Remove Serial Event Listener</h1>
     * 
     * <p>
     * Java consumer code can call this method to unregister itself as a listener for serial data
     * events.
     * </p>
     * 
     * @see #SerialListener
     * @see #SerialDataEvent
     * 
     * @param listener <p>
     *            A class instance that implements the SerialListener interface.
     *            </p>
     */
    public synchronized void removeListener(SerialDataListener listener)
    {
        // remove the listener from the list of listeners
        listeners.removeElement(listener);
        
        // if there are not more listeners, then exit and destroy 
        // the monitor thread now
        if(listeners.isEmpty() && monitor != null)
        {
            monitor.exit();
            monitor = null;
        }
    }
}
