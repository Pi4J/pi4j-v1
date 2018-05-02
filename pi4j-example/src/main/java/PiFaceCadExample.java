/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  PiFaceCadExample.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2018 Pi4J
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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.pi4j.component.lcd.impl.PiFaceCadLcd;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.spi.SpiChannel;
import java.util.Date;

/**
 *
 * @author sbodmer
 */
public class PiFaceCadExample {

    public static void main(String args[]) {

        try {
            System.out.println("<--Pi4J--> PiFaceCadExample ... started. " + new Date());

            GpioController gpio = GpioFactory.getInstance();
            final PiFaceCadLcd lcd = new PiFaceCadLcd(SpiChannel.CS1);

            lcd.setButtonsListener(new PiFaceCadLcd.PiFaceCadButtonsListener() {
                @Override
                public void pifaceButtonsEvent(int sw, int released) {
                    lcd.clear();
                    try {
                        lcd.write("P :("+sw+") ");
                        if ((sw & PiFaceCadLcd.PiFaceCadButtonsListener.BUTTON_1) != 0) lcd.write("1 ");
                        if ((sw & PiFaceCadLcd.PiFaceCadButtonsListener.BUTTON_2) != 0) lcd.write("2 ");
                        if ((sw & PiFaceCadLcd.PiFaceCadButtonsListener.BUTTON_3) != 0) lcd.write("3 ");
                        if ((sw & PiFaceCadLcd.PiFaceCadButtonsListener.BUTTON_4) != 0) lcd.write("4 ");
                        if ((sw & PiFaceCadLcd.PiFaceCadButtonsListener.BUTTON_5) != 0) lcd.write("5 ");
                        if ((sw & PiFaceCadLcd.PiFaceCadButtonsListener.BUTTON_MIDDLE) != 0) lcd.write("M ");
                        if ((sw & PiFaceCadLcd.PiFaceCadButtonsListener.BUTTON_LEFT) != 0) lcd.write("L ");
                        if ((sw & PiFaceCadLcd.PiFaceCadButtonsListener.BUTTON_RIGHT) != 0) lcd.write("R ");
                        lcd.write("\n");
                        lcd.write("R :("+released+") ");
                        if ((released & PiFaceCadLcd.PiFaceCadButtonsListener.BUTTON_1) != 0) lcd.write("1 ");
                        if ((released & PiFaceCadLcd.PiFaceCadButtonsListener.BUTTON_2) != 0) lcd.write("2 ");
                        if ((released & PiFaceCadLcd.PiFaceCadButtonsListener.BUTTON_3) != 0) lcd.write("3 ");
                        if ((released & PiFaceCadLcd.PiFaceCadButtonsListener.BUTTON_4) != 0) lcd.write("4 ");
                        if ((released & PiFaceCadLcd.PiFaceCadButtonsListener.BUTTON_5) != 0) lcd.write("5 ");
                        if ((released & PiFaceCadLcd.PiFaceCadButtonsListener.BUTTON_MIDDLE) != 0) lcd.write("M ");
                        if ((released & PiFaceCadLcd.PiFaceCadButtonsListener.BUTTON_LEFT) != 0) lcd.write("L ");
                        if ((released & PiFaceCadLcd.PiFaceCadButtonsListener.BUTTON_RIGHT) != 0) lcd.write("R ");
                        System.out.println("Pressed:"+sw+" Released:"+released);

                    } catch (Exception ex) {
                        ex.printStackTrace();

                    }

                }
            });

            lcd.setBackLight(true);
            lcd.write("PiFace LCD Check\nTouch buttons...");

            //--- Wait 30 seconds before quitting
            int cnt = 0;
            while (cnt < 30) {
                int sw = lcd.readSwitches();
                // System.out.println("["+cnt+"] SW:"+sw);
                System.out.println("[" + cnt + "]");
                Thread.sleep(1000);
                cnt++;
            }

            //--- Switch off all needed stuff
            lcd.setBackLight(false);
            lcd.clear();
            lcd.setBlink(false);
            lcd.setCursor(false);
            lcd.close(); //--- Dont't forget to close it, it's important to release some resources
            gpio.shutdown();

        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }
}
