/*
 * Copyright 2014 Pi4J.
 *
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
 */

package com.pi4j.device.sensor.distance.i2c;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  Srf02.java  
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

import com.pi4j.component.sensor.DistanceSensorBase;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.impl.I2CDeviceImpl;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andy
 */
public class Srf02 extends DistanceSensorBase{
    private static final byte kCommandRegister         = 0x00;
    private static final byte kRangeHighByte           = 0x02;
    private static final byte kRangeLowByte            = 0x03;
    private static final byte kRangeInchesCommand      = 0x50;
    private static final byte kRangeCentimetersCommand = 0x51;
    private byte[]            buffer; 
    
    
    
    
    
    
    private int devAddr = 0x70;
    I2CDeviceImpl dev = null;
    I2CBus bus = null;
    private static final int busNum = 1;
    
    public Srf02(){
       
        init();
    }
      public Srf02(int devAddr){
        this.devAddr = devAddr;
        init();
    }
    
    public void init(){
        try {
           dev =(I2CDeviceImpl)  I2CFactory.getInstance(busNum).getDevice(devAddr);
           buffer = new byte[10];
        } catch (IOException ex) {
            Logger.getLogger(Srf02.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }
    
    @Override
    public double getValue() {
          short result  = 0;
        try {
            boolean bSuccess = false;
            
            
//        m_i2c.read(kCommandRegister, 1, buffer);
//        System.out.println("Buffer is :" + buffer[0]);
//        result = buffer[0];
            
            dev.write(kCommandRegister, kRangeCentimetersCommand);
            
            while (!bSuccess) {
                Thread.sleep(80);
                
           
                try{
                dev.read(kCommandRegister,buffer,0, 4);
                }catch(java.io.IOException ioe){
                    System.out.println("looping");
                    continue;
                }
                //System.out.println("Return array: " + buffer.toString());
                
                if (buffer[0] != 0xFF) {
                    bSuccess = true;
                } else {
                    try {
                        Thread.currentThread().sleep(10);    // takes up to 66ms after you initiate ranging so slow loop down
                    } catch (InterruptedException ie) {
                        
                        // don't have to actually do anything with the exception except leave loop maybe
                        break;
                    }
                }
            }
            
            if(bSuccess){
//                System.out.println("Original buffer value : " +  buffer[3]);
           //     System.out.println("Original highbite : " + (short) buffer[2]);
//                System.out.println("Without the multiplication : " + ((long)buffer[3]& 0xffffL));
//                result =  ((short)buffer[3]&  ) ;
                result = (short)((buffer[2] >> 8) + (buffer[3]&0xFF));
                System.out.println("Result inside : " + result);
            //    result =  (result + buffer[3]);
                
                
                
                // if lowByte and highByte aren't actually being populated by that single 4 byte read use next 4 comment lines instead
//            m_i2c.read(kRangeHighByte, 1, buffer);  
//            result = buffer[0] * 256;
//            m_i2c.read(kRangeLowByte, 1, buffer);
//            result = result + buffer[0];
                
            }
           
        } catch (IOException ex) {
          //  Logger.getLogger(Srf02.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            return result;
        }
    }
    
    public void setNewAddress(int newAddress){
        try {
            //0xA0, 0xAA, 0xA5, 0xF2
            dev.write(kCommandRegister, (byte) 0xA0);
            dev.write(kCommandRegister, (byte) 0xAA);
            dev.write(kCommandRegister, (byte) 0xA5);
            dev.write(kCommandRegister, (byte) newAddress);
            this.devAddr = newAddress;
            init();
            
        } catch (IOException ex) {
            Logger.getLogger(Srf02.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
