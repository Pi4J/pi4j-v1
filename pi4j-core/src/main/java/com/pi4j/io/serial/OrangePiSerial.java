package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  OrangePiSerial.java
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
 * address (device file paths) that are exposed on the BananaPro.
 */
public class OrangePiSerial {

    // (UART0)
    public static final String UART0_COM_PORT = "/dev/ttyS0";

    // (UART2)
    public static final String UART2_COM_PORT = "/dev/ttyS2";

    // (UART3)
    public static final String UART3_COM_PORT = "/dev/ttyS3";

    // (UART7)
    public static final String UART7_COM_PORT = "/dev/ttyS3";

    public static final String DEFAULT_COM_PORT = UART3_COM_PORT;
}
