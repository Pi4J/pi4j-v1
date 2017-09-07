package com.pi4j.system.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  NanoPiSystemInfoProvider.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2017 Pi4J
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
