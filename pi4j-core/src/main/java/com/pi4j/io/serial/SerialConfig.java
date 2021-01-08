package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SerialConfig.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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

public class SerialConfig {

    private String device = Serial.DEFAULT_COM_PORT;
    private Baud baud = Baud._9600;
    private DataBits dataBits = DataBits._8;
    private Parity parity = Parity.NONE;
    private StopBits stopBits = StopBits._1;
    private FlowControl flowControl = FlowControl.NONE;

    public SerialConfig(){}

    /*
     *  The device address of the serial port to access. You can use constant 'Serial.DEFAULT_COM_PORT'
     *   if you wish to access the default serial port provided via the GPIO header.
     */
    public String device() { return device; }

    /*
     *  The device address of the serial port to access. You can use constant 'Serial.DEFAULT_COM_PORT'
     *   if you wish to access the default serial port provided via the GPIO header.
     */
    public SerialConfig device(String device) { this.device = device; return this; }

    /*
     * The baud rate to use with the serial port.
     */
    public Baud baud() { return baud; }

    /*
     * The baud rate to use with the serial port.
     */
    public SerialConfig baud(Baud baud) { this.baud = baud; return this; }

    /*
     * The data bits to use for serial communication. (5,6,7,8)
     */
    public DataBits dataBits() { return dataBits; }

    /*
     * The data bits to use for serial communication. (5,6,7,8)
     */
    public SerialConfig dataBits(DataBits dataBits) { this.dataBits = dataBits; return this; }

    /*
     * The parity setting to use for serial communication. (None, Event, Odd, Mark, Space)
     */
    public Parity parity() { return parity; }

    /*
     * The parity setting to use for serial communication. (None, Event, Odd, Mark, Space)
     */
    public SerialConfig parity(Parity parity) { this.parity = parity; return this; }

    /*
     * The stop bits to use for serial communication. (1,2)
     */
    public StopBits stopBits() { return stopBits; }

    /*
     * The stop bits to use for serial communication. (1,2)
     */
    public SerialConfig stopBits(StopBits stopBits) { this.stopBits = stopBits; return this; }

    /*
     * The flow control option to use for serial communication. (none, hardware, software)
     */
    public FlowControl flowControl() { return flowControl; }

    /*
     * The flow control option to use for serial communication. (none, hardware, software)
     */
    public SerialConfig flowControl(FlowControl flowControl) { this.flowControl = flowControl; return this; }

    @Override
    public String toString(){
        // /dev/ttyAMA0 (38400, 8N1) [FC=NONE]
        return device() + " (" +
                baud().getValue() + "," +
                dataBits().getValue() +
                parity().toString().substring(0, 1) +
                stopBits().getValue() + ") {" +
                "FC:" + flowControl().toString() + "}";
    }

}
