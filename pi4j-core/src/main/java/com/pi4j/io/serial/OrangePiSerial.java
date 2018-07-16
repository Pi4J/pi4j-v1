package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  OrangePiSerial.java
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
