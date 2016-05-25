/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  IFTTTMakerChannelTriggerExample.java
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

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.IFTTTMakerChannelTriggerEvent;
import com.pi4j.io.gpio.event.IFTTTMakerChannelTriggerListener;
import com.pi4j.io.gpio.trigger.IFTTTMakerChannelTrigger;
import com.pi4j.util.Console;

/**
 * This example code demonstrates how to listen to a GPIO input pin
 * on the Raspberry Pi and use the IFTTT Maker Channel trigger to
 * send a triggered event to the IFTTT web service API.
 *
 * @author Robert Savage
 */
public class IFTTTMakerChannelTriggerExample {

    // TODO: ADD YOU IFTTT MAKER CHANNEL API KEY HERE!
    // (You can get this API KEY when you add the Maker Channel to your IFTTT account.)
    public static String IFTTT_MAKER_CHANNEL_API_KEY = "lyekJ_ZnpzXIMYpZiPaeXzMzpqGqX0KKLGWCwJYevgx";

    // TODO: ADD YOU IFTTT MAKER CHANNEL TRIGGER EVENT NAME HERE!
    // (You define the event name when you create an IFTTT recipe with Maker Channel as the recipe trigger)
    public static String IFTTT_MAKER_CHANNEL_EVENT_NAME = "pi4j";

    /**
     * @param args --none-
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate code)
        final Console console = new Console();

        // print program title/header
        console.title("<-- The Pi4J Project -->", "IFTTT Maker Channel Trigger Example");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin as an input pin
        final GpioPinDigitalInput input = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00,
                "MyInputPin", PinPullResistance.PULL_DOWN);

        // provide 500ms of pin debounce protection
        input.setDebounce(500);;

        // -----------------------------------------
        // Add the IFTTT Maker Channel Trigger
        // (This is where the magic happens)
        // -----------------------------------------
        input.addTrigger(new IFTTTMakerChannelTrigger(
                IFTTT_MAKER_CHANNEL_API_KEY,     // <<-- PROVIDE YOUR ACCOUNT SPECIFIC IFTTT MAKER CHANNEL API KEY
                IFTTT_MAKER_CHANNEL_EVENT_NAME,  // <<-- PROVIDE THE IFTTT MAKER CHANNEL EVENT NAME (defined in your IFTTTT recipe)
                PinState.HIGH,                   // <<-- OPTIONALLY DEFINE A SPECIFIC STATE TO TRIGGER ON

                // OPTIONALLY REGISTER A TRIGGER CALLBACK LISTENER
                // (Note: this callback parameter is not required for basic functionality)
                new IFTTTMakerChannelTriggerListener() {
                    @Override
                    public boolean onTriggered(IFTTTMakerChannelTriggerEvent event) {

                        // The IFTTT Maker Channel API accepts three value parameters (value1, value2, and value3)
                        // By default, Pi4J applies the following values to each:
                        //
                        // "value1" = {pin-name}
                        // "value2" = {pin-state-value} (as an Integer; 0==LOW, 1==HIGH)
                        // "value3" = {json-payload}  Example:
                        // {
                        //    "pin": {
                        //        "name": "MyInputPin",
                        //        "address": "0",
                        //        "provider": "RaspberryPi GPIO Provider",
                        //        "mode": "input",
                        //        "direction": "IN",
                        //        "pull": "down"
                        //    },
                        //    "state": {
                        //        "name": "HIGH",
                        //        "value": "1",
                        //        "is-high": "true",
                        //        "is-low": "false"
                        //    },
                        //    "timestamp": "2016-04-15T17:32:49.666-0400"
                        //}
                        //
                        // However, you can override any of these defaults in your callback listener by
                        // applying new string values via the 'setValueX()' methods on the event object.
                        //
                        // Example:
                        if(event.getValue2().equals("1")){
                            event.setValue2("ON");
                        } else{
                            event.setValue2("OFF");
                        }

                        // display event trigger details on screen
                        console.println(" --> IFTTT MAKER CHANNEL EVENT TRIGGER");
                        console.println("     - GPIO PIN            : " + event.getPin());
                        console.println("     - PIN STATE           : " + event.getState());
                        console.println("     - IFTTT EVENT NAME    : " + event.getEventName());
                        console.println("     - IFTTT EVENT VALUE 1 : " + event.getValue1());
                        console.println("     - IFTTT EVENT VALUE 2 : " + event.getValue2());
                        console.println("     - IFTTT EVENT VALUE 3 : " + event.getValue3());
                        console.emptyLine();

                        // MAKE SURE TO RETURN 'true' TO CONTINUE WITH THE IFTTT MAKER CHANNEL API CALL
                        // (you can optionally return 'false' if you want to abort the IFTTT API call)
                        return true;
                    }
        }));

        // set shutdown state for this pin: unexport the pin
        input.setShutdownOptions(true);

        // prompt user that we are ready
        console.println("Successfully provisioned [" + input + "] with PULL resistance = [" + input.getPullResistance() + "]");
        console.emptyLine();

        // wait for user to exit by pressing CTRL-C
        console.waitForExit();

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
    }
}
