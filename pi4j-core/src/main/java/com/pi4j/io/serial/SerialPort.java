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
 * Copyright (C) 2012 - 2021 Pi4J
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
	 *
	 * REF:  https://www.raspberrypi.org/documentation/configuration/uart.md
	 *
	 *   By default, only UART0 is enabled.
	 *   The following table summarises the assignment of the first two UARTs:
	 *
	 *     ----------------------------------------------------------------
	 *     Model	              |  first PL011 (UART0)   | mini UART
	 *     ----------------------------------------------------------------
	 *     Raspberry Pi Zero       primary                 secondary
	 *     Raspberry Pi Zero W     secondary (Bluetooth)   primary
	 *     Raspberry Pi 1          primary                 secondary
	 *     Raspberry Pi 2          primary                 secondary
	 *     Raspberry Pi 3          secondary (Bluetooth)   primary
	 *     Raspberry Pi 4          secondary (Bluetooth)   primary
	 *     ----------------------------------------------------------------
	 *     Note: the mini UART is disabled by default, whether it is designated
	 *           primary or secondary UART.
	 *
	 *   Linux devices on Raspberry Pi OS:
	 *     ----------------------------------------------------------
	 *     Linux device	       Description
	 *     ----------------------------------------------------------
	 *     /dev/ttyS0          mini UART
	 *     /dev/ttyAMA0        first PL011 (UART0)
	 *     /dev/serial0        primary UART
	 *     /dev/serial1        secondary UART
	 *     ----------------------------------------------------------
	 *     Note: /dev/serial0 and /dev/serial1 are symbolic links which point
	 *           to either /dev/ttyS0 or /dev/ttyAMA0.
     */
    public static String getDefaultPort(SystemInfo.BoardType board) throws UnsupportedBoardType {
        switch (board){
			// -------------------------------------------------------------
            // LEGACY RASPBERRY PI MODELS: 1A, 1B, 1A+, 1B+, CM1, 2B, Zero
			// -------------------------------------------------------------
            case RaspberryPi_A:
            case RaspberryPi_B_Rev1:
            case RaspberryPi_B_Rev2:
            case RaspberryPi_A_Plus:
            case RaspberryPi_B_Plus:
            case RaspberryPi_ComputeModule:
            case RaspberryPi_2B:
            case RaspberryPi_Zero:
            case RaspberryPi_ComputeModule3:
            case RaspberryPi_Alpha:
            case RaspberryPi_Unknown: {
                return RaspberryPiSerial.DEFAULT_COM_PORT;
            }

			// --------------------------------------------------------
            // NEWER RASPBERRY PI MODELS: ZeroW, 3B, 3B+, 4B, 400, CM4
            // --------------------------------------------------------
			case RaspberryPi_ZeroW:
            case RaspberryPi_3B:
            case RaspberryPi_3B_Plus:
			case RaspberryPi_4B:
			case RaspberryPi_400:
			case RaspberryPi_ComputeModule4:{
                // if the /dev/ttyS0 port exists, then use it as the default serial port
                File s0ComPort = new File(RaspberryPiSerial.S0_COM_PORT);
                if((s0ComPort.exists())){
                    return RaspberryPiSerial.S0_COM_PORT;
                }
                return RaspberryPiSerial.DEFAULT_COM_PORT;
            }

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

