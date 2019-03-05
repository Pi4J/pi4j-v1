package com.pi4j.system.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  BpiSystemInfoProvider.java
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

import com.pi4j.system.SystemInfo;
import com.pi4j.system.SystemInfoProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * BPI platform specific implementation of the SystemInfoProvider interface.
 */
public class BpiSystemInfoProvider extends DefaultSystemInfoProvider implements SystemInfoProvider {

    public BpiSystemInfoProvider(){
        super();
    }

    @Override
    public String getModelName() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCpuInfo("Processor");
    }

    @Override
    public SystemInfo.BoardType getBoardType() throws IOException, InterruptedException, UnsupportedOperationException {
        // TODO: IMPLEMENT TYPE
        return SystemInfo.BoardType.UNKNOWN;
    }

    @Override
    public float getCpuTemperature() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        try(BufferedReader br = new BufferedReader(new FileReader("/sys/devices/platform/sunxi-i2c.0/i2c-0/0-0034/temp1_input"))) {
            for(String line; (line = br.readLine()) != null; ) {
                return Float.parseFloat(line) / 1000;
            }
        }
        throw new UnsupportedOperationException();
    }

}
