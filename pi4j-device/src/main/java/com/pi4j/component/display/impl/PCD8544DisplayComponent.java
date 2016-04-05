package com.pi4j.component.display.impl;

import java.awt.Color;
import java.util.Iterator;
import java.util.Queue;

import com.pi4j.component.display.WhiteBlackDisplay;
import com.pi4j.component.display.impl.PCD8544Constants.BitOrderFirst;
import com.pi4j.component.display.impl.PCD8544Constants.DisplaySize;
import com.pi4j.component.display.impl.PCD8544Constants.Setting;
import com.pi4j.component.display.impl.PCD8544Constants.SysCommand;
import com.pi4j.component.display.utils.ByteCommand;
import com.pi4j.component.display.utils.Command;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PCD8544DisplayComponent.java  
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
 * 
 * @author SrMouraSilva
 * Based in 2013 Giacomo Trudu - wicker25[at]gmail[dot]com
 * Based in 2010 Limor Fried, Adafruit Industries
 * Based in CORTEX-M3 version by Le Dang Dung, 2011 LeeDangDung@gmail.com (tested on LPC1769)
 * Based in Raspberry Pi version by Andre Wussow, 2012, desk@binerry.de
 * Based in Raspberry Pi Java version by Cleverson dos Santos Assis, 2013, tecinfcsa@yahoo.com.br
 */

/**
 * <p>PCD8544 display implementation.</p>
 * 
 * <p>This implementation uses software shiftOut implementation</p>
 */
public class PCD8544DisplayComponent implements WhiteBlackDisplay {

    private static final int CLOCK_TIME_DELAY = 1;//micro seconds // 10 nanosseconds is the correct 
    //http://stackoverflow.com/questions/11498585/how-to-suspend-a-java-thread-for-a-small-period-of-time-like-100-nanoseconds
    private static final int RESET_DELAY = 1;//10^-3ms

    private PCB8544DisplayDataRam DDRAM;

    /** Serial data input. */
    private GpioPinDigitalOutput DIN;
    /** Input for the clock signal */
    private GpioPinDigitalOutput SCLK;
    /** Data/Command mode select */
    private GpioPinDigitalOutput DC; 
    /** External rst input */
    private GpioPinDigitalOutput RST; 
    /** Chip Enable (CS/SS) */
    private GpioPinDigitalOutput SCE; 

    /**
     * 
     * @param din Serial data input.
     * @param sclk Input for the clock signal.
     * @param dc Data/Command mode select.
     * @param rst External rst input.
     * @param cs Chip Enable (CS/SS)
     * 
     * @param contrast
     * @param inverse
     */
    public PCD8544DisplayComponent(
            GpioPinDigitalOutput din, 
            GpioPinDigitalOutput sclk, 
            GpioPinDigitalOutput dc, 
            GpioPinDigitalOutput rst,
            GpioPinDigitalOutput cs,

            byte contrast,
            boolean inverse) {

        this.DDRAM = new PCB8544DisplayDataRam(this, Color.WHITE);

        this.DIN = din;
        this.SCLK = sclk;
        this.DC = dc;
        this.RST = rst;
        this.SCE = cs;

        reset();
        init(contrast, inverse);
        redraw();
    }

    private void reset() {
        RST.low();
        try {
            Thread.sleep(RESET_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RST.high();
    }

    private void init(byte contrast, boolean inverse) {
        sendCommand(SysCommand.FUNC, Setting.FUNC_H);
        sendCommand(SysCommand.BIAS, new ByteCommand(0x04));
        sendCommand(SysCommand.VOP, new ByteCommand(contrast & 0x7f ));
        sendCommand(SysCommand.FUNC);
        sendCommand(
            SysCommand.DISPLAY,
            Setting.DISPLAY_D,
            new ByteCommand(Setting.DISPLAY_E.cmd() * (byte) (inverse ? 1 : 0))
        );
    }

    /**
     * @param Send command | command | command...
     */
    private void sendCommand(Command ... commands) {
        byte result = 0;

        for (Command command : commands)
            result |= command.cmd();

        sendCommand(result);
    }

    private void sendCommand(byte data) {
        DC.low();

        SCE.low();
        writeData(data);
        SCE.high();
    }

    private void writeData(byte data) {
        BitOrderFirst order = BitOrderFirst.MSB;
        if (order == BitOrderFirst.MSB)
            writeDataMSBFirst(data);
        else
            writeDataLSBFirst(data);
    }

    private void writeDataLSBFirst(byte data) {
        for (byte i = 0; i < 8; ++i) {
            PinState bitState = (data & (1 << i)) >> i == 1 ? PinState.HIGH : PinState.LOW;
            DIN.setState(bitState);

            toggleClock();
        }
    }

    private void writeDataMSBFirst(byte data) {
        for (byte i = 7; i >= 0; --i) {
            PinState bitState = (data & (1 << i)) >> i == 1 ? PinState.HIGH : PinState.LOW;
            DIN.setState(bitState);

            toggleClock();
        }
    }

    private void toggleClock() {
        SCLK.high();
        // The pin changes usign wiring pi are 20ns?
        // The pi4j in Snapshot 1.1.0 are 1MHz ~ 1 microssecond in Raspberry 2      http://www.savagehomeautomation.com/projects/raspberry-pi-with-java-programming-the-internet-of-things-io.html#follow_up_pi4j
        // Its necessary only 10ns    Pag 22 - https://www.sparkfun.com/datasheets/LCD/Monochrome/Nokia5110.pdf
        // Not discoment :D
        //Gpio.delayMicroseconds(CLOCK_TIME_DELAY);
        SCLK.low();
    }

    public void setContrast(byte value) {
        sendCommand(SysCommand.FUNC, Setting.FUNC_H);
        sendCommand(SysCommand.VOP, new ByteCommand(value & 0x7f));
        sendCommand(SysCommand.FUNC);
    }

    @Override
    public void setPixel(int x, int y, Color color) {
        this.DDRAM.setPixel(x, y, color);
    }

    public Color getPixel(int x, int y) { 
        return this.DDRAM.getPixel(x, y);
    }

    @Override
    public void redraw() {
        Queue<PCB8544DDRamBank> changes = this.DDRAM.getChanges();
        while (!changes.isEmpty()) {
            PCB8544DDRamBank bank = changes.remove();
            setCursorY(bank.y());
            setCursorX(bank.x());

            sendData(bank);
        }
    }

    private void sendData(PCB8544DDRamBank bankData) {
        DC.high();

        SCE.low();
        writeData(bankData);
        SCE.high();
    }

    private void writeData(PCB8544DDRamBank bank) {
        Iterator<Color> iterator = bank.msbIterator();
        while (iterator.hasNext()) {
            Color color = iterator.next();
            DIN.setState(color.equals(Color.BLACK) ? true : false);

            toggleClock();
        }
        bank.setChanged(false);
    }

    private void setCursorX(int x) {
        sendCommand(SysCommand.XADDR, new ByteCommand(x));
    }

    private void setCursorY(int y) {
        sendCommand(SysCommand.YADDR, new ByteCommand(y));
    }

    public void clear() {
        this.DDRAM.clear();

        setCursorX(0);
        setCursorY(0);
    }
    
    @Override
    public int getWidth() {
        return DisplaySize.WIDTH;
    }
    
    @Override
    public int getHeight() {
        return DisplaySize.HEIGHT;
    }
}
