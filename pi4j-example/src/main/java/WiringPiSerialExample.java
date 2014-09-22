
/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  WiringPiSerialExample.java  
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
import com.pi4j.wiringpi.Serial;

public class WiringPiSerialExample {
    
    public static void main(String args[]) throws InterruptedException {
        
        System.out.println("<--Pi4J--> SERIAL test program");

        // open serial port for communication
        int fd = Serial.serialOpen(Serial.DEFAULT_COM_PORT, 38400);
        if (fd == -1) {
            System.out.println(" ==>> SERIAL SETUP FAILED");
            return;
        }

        // infinite loop
        while(true) {
            
            // send test ASCII message
            Serial.serialPuts(fd, "TEST\r\n");

            // display data received to console
            int dataavail = Serial.serialDataAvail(fd);            
            while(dataavail > 0) {
                int data = Serial.serialGetchar(fd);
                System.out.print((char)data);                
                dataavail = Serial.serialDataAvail(fd);
            }
            
            // wash, rinse, repeat
            Thread.sleep(1000);
        }
    }
}
