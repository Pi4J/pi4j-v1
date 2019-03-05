package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SerialPort.java
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

