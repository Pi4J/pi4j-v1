/*
 * @(#)I2CLcdDisplay.java   09/02/13
* #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  I2CLcdDisplay.java
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



package com.pi4j.component.lcd.impl;

//~--- non-JDK imports --------------------------------------------------------

import com.pi4j.component.lcd.LCD;
import com.pi4j.component.lcd.LCDBase;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.util.BitSet;
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
    private boolean     backlight             = true;
    boolean             rsFlag                = false;
    boolean             eFlag                 = false;
    private I2CDevice   dev                   = null;
    private final int[] LCD_LINE_ADDRESS      = { 0x80, 0xC0, 0x94, 0xD4 };

    /** posilame text */
    private final boolean LCD_CHR = true;

    /** posilame command */
    private final boolean LCD_CMD = false;

    // private int           lcdHandle;
    @SuppressWarnings("unused")
    private int pulseData = 0;
    int         backlightBit;
    int         rsBit;
    int         rwBit;
    int         eBit;
    int         d7Bit;
    int         d6Bit;
    int         d5Bit;
    int         d4Bit;

    /**
     *
     */
    protected int rows;

    /**
     *
     */
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

        // int bits[] = { d7, d6, d5, d4 };
        this.d7Bit        = d7;
        this.d6Bit        = d6;
        this.d5Bit        = d5;
        this.d4Bit        = d4;
        this.backlightBit = backlightBit;
        this.rsBit        = rsBit;
        this.eBit         = eBit;
        this.rows         = rows;
        this.columns      = columns;

        I2CBus bus = I2CFactory.getInstance(i2cBus);

        dev = bus.getDevice(i2cAddress);
        this.init();
    }

    /**
     *
     */
    @Override
    public void clear() {
        try {
            lcd_byte(0x01, LCD_CMD);
        } catch (Exception ex) {
            Logger.getLogger(I2CLcdDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param data
     */
    @Override
    public void write(byte data) {
        try {
            lcd_byte(data, LCD_CHR);
        } catch (Exception ex) {
            Logger.getLogger(I2CLcdDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param data
     */
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

    /**
     *
     * @param val
     * @param type   true = display data, false = LCD cmd
     * @throws Exception
     */
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

    public void diagnostics() {
        try {
            lcd_byte(1, LCD_CHR);
        } catch (Exception ex) {
            Logger.getLogger(I2CLcdDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static BitSet fromByte(byte b) {
        BitSet bits = new BitSet(8);

        for (int i = 0; i < 8; i++) {
            bits.set(i, (b & 1) == 1);
            b >>= 1;
        }

        return bits;
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

    private void write(int incomingData) throws Exception {
        int    tmpData = incomingData;
        BitSet bits    = fromByte((byte) tmpData);
        byte   out     = (byte) ((bits.get(3)
                                  ? 1 << d7Bit
                                  : 0 << d7Bit) | (bits.get(2)
                ? 1 << d6Bit
                : 0 << d6Bit) | (bits.get(1)
                                 ? 1 << d5Bit
                                 : 0 << d5Bit) | (bits.get(0)
                ? 1 << d4Bit
                : 0 << d4Bit) | (isBacklight()
                                 ? 1 << backlightBit
                                 : 0 << backlightBit) | (rsFlag
                ? 1 << rsBit
                : 0 << rsBit) | (eFlag
                                 ? 1 << eBit
                                 : 0 << eBit));

        // ReMap - Default case where everything just works is

        /*
         *  7 backlightBit
         *  6 rsBit
         *  5 rwBit
         *  4 eBit
         *  3 d7
         *  2 d6
         *  1 d5
         *  0 d4
         */

        /*
         *  Sainsmart Use case:
         * 3 backlightBit
         * 0 rsBit
         * 1 rwBit
         * 2 eBit
         * 7 d7
         * 6 d6
         * 5 d5
         * 4 d4
         */
        dev.write(out);
    }

    /**
     *
     * @return
     */
    @Override
    public int getRowCount() {
        return rows;
    }

    /**
     *
     * @return
     */
    @Override
    public int getColumnCount() {
        return columns;
    }

    /**
     *
     */
    @Override
    public void setCursorHome() {
        try {
            lcd_byte(0x02, LCD_CMD);
        } catch (Exception ex) {
            Logger.getLogger(I2CLcdDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param row
     * @param column
     */
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

    /**
     * @param backlight the backlight to set
     */
    public void setBacklight(boolean backlight) {
        this.backlight = backlight;
    }

    /**
     * @param backlight the backlight to set
     * @param immediate optionally update the device immediately
     */
    public void setBacklight(boolean backlight, boolean immediate) throws IOException {
        setBacklight(backlight);
        if(immediate) {
            dev.write((byte) (backlight
                    ? 1 << (byte) backlightBit
                    : 0 << (byte) backlightBit));
        }
    }

    private void setRS(boolean val) {
        rsFlag = val;
    }

    private void setE(boolean val) {
        eFlag = val;
    }

    /**
     * @return the backlight
     */
    public boolean isBacklight() {
        return backlight;
    }
}
