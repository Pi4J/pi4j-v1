package com.pi4j.component.lcd.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  GpioLcdDisplay.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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


import com.pi4j.component.lcd.LCD;
import com.pi4j.component.lcd.LCDBase;
import com.pi4j.io.gpio.Pin;
import com.pi4j.wiringpi.Lcd;

public class GpioLcdDisplay extends LCDBase implements LCD
{
    protected int rows;
    protected int columns;
    private int lcdHandle;

    public GpioLcdDisplay(int rows, int columns, Pin rsPin, Pin strobePin, Pin... dataPins) {
        this.rows = rows;
        this.columns = columns;
        int bits[] = { 0,0,0,0,0,0,0,0 };

        // set wiringPi interface for internal use
        // we will use the WiringPi pin number scheme with the wiringPi library
        com.pi4j.wiringpi.Gpio.wiringPiSetup();

        // seed bit pin address array
        for(int index = 0; index < 8; index++) {
            if(index < dataPins.length)
                bits[index] = dataPins[index].getAddress();
        }

        // initialize LCD
        lcdHandle = Lcd.lcdInit(rows,
                                columns,
                                dataPins.length,
                                rsPin.getAddress(),
                                strobePin.getAddress(),
                                bits[0], bits[1], bits[2], bits[3], bits[4], bits[5], bits[6], bits[7]);

        // verify LCD initialization
        if (lcdHandle == -1)
            throw new RuntimeException("Invalid LCD handle returned from wiringPi: " + lcdHandle);
    }

    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public int getColumnCount() {
        return columns;
    }

    @Override
    public void clear() {
        Lcd.lcdClear(lcdHandle);
    }

    @Override
    public void setCursorHome() {
        Lcd.lcdHome(lcdHandle);
    }

    @Override
    public void setCursorPosition(int row, int column) {
        validateCoordinates(row, column);
        Lcd.lcdPosition(lcdHandle, column, row);
    }

    @Override
    public void write(byte data) {
        Lcd.lcdPutchar(lcdHandle, data);
    }

    @Override
    public void write(String data) {
        Lcd.lcdPuts(lcdHandle, data);
    }
}
