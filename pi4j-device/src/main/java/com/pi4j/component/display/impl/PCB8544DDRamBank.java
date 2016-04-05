package com.pi4j.component.display.impl;

import java.awt.Color;
import java.util.Iterator;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PCB8544DDRamBank.java  
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
 * Display Data Ram Bank abstraction <br />
 * See Pcd8544 datasheet for more information.
 */
class PCB8544DDRamBank {
    private final int x;
    private final int y;
    private Color[] colors;
    private boolean changed;

    public PCB8544DDRamBank(int x, int y, Color initialColor) {
        this.x = x;
        this.y = y;

        this.changed = false;
        this.colors = new Color[8];
        for (int i = 0; i < colors.length; i++)
            this.colors[i] = initialColor;
    }

    public void setPixel(int y, Color color) {
        if (colors[y] != color)
            this.changed = true;

        colors[y] = color;
    }
    
    public Color getPixel(int y) {
        return colors[y];
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public boolean hasChanged() {
        return this.changed;
    }

    public int x() {
        return x;
    }
    
    public int y() {
        return y;
    }
    
    /**
     * @deprecated It's not necessary
     */
    @Deprecated
    public Iterator<Color> lsbIterator() {
        return null;
    }
    
    public Iterator<Color> msbIterator() {
        return new MsbIterator(this);
    }

    private static class MsbIterator implements Iterator<Color> {
        private PCB8544DDRamBank PCB8544DisplayDDramBank;
        private int count;

        public MsbIterator(PCB8544DDRamBank PCB8544DisplayDDramBank) {
            this.PCB8544DisplayDDramBank = PCB8544DisplayDDramBank;
            this.count = 7;
        }

        @Override
        public Color next() {
            return PCB8544DisplayDDramBank.getPixel(count--);
        }

        @Override
        public boolean hasNext() {
            return count >= 0;
        }
    }
}