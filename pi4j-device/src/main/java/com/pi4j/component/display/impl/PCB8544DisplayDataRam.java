package com.pi4j.component.display.impl;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;

import com.pi4j.component.display.WhiteBlackDisplay;
import com.pi4j.component.display.impl.PCD8544Constants.DisplaySize;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PCB8544DisplayDataRam.java  
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
 */

/**
 * Display Data Ram abstraction <br />
 * See Pcd8544 datasheet for more information.
 */
class PCB8544DisplayDataRam {

    interface DisplayDataRamSize {
        public static final int DDRAM_WIDTH  = DisplaySize.WIDTH;
        public static final int DDRAM_HEIGHT = DisplaySize.HEIGHT / 8;
        public static final int DDRAM_SIZE   = DDRAM_WIDTH * DDRAM_HEIGHT;
    }


    private PCB8544DDRamBank[][] buffer = new PCB8544DDRamBank[DisplayDataRamSize.DDRAM_WIDTH][DisplayDataRamSize.DDRAM_HEIGHT];

    private final PCD8544DisplayComponent display;
    private final Color initialColor;

    private Queue<PCB8544DDRamBank> changes;
    
    public PCB8544DisplayDataRam(PCD8544DisplayComponent display, Color initialColor) {
        this.display = display;
        this.initialColor = initialColor;
        
        this.changes = new LinkedList<>();

        for (int x = 0; x < DisplayDataRamSize.DDRAM_WIDTH; x++) {
            for (int y = 0; y < DisplayDataRamSize.DDRAM_HEIGHT; y++) {
                buffer[x][y] = new PCB8544DDRamBank(x, y, initialColor);
                this.changes.add(buffer[x][y]);
            }
        }
    }

    public void setPixel(int x, int y, Color color) {
        if (!isPositionExists(x, y))
            //throw new IndexOutOfBoundsException("Position ("+x+", "+y+") don't exists");
            return;
        if (!color.equals(WhiteBlackDisplay.BLACK) && !color.equals(WhiteBlackDisplay.WHITE))
            throw new RuntimeException("The color should be WhiteBlackDisplay.BLACK or WhiteBlackDisplay.WHITE!");
            //color = WhiteBlackDisplay.BLACK;

        PCB8544DDRamBank bank = getBank(x, y);
        boolean changedAnotherTime = bank.hasChanged(); 

        bank.setPixel(y%8, color);

        if (bank.hasChanged() && !changedAnotherTime)
            this.changes.add(bank);
    }

    private PCB8544DDRamBank getBank(int x, int y) {
        return buffer[x][y/8];
    }

    public Color getPixel(int x, int y) {
        if (!isPositionExists(x, y))
            throw new IndexOutOfBoundsException("Position ("+x+", "+y+") don't exists");

        return this.getBank(x, y).getPixel(y);
    }

    private boolean isPositionExists(int x, int y) {
        return !(x < 0 || y < 0 || x >= display.getWidth() || y >= display.getHeight());
    }

    public void clear() {
        for (int x = 0; x < PCD8544Constants.DisplaySize.WIDTH; x++)
            for (int y = 0; y < PCD8544Constants.DisplaySize.HEIGHT; y++)
                setPixel(x, y, initialColor);
    }
    
    public Queue<PCB8544DDRamBank> getChanges() {
        return changes;
    }
}