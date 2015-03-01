/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: I2C Device Abstractions
 * FILENAME      :  TestADXL345.java  
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
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;


public class TestADXL345 {

    public static void main(String[] args) throws Exception {
    
        I2CBus bus = I2CFactory.getInstance(1);
        I2CDevice adxlDevice = bus.getDevice(0x53);

        adxlDevice.write(0x31, (byte)0x0b); // Initialize
        
        long now = System.currentTimeMillis();
        
        int measurement = 0;
        
        adxlDevice.write(0x2D, (byte)0x08); // Triger measuring
        while (System.currentTimeMillis() - now < 10000) {

            byte[] data = new byte[6];
            
            adxlDevice.read(0x32, data, 0, 6);
            
            int x = ((data[0] & 0x1f) << 8) + (data[1] & 0xff);
            if ((data[0] & 0x80) == 1) { x = -x; }
            int y = ((data[0] & 0x1f) << 8) + (data[1] & 0xff);
            if ((data[0] & 0x80) == 1) { y = -y; }
            int z = ((data[0] & 0x1f) << 8) + (data[1] & 0xff);
            if ((data[0] & 0x80) == 1) { z = -z; }

            
            String sm = toString(measurement, 3);
            
            String sx = toString(x, 7);
            String sy = toString(y, 7);
            String sz = toString(y, 7);
            
            System.out.print(sm + sx + sy + sz);
            for (int i = 0; i < 24; i++) { System.out.print((char)8); }
            
            Thread.sleep(100);
            
            measurement++;
        }
        
    }
    
    public static String toString(int i, int l) {
        String s = Integer.toString(i);
        return "        ".substring(0, l - s.length()) + s;
    }

    
}
