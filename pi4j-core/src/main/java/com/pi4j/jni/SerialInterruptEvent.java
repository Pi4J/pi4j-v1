package com.pi4j.jni;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SerialInterruptEvent.java
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


import java.util.EventObject;

/**
 * <p> This class provides the event object for Serial interrupt data receive event. </p>
 *
 * @see <a href="https://pi4j.com/">https://pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class SerialInterruptEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    private int fileDescriptor;
    private byte[] data;

    /**
     * <h1>Default event constructor</h1>
     *
     * @param obj Ignore this parameter
     * @param fileDescriptor The serial file handle/descriptor in use
     * @param data data bytes received in this event from the serial receive buffer
     */
    public SerialInterruptEvent(Object obj, int fileDescriptor, byte[] data) {
        super(obj);
        this.fileDescriptor = fileDescriptor;
        this.data = data;
    }

    /**
     * Get the serial port file descriptor/handle
     *
     * @return serial port file descriptor/handle
     */
    public int getFileDescriptor() {
        return fileDescriptor;
    }

    /**
     * Get the length of data bytes received in this event.
     *
     * @return length of data bytes received in this event
     */
    public int getLength() {
        return data.length;
    }

    /**
     * Get the data bytes received in this event.
     *
     * @return data bytes received in this event
     */
    public byte[] getData() {
        return data;
    }

}
