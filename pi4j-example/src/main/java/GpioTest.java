/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  GpioTest.java
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

import com.pi4j.io.gpio.*;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This example code demonstrates how to perform simple state
 * control of a GPIO pin on the BananaPi.
 *
 * @author Robert Savage
 */
public class GpioTest {

    private static boolean exit = false;

    public static void main(String[] args) throws InterruptedException, PlatformAlreadyAssignedException, IOException {

        // ####################################################################
        //
        // since we are not using the default Raspberry Pi platform, we should
        // explicitly assign the platform as the BananaPro platform.
        //
        // ####################################################################
        PlatformManager.setPlatform(Platform.BANANAPRO);

        final Scanner in = new Scanner(System.in);

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        System.out.println("<--Pi4J--> GPIO Test Program ... started.");

        while(!exit) {
            System.out.println();
            System.out.println();
            System.out.println("----------------------------------------");
            System.out.println("SELECT TEST:");
            System.out.println("----------------------------------------");
            System.out.println(" ( O ) GPIO OUTPUTS");
            System.out.println(" ( I ) GPIO INPUTS");
            System.out.println(" ( X ) EXIT");
            System.out.println("----------------------------------------");

            // get user input selection
            String selection = in.next();

            switch(selection.toUpperCase()){
                case "O":{
                    gpioOutputsMenu(); // GPIO OUTPUTS
                    break;
                }
                case "I":{
                    break;
                }
                case "X":{
                    exit = true;
                    break;
                }
                default: {
                    System.err.println("Invalid Entry, Try Again!");
                    break;
                }
            }
        }

        //close scanner stream
        in.close();

        System.out.println();
        System.out.println();
        System.out.println("Goodbye!");
        System.out.println();
        System.out.println();

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
    }


    public static void gpioOutputsMenu(){

        System.out.println();
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("GPIO OUTPUTS TEST:");
        System.out.println("----------------------------------------");

        switch(PlatformManager.getPlatform()){
            case RASPBERRYPI:{
                Pin pins[] = RaspiPin.allPins();
                Arrays.sort(pins);
                gpioOutputsTest(pins);
                break;
            }
            case BANANAPI:{
                Pin pins[] = BananaPiPin.allPins();
                Arrays.sort(pins);
                gpioOutputsTest(pins);
                break;
            }
            case BANANAPRO:{
                Pin pins[] = BananaProPin.allPins();
                Arrays.sort(pins);
                gpioOutputsTest(pins);
                break;
            }
			case BPI:{
                Pin pins[] = BpiPin.allPins();
                Arrays.sort(pins);
                gpioOutputsTest(pins);
                break;
			}
			case NANOPI:{
                Pin pins[] = NanoPiPin.allPins();
                Arrays.sort(pins);
                gpioOutputsTest(pins);
                break;
			}
			case ODROID:{
                Pin pins[] = OdroidC1Pin.allPins();
                Arrays.sort(pins);
                gpioOutputsTest(pins);
                break;
			}
			case ORANGEPI:{
                Pin pins[] = OrangePiPin.allPins();
                Arrays.sort(pins);
                gpioOutputsTest(pins);
                break;
			}
			default:
				break;
	        }
    }

    public static void gpioOutputsTest(Pin pins[]){
        final Scanner in = new Scanner(System.in);
        final GpioController gpio = GpioFactory.getInstance();
        for(Pin pin : pins){
            // provision pin
            System.out.println("... provisioning pin: " + pin.toString());
            GpioPinDigitalOutput outputPin = gpio.provisionDigitalOutputPin(pin, PinState.LOW);

            System.out.println(">>> pin: " + pin.toString() + " should be in the LOW state.");

            // wait for user input
            System.out.println("... press ENTER to continue ...");
            in.next();

            // set pin to HIGH state
            outputPin.high();
            System.out.println(">>> pin: " + pin.toString() + " should be in the HIGH state.");

            // wait for user input
            System.out.println("... press ENTER to continue ...");
            in.next();

            // set pin to LOW state
            outputPin.toggle();
            System.out.println(">>> pin: " + pin.toString() + " should be in the LOW state.");

            // wait for user input
            System.out.println("... press ENTER to continue ...");
            in.next();

            // set pin to blink
            outputPin.blink(1000);
            System.out.println(">>> pin: " + pin.toString() + " should be blinking 1 time per second.");

            // wait for user input
            System.out.println("... press ENTER to continue ...");
            in.next();

            // stop blinking
            outputPin.blink(0);

            // un-provision pin
            System.out.println("... un-provisioning pin: " + pin.toString());
            gpio.unprovisionPin(outputPin);
        }

        //close scanner stream
        in.close();
    }
}
