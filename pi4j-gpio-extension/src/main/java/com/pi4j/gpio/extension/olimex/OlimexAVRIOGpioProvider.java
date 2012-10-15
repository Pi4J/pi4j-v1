package com.pi4j.gpio.extension.olimex;

import com.pi4j.gpio.extension.serial.SerialCommandQueueProcessingThread;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.PinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.gpio.impl.GpioProviderBase;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  OlimexAVRIOGpioProvider.java  
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


public class OlimexAVRIOGpioProvider extends GpioProviderBase implements GpioProvider
{
    public static final String NAME = "com.pi4j.gpio.extension.olimex.OlimexAVRIOGpioProvider";
    public static final String DESCRIPTION = "Olimex AVR-IO GPIO Provider";
    private Serial com;
    private int currentStates = 0;
    private SerialCommandQueueProcessingThread queue;

    public OlimexAVRIOGpioProvider(String serialDevice)
    {
        com = SerialFactory.createInstance();
        
        // create serial data listener
        SerialExampleListener listener = new SerialExampleListener();

        // add/register the serial data listener
        com.addListener(listener);
        
        com.open(serialDevice, 19200);
        
        // create and start the serial command processing queue thread
        // set the delay time to 100 ms; this works well for the AVR-IO
        queue = new SerialCommandQueueProcessingThread(com, 50);
        queue.start();
        queue.put("?"); // query for current status
    }
    
    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public boolean hasPin(Pin pin)
    {
        return (pin.getProvider() == OlimexAVRIOGpioProvider.NAME);
    }

    @Override
    public void export(Pin pin, PinMode mode)
    {
        // NOT SUPPORTED        
    }

    @Override
    public boolean isExported(Pin pin)
    {
        // NOT SUPPORTED        
        return false;
    }

    @Override
    public void unexport(Pin pin)
    {
        // NOT SUPPORTED        
    }

    @Override
    public void setMode(Pin pin, PinMode mode)
    {
        // ALL PIN MODES ARE PREDEFINED        
    }

    @Override
    public PinMode getMode(Pin pin)
    {
        // return first mode found; this device has singular fixed pin modes
        for(PinMode mode : pin.getSupportedPinModes())
            return mode;
        
        return null;
    }

    @Override
    public void setPullResistance(Pin pin, PinPullResistance resistance)
    {
        // NOT SUPPORTED        
    }

    @Override
    public PinPullResistance getPullResistance(Pin pin)
    {
        // NOT SUPPORTED        
        return null;
    }

    @Override
    public void setValue(Pin pin, int value)
    {
        // NOT SUPPORTED        
    }

    @Override
    public int getValue(Pin pin)
    {
        // NOT SUPPORTED        
        return -1;
    }

    @Override
    public void setPwm(Pin pin, int value)
    {
        // NOT SUPPORTED        
    }

    @Override
    public int getPwm(Pin pin)
    {
        // NOT SUPPORTED        
        return -1;
    }
    
    @Override
    public void setState(Pin pin, PinState state)
    {
        // turn ON/OFF relay pins
        if(state == PinState.HIGH)
            queue.put("+" + pin.getAddress());
        else
            queue.put("-" + pin.getAddress());
    }

    @Override
    public PinState getState(Pin pin)
    {
        int bit = (int)Math.pow(2, (pin.getAddress()-1));
        int state = (currentStates & bit);
        return (state == bit) ? PinState.HIGH : PinState.LOW;
    }    
    
    
    /**
     * This class implements the serial data listener interface with the callback method for event
     * notifications when data is received on the serial port.
     * 
     * @see SerialDataListener
     * @author Robert Savage
     */
    class SerialExampleListener implements SerialDataListener
    {
        private StringBuilder buffer = new StringBuilder();
        
        public void dataReceived(SerialDataEvent event)
        {
           String data = event.getData();
           
           // append received data into buffer
           if(data != null && !data.isEmpty())
               buffer.append(data);
           
           int start = buffer.indexOf("$");
           int stop = buffer.indexOf("\n");
           
           while(stop >= 0)
           {
               // process data buffer
               if(start >= 0 && stop > start)
               {
                   // get command
                   String command = buffer.substring(start, stop+1);
                   buffer.delete(start, stop+1).toString();
    
                   // remove terminating characters
                   command = command.replace("$", "");
                   command = command.replace("\n", "");
                   command = command.replace("\r", "");
                   
                   // print out the data received to the console
                   //System.out.println("<<< COM RX >>> " + command);
                   
                   int value = Integer.parseInt(command, 16);
                   
                   // process each INPUT pin for changes; 
                   // dispatch change events if needed
                   for(Pin pin : OlimexAVRIOPin.INPUTS)
                       evaluatePinForChange(pin, value);
                   
                   // update the current value tracking variable
                   currentStates = value;
               }
               else if(stop >= 0)
               {
                   // invalid data command; purge
                   buffer.delete(0, stop+1);
                   
                   //System.out.println("PURGE >>> " + removed);
               }
               
               // seek to next command in buffer
               start = buffer.indexOf("$");
               stop = buffer.indexOf("\n");               
           }
        }
        
        
        private void evaluatePinForChange(Pin pin, int value)
        {
            int bit = (int)Math.pow(2, (pin.getAddress()-1));
            if((value & bit) != (currentStates & bit))
            {
                // change detected for INPUT PIN
                //System.out.println("<<< CHANGE >>> " + pin.getName());
                dispatchPinChangeEvent(pin.getAddress(), ((value & bit) == bit) ? PinState.HIGH : PinState.LOW);
            }
        }
        
        
        private void dispatchPinChangeEvent(int pinAddress, PinState state)
        {
            // iterate over the pin listeners map
            for(Pin pin : listeners.keySet())
            {
                //System.out.println("<<< DISPATCH >>> " + pin.getName() + " : " + state.getName());
                
                // dispatch this event to the listener 
                // if a matching pin address is found
                if(pin.getAddress() == pinAddress)
                {
                    // dispatch this event to all listener handlers
                    for(PinListener listener : listeners.get(pin))
                    {
                        listener.handlePinEvent(new PinDigitalStateChangeEvent(this, pin, state));
                    }
                }            
            }            
        }
    }      
}
