package com.pi4j.io.wdt.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  WDTimerImpl.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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

import com.pi4j.io.wdt.WDTimer;
import com.pi4j.jni.WDT;
import java.io.IOException;

/**
 *
 * @author zerog
 */
public final class WDTimerImpl implements WDTimer {

    private static final WDTimerImpl instance = new WDTimerImpl();

    /** File handle for watchdog */
    private int fd = -1;

    /** File name of watchdog */
    private String filename;

    /**
     * Singleton.
     *
     * @return instance
     */
    public static WDTimer getInstance() {
        return instance;
    }

    /**
     * Open Watchdog.
     *
     * @throws IOException
     */
    @Override
    public void open() throws IOException {
        open("/dev/watchdog");
    }


    /**
     * Open custom Watchdog
     *
     * @param file
     * @throws IOException
     */
    public void open(String file) throws IOException {
            filename = file;
            if(fd>0) {
                throw new IOException("File "+filename+" already open.");
            }
            fd = WDT.open(file);
            if(fd<0) {
                throw new IOException("Cannot open file handle for " + filename + " got " + fd + " back.");
            }
    }

    /**
     * Set new timeout
     *
     * @param timeout
     * @throws IOException
     */
    @Override
    public void setTimeOut(int timeout) throws IOException {
        isOpen();
        int ret = WDT.setTimeOut(fd, timeout);
        if(ret<0) {
            throw new IOException("Cannot set timeout for " + filename + " got " + ret + " back.");
        }
    }

    /**
     * Get timeout
     *
     * @return
     * @throws IOException
     */
    @Override
    public int getTimeOut() throws IOException {
        isOpen();
        int ret = WDT.getTimeOut(fd);
        if(ret<0) {
            throw new IOException("Cannot read timeout for " + filename + " got " + ret + " back.");
        }
        return ret;
    }

    /**
     * Ping a watchdog.
     *
     * @throws IOException
     */
    @Override
    public void heartbeat() throws IOException {
        isOpen();
        int ret = WDT.ping(fd);
        if(ret<0) {
            throw new IOException("Heartbeat error. File " + filename + " got " + ret + " back.");
        }
    }

    /**
     * Disable watchdog with "Magic Close". Now watchdog not working.
     * Close watchdog without call disable causes RaspberryPi reboot!
     * @throws IOException
     */
    @Override
    public void disable() throws IOException {
        isOpen();
        int ret = WDT.disable(fd);
        if(ret<0) {
            throw new IOException("Cannot disable WatchDog.  File " + filename + " got " + ret + " back.");
        }
    }

    /**
     * Close a watchdog (file). If close without {@link disable} Raspberry reboot after
     * timeout expired.
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        isOpen();
        int ret = WDT.close(fd);
        if(ret<0) {
            throw new IOException("Close file " + filename + " got " + ret + " back.");
        }
        fd=-1;
    }

    /**
     * Test if WDT is open
     * @throws IOException if not
     */
    private void isOpen() throws IOException {
        if(fd<0) {
            throw new IOException("Watchdog is not open yet");
        }
    }
}
