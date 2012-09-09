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
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;

/**
 * <h1>Serial Data Monitoring Implementation Class</h1>
 * 
 * <p>
 * This implementation class implements the 'Serial' monitoring thread to poll the serial received
 * buffer and notify registered event listeners when data is available.
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
public class SerialDataMonitorThread extends Thread
{
    public static final int DELAY = 100; // milliseconds
    boolean exiting = false;
    Serial serial;
    private Vector<SerialDataListener> listeners;

    /**
     * <p>
     * Default constructor
     * </p>
     * <p>
     * NOTE: This class is used internal to the Pi4J library by the SerialImpl class./p>
     * 
     * @param serial <p>
     *            A class that implements the 'Serial' interface.
     *            </p>
     * @param listeners <p>
     *            A collection of class instances that implement the SerialListener interface.
     *            </p>
     */
    public SerialDataMonitorThread(Serial serial, Vector<SerialDataListener> listeners)
    {
        this.serial = serial;
        this.listeners = listeners;
    }

    /**
     * <p>
     * Exit the monitoring thread.
     * </p>
     */
    public synchronized void exit()
    {
        exiting = true;
    }

    /**
     * <p>
     * This method is called when this monitoring thread starts
     * </p>
     */
    @SuppressWarnings("unchecked")
    public void run()
    {
        StringBuffer buffer = new StringBuffer();

        while (!exiting)
        {
            if (serial.isOpen())
            {
                if (serial.availableBytes() > 0)
                {
                    // reset buffer data
                    buffer.setLength(0);

                    // reset data from serial port
                    while (serial.availableBytes() > 0)
                        buffer.append(serial.read());

                    // when done reading, emit the event if there are any listeners
                    if (listeners.size() > 0)
                    {
                        // create a cloned copy of the listeners
                        Vector<SerialDataListener> dataCopy;
                        dataCopy = (Vector<SerialDataListener>) listeners.clone();

                        // iterate over the listeners and send teh data events
                        for (int i = 0; i < dataCopy.size(); i++)
                        {
                            SerialDataEvent event = new SerialDataEvent(serial, buffer.toString());
                            ((SerialDataListener) dataCopy.elementAt(i)).dataReceived(event);
                        }
                    }
                }
            }

            // wait for a small interval before attempting to read serial data again
            try
            {
                Thread.sleep(DELAY);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
