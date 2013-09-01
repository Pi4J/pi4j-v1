/*
 * @(#)I2CLcdDisplay.java   08/31/13
 * 
 *
 */



package com.pi4j.component.lcd.impl;

//~--- non-JDK imports --------------------------------------------------------

/*
* #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  I2CLcdDisplay.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
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
import com.pi4j.component.lcd.LCD;
import com.pi4j.component.lcd.LCDBase;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.wiringpi.Lcd;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

//~--- classes ----------------------------------------------------------------

/**
 * Class description
 *
 *
 * @version        1.0, 08/29/13
 * @author         Andrew Chandler
 */
public class I2CLcdDisplay extends LCDBase implements LCD {
    boolean             backLightDesiredState = true;
    boolean             backlight             = false;
    boolean             rsFlag                = false;
    boolean             eFlag                 = false;
    private I2CDevice   dev                   = null;
    private final int[] LCD_LINE_ADDRESS      = { 0x80, 0xC0, 0x94, 0xD4 };

    /** posilame text */
    private final boolean LCD_CHR = true;

    /** posilame command */
    private final boolean LCD_CMD = false;

    // private int           lcdHandle;
    private int   pulseData = 0;
    protected int rows;
    protected int columns;

    /**
     * Constructs ...
     *
     *
     * @param rows
     * @param columns
     * @param i2cBus
     * @param i2cAddress
     * @param backlightBit
     * @param rsBit
     * @param rwBit
     * @param eBit
     * @param d7
     * @param d6
     * @param d5
     * @param d4
     *
     * @throws Exception
     */
    public I2CLcdDisplay(int rows, int columns, int i2cBus, int i2cAddress, int backlightBit, int rsBit, int rwBit,
                         int eBit, int d7, int d6, int d5, int d4)
            throws Exception {
        this.rows    = rows;
        this.columns = columns;

        int bits[] = { d7, d6, d5, d4 };

        this.rows    = rows;
        this.columns = columns;

        I2CBus bus = I2CFactory.getInstance(i2cBus);

        dev = bus.getDevice(i2cAddress);
        this.init();
    }

    @Override
    public void clear() {
        try {
            lcd_byte(0x01, LCD_CMD);
        } catch (Exception ex) {
            Logger.getLogger(I2CLcdDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void write(byte data) {
        try {
            lcd_byte(data, LCD_CHR);
        } catch (Exception ex) {
            Logger.getLogger(I2CLcdDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void write(String data) {
        for (int i = 0; i < data.length(); i++) {
            try {
                lcd_byte(data.charAt(i), LCD_CHR);
            } catch (Exception e) {
                System.out.println("Problems writing data");
            }
        }
    }

    public void lcd_byte(int val, boolean type) throws Exception {

        // typ zapisu
        setRS(type);

        // High Bit
        write(val >> 4);
        pulse_en(type, val >> 4);    // cmd or display data

        // lowbit
        write(val & 0x0f);
        pulse_en(type, val & 0x0f);
    }

    private void init() throws Exception {
        lcd_byte(0x33, LCD_CMD);    // 4 bit
        lcd_byte(0x32, LCD_CMD);    // 4 bit
        lcd_byte(0x28, LCD_CMD);    // 4bit - 2 line
        lcd_byte(0x08, LCD_CMD);    // don't shift, hide cursor
        lcd_byte(0x01, LCD_CMD);    // clear and home display
        lcd_byte(0x06, LCD_CMD);    // move cursor right
        lcd_byte(0x0c, LCD_CMD);    // turn on
    }

    private void pulse_en(boolean type, int val) throws Exception {
        setE(true);
        write(val);
        setE(false);
        write(val);

        // po CMD by se melo chvilku pockat
        if (type == LCD_CMD) {
            Thread.sleep(1);
        }
    }    // private voi

    private void write(int tmpData) throws Exception {
        byte out = (byte) (tmpData | (backlight
                                      ? 128
                                      : 0) | (rsFlag
                ? 64
                : 0) | (eFlag
                        ? 16
                        : 0));

        dev.write(out);
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
    public void setCursorHome() {
        try {
            lcd_byte(0x02, LCD_CMD);
        } catch (Exception ex) {
            Logger.getLogger(I2CLcdDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setCursorPosition(int row, int column) {
        validateCoordinates(row, column);

        try {
            lcd_byte(LCD_LINE_ADDRESS[row] + column, LCD_CMD);

            // Lcd.lcdPosition(lcdHandle, column, row);
        } catch (Exception ex) {
            Logger.getLogger(I2CLcdDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setRS(boolean val) {
        rsFlag = val;
    }

    private void setE(boolean val) {
        eFlag = val;
    }
}
