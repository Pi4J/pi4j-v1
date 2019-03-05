package com.pi4j.util;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  CommandArgumentParser.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinProvider;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.serial.*;

import java.lang.reflect.Method;

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
     * @return GPIO pin instance
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
     * This utility method searches for "--pull (up|down|off)", "--l (up|down|off)", "--up", or "--down" in the command
     * arguments array and returns a PinPullResistance instance based on the option
     * value provided.
     *
     * @param defaultPull default pin pull resistance to apply if no option/argument is found
     * @param args the argument array to search in
     * @return pill pull resistance enum
     */
    public static PinPullResistance getPinPullResistance(PinPullResistance defaultPull, String ... args){

        // search all arguments for the argument option designators
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
                    else{
                        return PinPullResistance.OFF;
                    }
                }
            }
        }
        return defaultPull;
    }

    /**
     * This utility method searches for the following options:
     *
     *   "--device (device-path)"                   [DEFAULT: /dev/ttyAMA0]
     *   "--baud (baud-rate)"                       [DEFAULT: 38400]
     *   "--data-bits (5|6|7|8)"                    [DEFAULT: 8]
     *   "--parity (none|odd|even)"                 [DEFAULT: none]
     *   "--stop-bits (1|2)"                        [DEFAULT: 1]
     *   "--flow-control (none|hardware|software)"  [DEFAULT: none]
     *
     * in the arguments array and returns a SerialConfig instance based on the option
     * values detected.
     *
     * @param defaultConfig default serial configuration to apply if no option/arguments are found
     * @param args the argument array to search in
     * @return serial config object
     */
    public static SerialConfig getSerialConfig(SerialConfig defaultConfig, String ... args){

        // search all arguments for the argument option designators
        // we skip the last argument in the array because we expect a value defined after each option designator
        for(int index = 0; index < (args.length-1); index++){

            // "--device (device-path)"                   [DEFAULT: /dev/ttyAMA0]
            if(args[index].toLowerCase().equals("--device")){
                defaultConfig.device(args[index+1]);
                index++;
                continue;
            }

            // "--baud (baud-rate)"                       [DEFAULT: 38400]
            if(args[index].toLowerCase().equals("--baud")){
                defaultConfig.baud(Baud.getInstance(Integer.parseInt(args[index + 1])));
                index++;
                continue;
            }

            // "--data-bits (5|6|7|8)"                    [DEFAULT: 8]
            if(args[index].toLowerCase().equals("--data-bits")){
                defaultConfig.dataBits(DataBits.getInstance(Integer.parseInt(args[index + 1])));
                index++;
                continue;
            }

            // "--parity (none|odd|even)"                 [DEFAULT: none]
            if(args[index].toLowerCase().equals("--parity")){
                defaultConfig.parity(Parity.getInstance(args[index + 1]));
                index++;
                continue;
            }

            // "--stop-bits (1|2)"                        [DEFAULT: 1]
            if(args[index].toLowerCase().equals("--stop-bits")){
                defaultConfig.stopBits(StopBits.getInstance(Integer.parseInt(args[index + 1])));
                index++;
                continue;
            }

            // "--flow-control (none|hardware|software)"  [DEFAULT: none]
            if(args[index].toLowerCase().equals("--flow-control")){
                defaultConfig.flowControl(FlowControl.getInstance(args[index + 1]));
                index++;
                continue;
            }
        }
        return defaultConfig;
    }
}
