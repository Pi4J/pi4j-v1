package com.pi4j.util;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  CommandArgumentParser.java
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

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinProvider;
import com.pi4j.io.gpio.PinPullResistance;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 *
 */
public class CommandArgumentParser {

    /**
     * This utility method searches for "--pin (#)" or "-p (#)" in the command
     * arguments array and returns a Pin instance based on the pin
     * address/number specified.
     *
     * @param pinProviderClass pin provider class to get pin instance from
     * @param defaultPin default pin instance to use if no --pin argument is found
     * @param args the argument array to search in
     * @return
     */
    public static Pin getPin(Class<? extends PinProvider> pinProviderClass, Pin defaultPin, String ... args){
        // search all arguments for the "--pin" or "-p" option
        // we skip the last argument in the array because we expect a value defined after the option designator
        for(int index = 0; index < (args.length-1); index++){
            if(args[index].toLowerCase().equals("--pin") ||
               args[index].toLowerCase().equals("-p")){
                try {
                    int pinAddress = Integer.parseInt(args[index+1]);
                    Method m = pinProviderClass.getDeclaredMethod("getPinByAddress", int.class);
                    Object pin = m.invoke(null, pinAddress);
                    return (Pin)pin;
                }
                catch(Exception ex){
                    System.err.println(ex.getMessage());
                }
            }
        }
        return defaultPin;
    }

    /**
     * This utility method searches for "--pull (up|down)", "--l (up|down)", "--up", or "--down" in the command
     * arguments array and returns a PinPullResistance instance based on the option
     * value provided.
     *
     * @param defaultPull default pin pull resistance to apply if no option/argument is found
     * @param args the argument array to search in
     * @return
     */
    public static PinPullResistance getPinPullResistance(PinPullResistance defaultPull, String ... args){

        // search all arguments for the argument option designators
        // we skip the last argument in the array because we expect a value defined after the option designator
        for(int index = 0; index < args.length; index++){

            if(args[index].toLowerCase().equals("--up")){
                return PinPullResistance.PULL_UP;
            }
            else if(args[index].toLowerCase().equals("--down")){
                return PinPullResistance.PULL_DOWN;
            }
            else if(args[index].toLowerCase().equals("--pull") ||
                    args[index].toLowerCase().equals("-l")){
                // if using this option designator, we must have an additional
                // argument in the array to read the value from
                if(index < (args.length-1)){
                    String pull = args[index+1].toLowerCase();
                    if(pull.equals("up")){
                        return PinPullResistance.PULL_UP;
                    }
                    else if(pull.equals("1")){
                        return PinPullResistance.PULL_UP;
                    }
                    else if(pull.equals("down")){
                        return PinPullResistance.PULL_DOWN;
                    }
                    else if(pull.equals("0")){
                        return PinPullResistance.PULL_DOWN;
                    }
                }
            }
        }
        return defaultPull;
    }
}
