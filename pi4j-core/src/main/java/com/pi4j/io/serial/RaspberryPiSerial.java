package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  RaspberryPiSerial.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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
 * address (device file paths) that are exposed on the RaspberryPi.
 */
public class RaspberryPiSerial {

    public static final String AMA0_COM_PORT = "/dev/ttyAMA0";
    public static final String DEFAULT_COM_PORT = AMA0_COM_PORT;

    // The "S0" com port was added with the introduction of the Raspberry Pi 3B model.
    public static final String S0_COM_PORT = "/dev/ttyS0";
}
