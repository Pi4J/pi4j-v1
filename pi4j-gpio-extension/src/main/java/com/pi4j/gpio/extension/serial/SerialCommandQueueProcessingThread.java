package com.pi4j.gpio.extension.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  SerialCommandQueueProcessingThread.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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


import com.pi4j.io.serial.Serial;

import java.io.IOException;
import java.util.concurrent.LinkedTransferQueue;


public class SerialCommandQueueProcessingThread extends Thread {
    public static final int DEAFULT_DELAY = 100; // milliseconds
    private boolean exiting = false;
    private final Serial serial;
    private final int delay;
    private final LinkedTransferQueue<String> queue = new LinkedTransferQueue<>();


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
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        }
    }
}
