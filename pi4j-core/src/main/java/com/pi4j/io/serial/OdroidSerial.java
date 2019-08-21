package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  OdroidSerial.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
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
 * address (device file paths) that are exposed on the Odroid C1/C1+/C2.
 *
 * SEE: http://odroid.com/dokuwiki/doku.php?id=en:c1_hardware_uart
 */
public class OdroidSerial {

    // SEE:  http://odroid.com/dokuwiki/doku.php?id=en:c1_hardware_uart  (C1, C1+)
    //       http://odroid.com/dokuwiki/doku.php?id=en:c2_hardware_uart  (C2)
    //       http://odroid.com/dokuwiki/doku.php?id=en:xu3_hardware_uart (XU3)
    //       http://odroid.com/dokuwiki/doku.php?id=en:xu3_hardware_uart (XU4)

    // (UART0)  SUPPORTED BY: C1, C1+, C2
    // this com port is exposed on the CON5 header and is typically
    // configured for terminal access to the operating system
    // - TX is located at CON5-2
    // - RX is located at CON5-3
    public static final String UART0_COM_PORT_C = "/dev/ttyS0";
    public static final String CON5_COM_PORT_C = UART0_COM_PORT_C;

    // (UART1)  SUPPORTED BY: C2
    // this com port is exposed on the CON6 header
    // - TX is located at 40-pin GPIO header, pin #8
    // - RX is located at 40-pin GPIO header, pin #10
    public static final String UART1_COM_PORT_C2 = "/dev/ttyS1";
    public static final String DEFAULT_COM_PORT_C2 = UART1_COM_PORT_C2;

    // (UART2)  SUPPORTED BY: C1, C1+
    // this com port is exposed on the CON6 header
    // - TX is located at 40-pin GPIO header, pin #8
    // - RX is located at 40-pin GPIO header, pin #10
    public static final String UART2_COM_PORT_C1 = "/dev/ttyS2";
    public static final String DEFAULT_COM_PORT_C1 = UART2_COM_PORT_C1;


    // (UART0)  SUPPORTED BY: XU3, XU4
    // this com port is exposed on the CON6 header
    // - RX is located at CON10, pin #6
    // - TX is located at CON10, pin #8
    public static final String UART0_COM_PORT_XU = "/dev/ttySAC0";
    public static final String DEFAULT_COM_PORT_XU = UART0_COM_PORT_XU;
}
