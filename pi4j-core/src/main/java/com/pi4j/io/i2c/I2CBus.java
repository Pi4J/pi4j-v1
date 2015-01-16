package com.pi4j.io.i2c;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  I2CBus.java  
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

import java.io.IOException;

/**
 * This is abstraction of i2c bus. This interface allows bus to return i2c device.
 * 
 * @author Daniel Sendula
 *
 */

public interface I2CBus {

    public static final int BUS_0 = 0;
    public static final int BUS_1 = 1;

    /**
     * Returns i2c device.
     * @param address i2c device's address
     * @return i2c device
     * 
     * @throws IOException thrown in case this bus cannot return i2c device.
     */
    I2CDevice getDevice(int address) throws IOException;
    
    String getFileName();
    
    int getFileDescriptor();
    
    /**
     * Closes this bus. This usually means closing underlying file.
     * 
     * @throws IOException thrown in case there are problems closing this i2c bus.
     */
    void close() throws IOException;
}
