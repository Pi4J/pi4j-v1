package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SerialPort.java
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

import com.pi4j.io.gpio.exception.UnsupportedBoardType;
import com.pi4j.io.gpio.exception.UnsupportedPinEventsException;
import com.pi4j.system.SystemInfo;

import java.io.IOException;

public class SerialPort {

    /**
     * Get the default serial port for the detected platform/model/board revision.
     *
     * @return com port device path
     * @throws IOException
     * @throws InterruptedException
     */
    public static String getDefaultPort() throws IOException, InterruptedException, UnsupportedBoardType {
        return getDefaultPort(SystemInfo.getBoardType());
    }

    /**
     * Get the default serial port for the specified platform/model/board revision.
     *
     * @param board hardware board type
     * @return com port device path
     */
    public static String getDefaultPort(SystemInfo.BoardType board) throws UnsupportedBoardType {
        switch (board){
            // ------------------------
            // ALL RASPBERRY PI MODELS
            // (except Model 3B)
            // ------------------------
            case RaspberryPi_A:
            case RaspberryPi_B_Rev1:
            case RaspberryPi_B_Rev2:
            case RaspberryPi_A_Plus:
            case RaspberryPi_B_Plus:
            case RaspberryPi_ComputeModule:
            case RaspberryPi_2B:
            case RaspberryPi_Zero:
            case RaspberryPi_Alpha:
            case RaspberryPi_Unknown: {
                return RaspberryPiSerial.DEFAULT_COM_PORT;
            }

            // ------------------------
            // RASPBERRY PI MODEL 3B
            // ------------------------
            case RaspberryPi_3B: {
                return RaspberryPiSerial.S0_COM_PORT;
            }

            // ------------------------
            // BANANAPI and BANANAPRO
            // ------------------------
            case BananaPi: {
                return BananaPiSerial.DEFAULT_COM_PORT;
            }
            case BananaPro: {
                return BananaProSerial.DEFAULT_COM_PORT;
            }

            // ------------------------
            // ODROID
            // ------------------------
            // TODO: Implement serial for Odroid boards
        }

        // unknown board type, return null
        throw new UnsupportedBoardType();
	}
}

