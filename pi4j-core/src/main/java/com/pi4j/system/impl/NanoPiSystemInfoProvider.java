package com.pi4j.system.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  NanoPiSystemInfoProvider.java
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
 * NanoPi platform specific implementation of the SystemInfoProvider interface.
 *
 * Prefered Linux distribution: https://forum.armbian.com/
 */
public class NanoPiSystemInfoProvider extends DefaultSystemInfoProvider implements SystemInfoProvider {

    @Override
    public String getModelName() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCpuInfo("Processor");
    }

    @Override
    public SystemInfo.BoardType getBoardType() throws IOException, InterruptedException, UnsupportedOperationException {
    		SystemInfo.BoardType boardType = SystemInfo.BoardType.UNKNOWN;
    		BufferedReader br = null;

    		// for the NanoPi there is no simple way to detect the board type
        // see: https://forum.armbian.com/index.php?/topic/3733-nanopi-simple-way-to-differ-nanopi-boards-by-sw/#comment-27008
        try {

//            NanoPi_M1,
//            NanoPi_M1_Plus,
//            NanoPi_M3,
//            NanoPi_NEO,
//            NanoPi_NEO2,
//            NanoPi_NEO2_Plus,
//            NanoPi_NEO_Air,
//            NanoPi_S2,
//            NanoPi_A64,
//            NanoPi_K2


	        	br = new BufferedReader(new FileReader("/etc/armbian-release"));
            for(String line; (line = br.readLine()) != null; ) {
                if(line.contains("=")) {
                    String[] split = line.split("=");
                    if(split[0].toLowerCase().trim().startsWith("board")) {
                        switch(split[0].toLowerCase().trim()) {
                            case "nanopim1":
                            		boardType = SystemInfo.BoardType.NanoPi_M1;
                            case "nanopineo":
                            		boardType = SystemInfo.BoardType.NanoPi_NEO;
                            case "nanopiair":
                            		boardType = SystemInfo.BoardType.NanoPi_NEO_Air;
                            default:
                            		boardType = SystemInfo.BoardType.UNKNOWN;
                        }
                    }
                }
            }
        } catch(Exception e) {
        } finally {
	        	// close reader
	        	if(br != null) br.close();
        }
        return boardType;
    }

    @Override
    public float getCpuTemperature() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        // Armbian way
        try(BufferedReader br = new BufferedReader(new FileReader("/sys/devices/virtual/thermal/thermal_zone0/temp"))) {
            for(String line; (line = br.readLine()) != null; ) {
                return Float.parseFloat(line);
            }
        }
        throw new UnsupportedOperationException();
    }

}
