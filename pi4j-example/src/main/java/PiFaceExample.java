

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  PiFaceExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
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


import com.pi4j.component.switches.SwitchListener;
import com.pi4j.component.switches.SwitchState;
import com.pi4j.component.switches.SwitchStateChangeEvent;
import com.pi4j.device.piface.PiFace;
import com.pi4j.device.piface.PiFaceLed;
import com.pi4j.device.piface.PiFaceRelay;
import com.pi4j.device.piface.PiFaceSwitch;
import com.pi4j.device.piface.impl.PiFaceDevice;
import com.pi4j.wiringpi.Spi;

import java.io.IOException;

/**
 * <p>
 * This example code demonstrates how to use the PiFace device interface
 * for GPIO pin state control and monitoring.
 * </p>  
 * 
 * @author Robert Savage
 */
public class PiFaceExample {
    
    static int cylonSpeed = 100;
    
    public static void main(String args[]) throws InterruptedException, IOException {
        
        System.out.println("<--Pi4J--> Pi-Face GPIO Example ... started.");
        
        // create the Pi-Face controller
        final PiFace piface = new PiFaceDevice(PiFace.DEFAULT_ADDRESS, Spi.CHANNEL_0);

        // -----------------------------------------------------------------
        // create a button listener for SWITCH #1
        // -----------------------------------------------------------------
        // -- when switch 'S1' is pressed, relay 'K0' will turn ON
        // -- when switch 'S1' is released, relay 'K0' will turn OFF
        piface.getSwitch(PiFaceSwitch.S1).addListener(new SwitchListener() {
            @Override
            public void onStateChange(SwitchStateChangeEvent event) {
                if(event.getNewState() == SwitchState.ON){
                    System.out.println("[SWITCH S1 PRESSED ] Turn RELAY-K0 <ON>");
                    piface.getRelay(PiFaceRelay.K0).close(); // turn on relay
                }
                else{
                    System.out.println("[SWITCH S1 RELEASED] Turn RELAY-K0 <OFF>");
                    piface.getRelay(PiFaceRelay.K0).open(); // turn off relay
                }
            }
        });

        // -----------------------------------------------------------------
        // create a button listener for SWITCH #2
        // -----------------------------------------------------------------
        // -- when switch 'S2' is pressed, relay 'K1' will toggle states
        piface.getSwitch(PiFaceSwitch.S2).addListener(new SwitchListener() {
            @Override
            public void onStateChange(SwitchStateChangeEvent event) {
                if(event.getNewState() == SwitchState.ON){
                    System.out.println("[SWITCH S2 PRESSED ] Toggle RELAY-K1");
                    piface.getRelay(PiFaceRelay.K1).toggle(); // toggle relay state
                }
                else{
                    System.out.println("[SWITCH S2 RELEASED] do nothing");
                }
            }
        });

        // -----------------------------------------------------------------
        // create a button listener for SWITCH #3
        // -----------------------------------------------------------------
        // -- when switch 'S3' is pressed, LED02 will start blinking
        // -- when switch 'S3' is released, LED02 will stop blinking and turn OFF
        piface.getSwitch(PiFaceSwitch.S3).addListener(new SwitchListener() {
            @Override
            public void onStateChange(SwitchStateChangeEvent event) {
                if(event.getNewState() == SwitchState.ON){
                    System.out.println("[SWITCH S3 PRESSED ] LED02 <BLINK>");
                    piface.getLed(PiFaceLed.LED2).blink(125); // start blinking 8 times per second
                }
                else{
                    System.out.println("[SWITCH S3 RELEASED] LED02 <OFF>");
                    piface.getLed(PiFaceLed.LED2).blink(0); // stop blinking
                    piface.getLed(PiFaceLed.LED2).off();    // turn off led
                }
            }
        });

        // -----------------------------------------------------------------
        // create a button listener for SWITCH #4
        // -----------------------------------------------------------------
        // -- when switch 'S4' is pressed, the cylon effect on LED03-LED07 will speed up
        // -- when switch 'S4' is pressed, the cylon effect on LED03-LED07 will slow down
        piface.getSwitch(PiFaceSwitch.S4).addListener(new SwitchListener() {
            @Override
            public void onStateChange(SwitchStateChangeEvent event) {
                if(event.getNewState() == SwitchState.ON){
                    System.out.println("[SWITCH S4 PRESSED ] CYLON <FAST>");;
                    cylonSpeed = 30;
                }
                else{
                    System.out.println("[SWITCH S4 RELEASED] CYLON <SLOW>");
                    cylonSpeed = 100;
                }
            }
        });

        // run continuously until user aborts with CTRL-C
        while(true) {
                        
            // step up the ladder
            for(int index = PiFaceLed.LED3.getIndex(); index <= PiFaceLed.LED7.getIndex(); index++) {
                piface.getLed(index).pulse(cylonSpeed);
                Thread.sleep(cylonSpeed);
            }
            
            // step down the ladder
            for(int index = PiFaceLed.LED7.getIndex(); index >= PiFaceLed.LED3.getIndex(); index--) {
                piface.getLed(index).pulse(cylonSpeed);
                Thread.sleep(cylonSpeed);
            }
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        //gpio.shutdown();  // <-- uncomment if your program terminates
    }
}

