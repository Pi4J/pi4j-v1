/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  WiringPiPinAltExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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
import com.pi4j.wiringpi.Gpio;


public class WiringPiPinAltExample {

    public static void main(String args[]) throws InterruptedException {
        int pin;
        int dataPtr;
        int l, s, d;

        System.out.println("<--Pi4J--> GPIO ALT MODE test program");

        // setup wiringPi
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println(" ==>> GPIO SETUP FAILED");
            return;
        }

        // NOTE, this example does not really do anything visible, its just an usage example of settings ALT pin modes

        // iterate through all the available pin modes
        Gpio.pinMode (7, Gpio.INPUT);
        Gpio.pinMode (7, Gpio.OUTPUT);
        Gpio.pinMode (7, Gpio.ALT0);
        Gpio.pinMode (7, Gpio.ALT1);
        Gpio.pinMode (7, Gpio.ALT2);
        Gpio.pinMode (7, Gpio.ALT3);
        Gpio.pinMode (7, Gpio.ALT4);
        Gpio.pinMode (7, Gpio.ALT5);

        System.out.println("Exiting WiringPiPinAltExample");
    }
}

