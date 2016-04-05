package com.pi4j.component.display.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Queue;

import com.pi4j.component.display.DisplayBuffer;
import com.pi4j.component.display.PixelBuffer;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  AWTDisplayComponent.java  
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
 * Based in Erkki 22-Jul-2014
 * https://raw.githubusercontent.com/noxo/SnakePI4J/master/src/org/noxo/devices/AWTDisplay.java
 * 
 * <p>WARNING: This don't works in Raspberry Pi - Console mode. <br />
 * Use for visual interfaces tests :D 
 * </p>
 */
public class AWTDisplayComponent implements com.pi4j.component.display.Display {

    private Frame screen;
    private int width;
    private int height;
    
    private DisplayBuffer buffer;
    
    public AWTDisplayComponent(int width, int height) {
        this.width = width;
        this.height = height;
        
        this.buffer = new DisplayBuffer(width, height, Color.WHITE);

        screen = new java.awt.Frame();
        screen.setSize(width, height);
        screen.setUndecorated(true);
        screen.validate();
        screen.setVisible(true);

        Dimension resolution = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        screen.setLocation(resolution.width/2-width/2, resolution.height/2-height/2);        
    }

    @Override
    public void setPixel(int x, int y, Color color) {
        this.buffer.setPixel(x, y, color);
    }

    @Override
    public void redraw() {
        Queue<PixelBuffer> pixelsChanged = buffer.getChanges();

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        while (!pixelsChanged.isEmpty()) {
            PixelBuffer pixel = pixelsChanged.remove();
            img.setRGB(pixel.x, pixel.y, pixel.getColor().getRGB());
        }

        simulateGPIODelay();

        Graphics g = screen.getGraphics();
        g.drawImage(img, 0, 0, screen);
    }

	private void simulateGPIODelay() {
		try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

    @Override
    public void clear() {
        for (int i=0; i<width; i++)
            for (int j=0; j<height; j++)
                buffer.setPixel(i, j, Color.WHITE);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }
}

