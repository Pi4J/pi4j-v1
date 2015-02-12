/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  PiBrellaExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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
import com.pi4j.device.pibrella.Pibrella;
import com.pi4j.device.pibrella.PibrellaLed;
import com.pi4j.device.pibrella.impl.PibrellaDevice;

import java.io.IOException;

public class PiBrellaExample {

    static int cylonSpeed = 100;

    public static void main(String args[]) throws InterruptedException, IOException {

        System.out.println("<--Pi4J--> PiBrella Example ... started.");

        // create the Pi-Face controller
        final Pibrella pibrella = new PibrellaDevice();

        // -----------------------------------------------------------------
        // create a button listener
        // -----------------------------------------------------------------
        pibrella.button().addListener(new SwitchListener() {
            @Override
            public void onStateChange(SwitchStateChangeEvent event) {
                if(event.getNewState() == SwitchState.ON){
                    System.out.println("[BUTTON PRESSED]");
                    cylonSpeed = 30;
                }
                else{
                    System.out.println("[BUTTON RELEASED]");
                    cylonSpeed = 100;
                }
            }
        });

        // run continuously until user aborts with CTRL-C
        while(true) {

            // step up the ladder
            for(int index = PibrellaLed.RED.getIndex(); index <= PibrellaLed.GREEN.getIndex(); index++) {
                pibrella.getLed(index).pulse(cylonSpeed);
                Thread.sleep(cylonSpeed);
            }

            // step down the ladder
            for(int index = PibrellaLed.GREEN.getIndex(); index >= PibrellaLed.RED.getIndex(); index--) {
                pibrella.getLed(index).pulse(cylonSpeed);
                Thread.sleep(cylonSpeed);
            }
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        //gpio.shutdown();  // <-- uncomment if your program terminates
    }
}
