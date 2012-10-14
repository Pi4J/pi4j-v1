package com.pi4j.example;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  I2CWiiMotionPlusExample.java  
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.impl.I2CBusImpl;


public class I2CWiiMotionPlusExample {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Starting:");
        
        I2CBus bus = I2CBusImpl.getBus(0);
        
        WiiMotionPlus wiiMotionPlus = new WiiMotionPlus(bus);
        wiiMotionPlus.init();
        
        int iteration = 0;
        
        makeBackup("log.txt");
        
        FileWriter logFile = new FileWriter("log.txt");
        BufferedWriter bw = new BufferedWriter(logFile, 2048);
        PrintWriter log = new PrintWriter(bw);
        
        try {
            while (true) {
                long now = System.currentTimeMillis();
                ThreeAxis threeAxis = wiiMotionPlus.read();
                long lasted = System.currentTimeMillis() - now;
                
                System.out.print(formatInt(iteration));
                System.out.print(' ');
                
                System.out.print(formatLong(lasted));
                System.out.print(' ');
                
                System.out.print(formatInt(threeAxis.x));
                System.out.print(' ');
    
                System.out.print(formatInt(threeAxis.y));
                System.out.print(' ');
    
                System.out.print(formatInt(threeAxis.z));
                System.out.print(' ');
    
                // System.out.print('\r');
                System.out.println();
                
                log.println(formatInt(iteration) + "," + formatLong(lasted) + "," + formatInt(threeAxis.x) + "," + formatInt(threeAxis.y) + "," + formatInt(threeAxis.z));
                //log.flush();
                
                Thread.sleep(500);
                iteration = iteration + 1;
            }
        } finally {
            bw.flush();
            bw.close();
            logFile.close();
        }
    }
    
    public static void makeBackup(String filename) {
        int i = 1;
        File f = new File(filename + "." + i);
        while (f.exists()) {
            i = i + 1;
            f = new File(filename + "." + i);
        }
        for (int j = i; j >= 1; j--) {
            File to = new File(filename + "." + j);
            File from = new File(filename + "." + (j - 1));
            if (j == 1) {
                from = new File(filename);
            }
            from.renameTo(to);
        }
    }
    
    public static String formatInt(int i) {
        String x = "         " + Integer.toString(i);
        x = x.substring(x.length() - 6, x.length());
        return x;
    }
    
    public static String formatLong(long i) {
        String x = "         " + Long.toString(i);
        x = x.substring(x.length() - 6, x.length());
        return x;
    }
    
    public static class WiiMotionPlus {
        
        private I2CDevice initDevice;
        private I2CDevice device;
        
        public WiiMotionPlus(I2CBus bus) throws IOException {
            initDevice = bus.getDevice(0x53);
            device = bus.getDevice(0x52);
        }
        
        public void init() {
            try {
                initDevice.write(0xfe, (byte)0x04);
            } catch (IOException ignore) {
                ignore.printStackTrace();
            }
        }
        
        public ThreeAxis read() throws IOException {
            byte[] buf = new byte[256];
            int res = device.read(0, buf, 0, 6);

            if (res != 6) {
                throw new RuntimeException("Read failure - got only " + res + " bytes from WiiMotionPlus");
            }

            
            ThreeAxis ret = new ThreeAxis();
            
            ret.x = asInt(buf[0]);
            ret.y = asInt(buf[1]);
            ret.z = asInt(buf[2]);
            ret.x = ret.x | (((asInt(buf[3]) & 0xfc) >> 2) * 256);
            ret.y = ret.y | (((asInt(buf[4]) & 0xfc) >> 2) * 256);
            ret.z = ret.z | (((asInt(buf[5]) & 0xfc) >> 2) * 256);

            return ret;
        }

        private int asInt(byte b) {
            int i = b;
            if (i < 0) { i = i + 256; }
            return i;
        }
    
    }
    
    public static class ThreeAxis {
        
        public int x;
        public int y;
        public int z;
    }    
}
