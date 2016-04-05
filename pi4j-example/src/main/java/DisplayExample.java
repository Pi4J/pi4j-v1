import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.pi4j.component.display.Display;
import com.pi4j.component.display.WhiteBlackDisplay;
import com.pi4j.component.display.drawer.DisplayGraphics;
import com.pi4j.component.display.drawer.DisplayGraphics.ColorType;
import com.pi4j.component.display.impl.AWTDisplayComponent;
import com.pi4j.component.display.impl.PCD8544DisplayComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  DisplayExample.java  
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

public class DisplayExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Init");
        
        // Get a display compoment
        // Implemented: 
        //  - PCD8544: Nokia 3110 and 5110
        //  - AWT: For tests in pc develop
        Display display = getDisplayComponent();

        
        //System.out.println("Test: Display single pixel.\n");
        //display.setPixel(10, 10, Color.BLACK);

        // For any display changes, if you need to show the changes
        // call display.redraw();
        //display.redraw();


        // WARNING - DisplayGraphics ignore ALL manual changes (by display.setPixel())
        DisplayGraphics graphics = new DisplayGraphics(display, Color.BLACK, ColorType.BINARY);

        // Cleaning the display
        System.out.println("Clear");
        graphics.clear();

        Thread.sleep(5000);

        // Writting text in a (x, y) position
        graphics.drawString("Pi4j!", 0, 20);

        // It's possible set the font and style usign Font:
        //Font font = new Font("Serif", Font.PLAIN, 15);
        //graphics.setFont(font);

        // graphics.dispose is the REDRAW method
        // this implementation is imcompatible with Java specification :(
        // FIXME - Create a new method redraw for redraw :P
        graphics.dispose();
        graphics.clear();
        
        Thread.sleep(5000);

        // Drawing images
        System.out.println("Test: Draw image.\n");
        String baseName = System.getProperty("user.dir") + File.separator + "lib" + File.separator;
        String imageName = baseName + "pi4j-header-small3.png";
        //String imageName = baseName + "test.png";

        try {
            Image image = ImageIO.read(new File(imageName));

            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();
            graphics.clear();
        } catch (IOException e) {
            System.err.println("Possibly image was not found :/");
            e.printStackTrace();
        }

        Thread.sleep(5000);
        
        // Line, rectangle, circle (oval) tests

        System.out.println("Test: Draw many lines.\n");
        for (int i=0; i<84; i+=4) {
            graphics.drawLine(0, 0, i, 47);
            graphics.dispose();
        }

        for (int i=0; i<48; i+=4) {
            graphics.drawLine(0, 0, 83, i);
            graphics.dispose();
        }

        graphics.clear();
        Thread.sleep(5000);

        System.out.println("Test: Draw rectangles.\n");
        for (int i=0; i<48; i+=2) {
            graphics.drawRect(i, i, 83-i, 47-i);
            graphics.dispose();
        }

        graphics.clear();
        Thread.sleep(5000);



        System.out.println("Test: Draw multiple rectangles.\n");
        for (int i=0; i<48; i+=1) {
            Color color = i%2 == 0 ? Color.BLACK : Color.WHITE;
            graphics.setColor(color);
            graphics.fillRect(i, i, 83-i, 47-i);
            graphics.dispose();
        }

        graphics.clear();
        Thread.sleep(5000);
        

        System.out.println("Test: Draw multiple circles.\n");
        graphics.setColor(Color.BLACK);
        for (int i=0; i<48; i+=4) {
            graphics.drawOval(41-i/2, 23-i/2, i, i);
            graphics.dispose();
        }

        graphics.clear();
        Thread.sleep(5000);
        
        // The Java Graphics2D api has more methods
        // try it!
    }

    private static Display getDisplayComponent() {
        return new AWTDisplayComponent(500, 400);
        
        /* /
         PCD8544 example
        GpioController gpio = GpioFactory.getInstance();

        GpioPinDigitalOutput RST = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15, PinState.LOW);
        GpioPinDigitalOutput SCE = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, PinState.LOW);
        GpioPinDigitalOutput DC  = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
        GpioPinDigitalOutput DIN = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
        GpioPinDigitalOutput CLK = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);

        return new PCD8544DisplayComponent(
            DIN,
            CLK,
            DC,
            RST,
            SCE,
            (byte) 60,
            false
        );
        /**/
    }
}
