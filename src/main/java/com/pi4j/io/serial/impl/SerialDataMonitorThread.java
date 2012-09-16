package com.pi4j.io.serial.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library
 * FILENAME      :  SerialDataMonitorThread.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
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
