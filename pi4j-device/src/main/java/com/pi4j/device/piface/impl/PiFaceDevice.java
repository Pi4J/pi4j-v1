package com.pi4j.device.piface.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PiFaceDevice.java  
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

import com.pi4j.device.piface.PiFace;
import com.pi4j.io.spi.SpiChannel;

import java.io.IOException;

public class PiFaceDevice extends PiFaceBase {

    // default constructor
    public PiFaceDevice(byte spiAddress, int spiChannel) throws IOException {
        super(spiAddress, spiChannel);
    }

    // default constructor
    public PiFaceDevice(byte spiAddress, SpiChannel spiChannel) throws IOException {
        super(spiAddress, spiChannel);
    }

    // alternate constructor that assumes default address
    public PiFaceDevice(SpiChannel spiChannel) throws IOException {
        super(PiFace.DEFAULT_ADDRESS, spiChannel);
    }

    // alternate constructor that assumes default address
    public PiFaceDevice(int spiChannel) throws IOException {
        super(PiFace.DEFAULT_ADDRESS, spiChannel);
    }

}
