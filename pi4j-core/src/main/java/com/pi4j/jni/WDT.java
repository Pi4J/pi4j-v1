package com.pi4j.jni;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  WDT.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2018 Pi4J
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

import com.pi4j.util.NativeLibraryLoader;

/**
 *
 * @author Zerog
 */
public class WDT {

    private WDT() {
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    public static native int open(String file);

    public static native int close(int fd);

    public static native int disable(int fd);

    public static native int getTimeOut(int fd);

    public static native int setTimeOut(int fd, int timeout);

    public static native int ping(int fd);



}
