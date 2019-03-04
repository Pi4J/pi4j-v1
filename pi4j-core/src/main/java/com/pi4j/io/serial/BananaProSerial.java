package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  BananaProSerial.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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

/**
 * This class simply exposed the available UART/serial port
 * address (device file paths) that are exposed on the BananaPro.
 */
public class BananaProSerial {

    // (UART0)
    // this com port is exposed on the J15 header and is typically
    // configured for terminal access to the operating system
    // - TX is located at J15-03
    // - RX is located at J15-02
    public static final String UART0_COM_PORT = "/dev/ttyS0";
    public static final String J15_COM_PORT = UART0_COM_PORT;

    // (UART2)
    // this com port is exposed on the CON6 header
    // - TX is located at CON6-13
    // - RX is located at CON6-11
    public static final String UART2_COM_PORT = "/dev/ttyS1";

    // (UART4)
    // this is the default com port exposed on the CON6 header
    // - TX is located at CON6-08
    // - RX is located at CON6-10
    public static final String UART4_COM_PORT = "/dev/ttyS2";
    public static final String DEFAULT_COM_PORT = UART4_COM_PORT;

//    // (UART5)  --- !! NOT CURRENTLY SUPPORTED IN OS !!
//    // this is the default com port exposed on the CON6 header
//    // - TX is located at CON6-24
//    // - RX is located at CON6-23
//    public static final String UART5_COM_PORT = "/dev/ttyS*";

//    // (UART6)  --- !! NOT CURRENTLY SUPPORTED IN OS !!
//    // this is the default com port exposed on the CON6 header
//    // - TX is located at CON6-19
//    // - RX is located at CON6-21
//    public static final String UART6_COM_PORT = "/dev/ttyS*";

    // (UART7)
    // this com port is exposed on the CON6 header
    // - TX is located at CON6-32
    // - RX is located at CON6-31
    public static final String UART7_COM_PORT = "/dev/ttyS3";
}
