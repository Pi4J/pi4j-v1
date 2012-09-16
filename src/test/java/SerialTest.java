/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library
 * FILENAME      :  SerialTest.java  
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
import java.util.Date;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;

public class SerialTest 
{
    
    public static void main(String args[]) throws InterruptedException
    {
        SerialTestListener listener = new SerialTestListener();
        Serial serial = SerialFactory.createInstance();
        serial.addListener(listener);
        
        System.out.println("<--Pi4J--> SERIAL test program");

        int fd = serial.open(Serial.DEFAULT_COM_PORT, 38400);
        if (fd == -1)
        {
            System.out.println(" ==>> SERIAL SETUP FAILED");
            return;
        }
        
        while(true)
        {
            serial.write("CURRENT TIME: %s", new Date().toString());
            serial.write((byte)13);
            serial.write((byte)10);
            serial.write('|');
            serial.write((byte)13);
            serial.write((byte)10);

            Thread.sleep(1000);
            serial.removeListener(listener);
        }
    }


}

class SerialTestListener implements SerialDataListener
{
    public void dataReceived(SerialDataEvent event)
    {
        // print out the data received to the console 
        System.out.print(event.getData());        
    }
}
