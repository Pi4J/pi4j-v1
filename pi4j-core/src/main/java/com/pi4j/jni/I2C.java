package com.pi4j.jni;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2C.java  
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

import com.pi4j.util.NativeLibraryLoader;

public class I2C 
{
    static 
    {
        // Load the platform library
        NativeLibraryLoader.load("pi4j", "libpi4j.so");
    }

    public static native int i2cOpen(String device);
    public static native int i2cClose(int fd);
    public static native int i2cWriteByte(int fd, int deviceAddress, int localAddress, byte data);
    public static native int i2cWriteBytes(int fd, int deviceAddress, int localAddress, int size, int offset, byte[] data);
    public static native int i2cReadByte(int fd, int deviceAddress, int localAddress);
    public static native int i2cReadBytes(int fd, int deviceAddress, int localAddress, int size, int offset, byte[] data);
}
