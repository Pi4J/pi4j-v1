package com.pi4j.jni;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SerialInterruptEvent.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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


import java.util.EventObject;

/**
 * <p> This class provides the event object for Serial interrupt data receive event. </p>
 *
 * @see <a href="https://www.pi4j.com/">https://www.pi4j.com/</a>
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
