package com.pi4j.io.serial.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SerialDataMonitorThread.java  
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


import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
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
 * Gordon Henderson @ <a href="http://wiringpi.com/">http://wiringpi.com/</a>)
 * </blockquote>
 * </p>
 * 
 * @see com.pi4j.io.serial.Serial
 * @see com.pi4j.io.serial.SerialDataEvent
 * @see com.pi4j.io.serial.SerialDataListener
 * @see com.pi4j.io.serial.SerialFactory
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class SerialDataMonitorThread extends Thread {

    private boolean exiting = false;
    private final Serial serial;
    private final List<SerialDataListener> listeners;

    /**
     * <p> Default constructor </p>
     * <p> NOTE: This class is used internal to the Pi4J library by the SerialImpl class.</p>
     * 
     * @param serial  A class that implements the 'Serial' interface.
     * @param listeners  A collection of class instances that implement the SerialListener interface.
     */
    public SerialDataMonitorThread(Serial serial, CopyOnWriteArrayList<SerialDataListener> listeners) {
        this.serial = serial;
        this.listeners = listeners;
    }

    /**
     * Exit the monitoring thread.
     */
    public synchronized void shutdown() {
        exiting = true;
    }

    /**
     * This method is called when this monitoring thread starts
     */
    public void run() {
        StringBuilder buffer = new StringBuilder();

        while (!exiting) {
            if (serial.isOpen()) {
                if (serial.availableBytes() > 0) {
                    // reset buffer data
                    buffer.setLength(0);

                    // reset data from serial port
                    while (serial.availableBytes() > 0)
                        buffer.append(serial.read());

                    // when done reading, emit the event if there are any listeners
                    if (!listeners.isEmpty()) {
                        // iterate over the listeners and send the data events
                        SerialDataEvent event = new SerialDataEvent(serial, buffer.toString());
                        for (SerialDataListener sdl : listeners) {
                            sdl.dataReceived(event);
                        }
                    }
                }
            }

            // wait for a small interval before attempting to read serial data again
            try {
                Thread.sleep(serial.getMonitorInterval());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
