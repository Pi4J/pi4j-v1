package com.pi4j.i2c.devices;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: I2C Device Abstractions
 * FILENAME      :  BMP085.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class BMP085 {

    private I2CDevice device;
    private int ac1;
    private int ac2;
    private int ac3;
    private int ac4;
    private int ac5;
    private int ac6;
    private int b1;
    private int b2;
    private long b5;
    @SuppressWarnings("unused")
    private int mb;
    private int mc;
    private int md;
    private int oss;
        
    public BMP085(I2CBus bus) throws IOException {
        device = bus.getDevice(0x77);
    }
        
    public void init() {
        try {
            byte[] eepromData = new byte[22];
            device.read(0xAA, eepromData, 0, 22);
            ac1 = readShort(eepromData, 0);
            ac2 = readShort(eepromData, 2);
            ac3 = readShort(eepromData, 4);
            ac4 = readUShort(eepromData, 6);
            ac5 = readUShort(eepromData, 8);
            ac6 = readUShort(eepromData, 10);
            b1 = readShort(eepromData, 12);
            b2 = readShort(eepromData, 14);
            mb = readShort(eepromData, 16);
            mc = readShort(eepromData, 18);
            md = readShort(eepromData, 20);
        } catch (IOException ignore) {
            ignore.printStackTrace();
        }
    }

    public void startTemperatureRead() throws IOException {
        device.write(0xf4, (byte)0x2e);
    }

    public int readUncompensatedTemperature() throws IOException {
        byte[] t = new byte[2];
        int r = device.read(0xf6, t, 0, 2);
        if (r != 2) {
            throw new IOException("Cannot read temperature; r=" + r);
        }
        int ut = readShort(t, 0);
        return ut;
    }

    public int calculateTemperature(int ut) {
        long x1 = (ut - ac6) * ac5;
        x1 = x1 >>> 15;
        long x2 = (mc << 11) / (x1 + md);
        b5 = x1 + x2;
        int t = (int)((b5 + 8) >>> 4);
        return t;
    }

    public int readTemperature() throws IOException {
        startTemperatureRead();
        try {
            Thread.sleep(5);
        } catch (InterruptedException ignore) { }
        int ut = readUncompensatedTemperature();
        int t = calculateTemperature(ut);
        return t;
    }

    public void startPressureRead() throws IOException {
        device.write(0xf4, (byte)(0x34 + (oss << 6)));
    }

    public int readUncompensatedPressure() throws IOException {
        byte[] p = new byte[3];
        int r = device.read(0xf6, p, 0, 3);
        if (r != 3) {
            throw new IOException("Cannot read pressure; r=" + r);
        }

        int up = ((p[0] & 0xff) << 16) + ((p[1] & 0xff) << 8) +(p[2] & 0xff) >> (8 - oss); 
        return up;
    }

    public int calculatePressure(int up) {
        System.out.println("up5=" + up);
        System.out.println("bp5=" + b5);

        long p = 0;
        long b6 = b5 - 4000;
        System.out.println("bp6=" + b6);

        long x1 = (b2 * ((b6 * b6) >> 12)) >> 11;
        System.out.println("x1=" + x1);

        long x2 = (ac2 * b6) >> 11;
        System.out.println("x2=" + x2);

        long x3 = x1 + x2;
        System.out.println("x3=" + x3);

        long b3 = (((ac1 * 4 + x3) << oss) + 2) >> 2;
        System.out.println("b3=" + b3);

        x1 = (ac3 * b6) >> 13;  
        System.out.println("x1=" + x1);

        x2 = (b1 * ((b6 * b6) >> 12)) >> 16; 
        System.out.println("x3=" + x2);

        x3 = ((x1 + x2) + 2) >> 2;
        System.out.println("x2=" + x3);

        long b4 = (ac4 * (x3 + 32768)) >> 15;
        System.out.println("b4=" + b4);

        long b7 = (up - b3) * (50000 >> oss);
        System.out.println("b7=" + b7);

        if (b7 < 0x80000000) {
            p = (b7 * 2) / b4;
        } else {
            p = (b7 / b4) * 2;
        }
        System.out.println("p=" + p);

        x1 = (p >> 8) * (p >> 8);
        System.out.println("x1=" + x1);

        x1 = (x1 * 3038) >> 16;
        System.out.println("x1=" + x1);

        x2 = (-7357 * p) / 65536;
        System.out.println("x2=" + x2);

        p = p + ((x1 + x2 + 3791) >> 4);
        System.out.println("p=" + p);

        return (int)p;
   }

    public int readPressure() throws IOException {
        startPressureRead();
        try {
            Thread.sleep(5);
        } catch (InterruptedException ignore) { }
        int up = readUncompensatedPressure();
        int p = calculatePressure(up);
        return p;
    }



    public int readShort(byte[] data, int a) {
        int r = (data[a] * 256) + (data[a + 1] & 0xff);
        return r;
    }

    public int readUShort(byte[] data, int a) {
        int r = ((data[a] & 0xff) << 8) + (data[a + 1] & 0xff);
        return r;
    }


    public static void main(String[] args) throws Exception {
        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_0);

        BMP085 bmp085 = new BMP085(bus);
        
        bmp085.init();
        int t = bmp085.readTemperature();
        System.out.println("Temperature is " + t + " in 0.1C");

        int p = bmp085.readPressure();
        System.out.println("Pressure is    " + p + " in Pascals");
        System.out.println("Pressure is    " + (p / 100d) + " in hPa");

        System.out.println();
        System.out.println();

        double p0 = 1037;
        System.out.println("p0 = " + p0);

        double dp = p / 100d;
        System.out.println("p = " + dp);

        double power = 1d / 5.255d;
        System.out.println("power = " + power);

        double division = dp / p0;
        System.out.println("division = " + division);

        double pw = Math.pow(division, power);
        System.out.println("pw = " + pw);

        double altitude = 44330 * (1 - pw);
        // double p0 = 101325;
        // double altitude = 44330 * (1 - (Math.pow((p / p0), (1 /  5.255))));
        System.out.println();
        System.out.println("Altitude " + altitude + "m");
    }

}
