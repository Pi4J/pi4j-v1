package com.pi4j.component.display;

import java.awt.Color;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  Display.java  
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
 * <p>Display is a abstraction for a two-dimensional array. </p>
 * 
 * <p>
 * For convention, the pixel position (0, 0) is the first pixel left to right 
 * and top to down direction 
 * </p>
 */
public interface Display {

    /**
     * Set a specific pixel for a color 
     * 
     * @param x Row position. 0 is first, top to down direction
     * @param y Column position. 0 is first, left to right direction
     * @param color
     */
    void setPixel(int x, int y, Color color);
    
    /**
     * Repaint the display, updating changes caused by use of setPixel method  
     */
    void redraw();
    
    /**
     * Change the Display for initial stage
     */
    void clear();

    int getWidth();
    int getHeight();
}
