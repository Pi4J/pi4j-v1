package com.pi4j.io.i2c;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CIOException.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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

import com.pi4j.jni.I2C;

import java.io.IOException;

public class I2CIOException extends IOException {
    int rawCode;
    String baseMessage;

    /**
     * @param message Exception message
     * @param rawCode negative POSIX error code with pi4j offsets
     */
    public I2CIOException(String message, int rawCode) {
        super(message);

        this.rawCode = Math.abs(rawCode);
    }

    /**
     * Gets the POSIX code associated with this IO error
     *
     * @return POSIX error code
     */
    public int getCode() {
        return rawCode - getType() * 10000;
    }

    /**
     * @return true if is ioctl error
     */
    public boolean isIOCTL() {
        return getType() == 1;
    }

    /**
     * @return true if is write error
     */
    public boolean isWrite() {
        return getType() == 2;
    }

    /**
     * @return true if is read error
     */
    public boolean isRead() {
        return getType() == 3;
    }

    private int getType() {
        return rawCode / 10000;
    }
}
