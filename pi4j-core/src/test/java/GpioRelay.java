/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioRelay.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
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
import com.pi4j.io.gpio.event.GpioListener;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinEvent;
import com.pi4j.io.gpio.event.PinEventType;

public class GpioRelay
{
    public static void main(String args[]) throws InterruptedException
    {
        System.out.println("<--Pi4J--> GPIO test program");
        
        // create gpio listener & callback handler
//      GpioRelayListener listener = new GpioRelayListener();

//        // create gpio trigger
//        GpioSetStateTrigger triggerOn = new GpioSetStateTrigger(Pin.GPIO_04, PinState.HIGH, Pin.GPIO_25, PinState.LOW);
//        GpioSetStateTrigger triggerOff = new GpioSetStateTrigger(Pin.GPIO_04, PinState.LOW, Pin.GPIO_25, PinState.HIGH);
//
//        // create gpio controller
//        Gpio gpio = GpioFactory.createInstance();
//        
//        // add gpio listener
//        gpio.addListener(listener);
//        
//        // add gpio triggers
//        gpio.addTrigger(triggerOn);
//        gpio.addTrigger(triggerOff);
//        
//        // setup gpio pin #4 as an input pin whose biased to ground and receives +3VDC to be triggered
//        gpio.setup(Pin.GPIO_04, PinDirection.IN, PinEdge.BOTH, PinResistor.PULL_DOWN);
//
//        // setup gpio pin #25 as an output pin  
//        gpio.setup(Pin.GPIO_25, PinDirection.OUT);
//        //gpio.setPullResistor(GpioPin.GPIO_25, GpioPinResistor.PULL_DOWN);
//        gpio.setState(Pin.GPIO_25, PinState.HIGH);

        // keep program alive
        for(;;)
        {
            Thread.sleep(500);
        }
    }}

class GpioRelayListener implements GpioListener
{
    @Override
    public void handleGpioPinEvent(GpioPinEvent event)
    {
        if(event.getEventType() == PinEventType.DIGITAL_STATE_CHANGE)
        {
            // cast to digital state change event
            GpioPinDigitalStateChangeEvent evt = (GpioPinDigitalStateChangeEvent)event;
            
            // display pin state on console
            System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = "
                    + evt.getState());
        }
    }     
}
