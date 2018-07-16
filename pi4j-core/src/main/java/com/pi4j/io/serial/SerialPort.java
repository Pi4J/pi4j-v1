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

import com.pi4j.io.gpio.exception.UnsupportedBoardType;
import com.pi4j.system.SystemInfo;

import java.io.File;
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
            case RaspberryPi_ComputeModule3:
            case RaspberryPi_ZeroW:
            case RaspberryPi_Alpha:
            case RaspberryPi_Unknown: {
                return RaspberryPiSerial.DEFAULT_COM_PORT;
            }

            // ---------------------------
            // RASPBERRY PI MODEL 3B, 3B+
            // ---------------------------
            case RaspberryPi_3B:
            case RaspberryPi_3B_Plus: {
                // if the /dev/ttyS0 port exists, then use it as the default serial port
                File s0ComPort = new File(RaspberryPiSerial.S0_COM_PORT);
                if((s0ComPort.exists())){
                    return RaspberryPiSerial.S0_COM_PORT;
                }
                return RaspberryPiSerial.DEFAULT_COM_PORT;
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
            // BPI
            // ------------------------
            // TODO : Implement serial for BPI boards
			case Bpi_M1:
				break;
			case Bpi_M1P:
				break;
			case Bpi_M2:
				break;
			case Bpi_M2M:
				break;
			case Bpi_M2P:
				break;
			case Bpi_M2P_H2_Plus:
				break;
			case Bpi_M2P_H5:
				break;
			case Bpi_M2U:
				break;
			case Bpi_M2U_V40:
				break;
			case Bpi_M3:
				break;
			case Bpi_M64:
				break;
			case Bpi_R1:
				break;

            // ------------------------
            // NANOPI
            // ------------------------
            // TODO : Implement serial for NanoPi boards
			case NanoPi_A64:
				break;
			case NanoPi_K2:
				break;
			case NanoPi_M1:
				break;
			case NanoPi_M1_Plus:
				break;
			case NanoPi_M3:
				break;
			case NanoPi_NEO:
				break;
			case NanoPi_NEO2:
				break;
			case NanoPi_NEO2_Plus:
				break;
			case NanoPi_NEO_Air:
				break;
			case NanoPi_S2:
				break;

            // ------------------------
            // ODROID
            // ------------------------
            // TODO : Implement serial for Odroid boards

			case Odroid:
				break;

            // ------------------------
            // ORANGEPI
            // ------------------------
            // TODO : Implement serial for OrangePi boards
			case OrangePi:
				break;

            // ------------------------
            // UNKNOWN
            // ------------------------
			case UNKNOWN:
				break;
			default:
				break;
        }

        // unknown board type, return null
        throw new UnsupportedBoardType();
	}
}

