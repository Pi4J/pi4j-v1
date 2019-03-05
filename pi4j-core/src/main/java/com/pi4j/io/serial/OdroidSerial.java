package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  OdroidSerial.java
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
