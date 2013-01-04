package com.pi4j.gpio.extension.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  SerialCommandQueueProcessingThread.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
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


import java.util.concurrent.LinkedTransferQueue;
import com.pi4j.io.serial.Serial;


public class SerialCommandQueueProcessingThread extends Thread {
    public static final int DEAFULT_DELAY = 100; // milliseconds
    private boolean exiting = false;
    private final Serial serial;
    private final int delay;
    private final LinkedTransferQueue<String> queue = new LinkedTransferQueue<String>();


    public SerialCommandQueueProcessingThread(Serial serial, int delay) {
        this.serial = serial;
        this.delay = delay;
    }

    public SerialCommandQueueProcessingThread(Serial serial) {
        this(serial, DEAFULT_DELAY);
    }

    /**
     * <p>
     * Exit the monitoring thread.
     * </p>
     */
    public synchronized void shutdown() {
        exiting = true;
    }

    /**
     * <p>
     * Exit the monitoring thread.
     * </p>
     */
    public void put(String data) {
        queue.add(data);
    }
    
    /**
     * <p>
     * This method is called when this monitoring thread starts
     * </p>
     */
    public void run() {
        while (!exiting) {
            if (!queue.isEmpty()) {
                // wait for a small interval before attempting next transmission
                try {
                    String data = queue.take();
                    
                    if (serial.isOpen()) {
                        serial.write(data);
                        serial.flush();
                    }
                    
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        }
    }
}
