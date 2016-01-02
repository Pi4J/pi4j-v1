package com.pi4j.component.gyroscope.honeywell;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  HMC5883L.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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

import com.pi4j.component.gyroscope.AxisGyroscope;
import com.pi4j.component.gyroscope.Gyroscope;
import com.pi4j.component.gyroscope.MultiAxisGyro;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

public class HMC5883L implements MultiAxisGyro {
    
    public final static int HMC5883L_ADDRESS = 0x1E;

    private I2CDevice device;

    public final Gyroscope X = new AxisGyroscope(this, 20f);
    public final Gyroscope Y = new AxisGyroscope(this, 20f);
    public final Gyroscope Z = new AxisGyroscope(this, 20f);
    
    protected final AxisGyroscope aX = (AxisGyroscope)X;
    protected final AxisGyroscope aY = (AxisGyroscope)Y;
    protected final AxisGyroscope aZ = (AxisGyroscope)Z;
    
    private int timeDelta;
    private long lastRead;
    
    private static final int CALIBRATION_READS = 50;
    private static final int CALIBRATION_SKIPS = 5;
    
    public static final int OUTPUT_RATE_0_75_Hz = 0;
    public static final int OUTPUT_RATE_1_5_Hz = 1;
    public static final int OUTPUT_RATE_3_Hz = 2;
    public static final int OUTPUT_RATE_7_5_Hz = 3;
    public static final int OUTPUT_RATE_15_Hz = 4;
    public static final int OUTPUT_RATE_30_Hz = 5;
    public static final int OUTPUT_RATE_75_Hz = 6;
    
    public static final int SAMPLES_AVERAGE_1 = 0;
    public static final int SAMPLES_AVERAGE_2 = 1;
    public static final int SAMPLES_AVERAGE_4 = 2;
    public static final int SAMPLES_AVERAGE_8 = 3;
    
    public static final int NORMAL_MEASUREMENT_MODE = 0;
    public static final int POSITIVE_BIAS_MEASUREMENT_MODE = 1;
    public static final int NEGATIVE_BIAS_MEASUREMENT_MODE = 2;
    
    public static final int GAIN_0_88_Ga = 0;
    public static final int GAIN_1_3_Ga = 1;
    public static final int GAIN_1_9_Ga = 2;
    public static final int GAIN_2_5_Ga = 3;
    public static final int GAIN_4_0_Ga = 4;
    public static final int GAIN_4_7_Ga = 5;
    public static final int GAIN_5_6_Ga = 6;
    public static final int GAIN_8_1_Ga = 7;
    
    public static final int CONINIOUS_MODE = 0;
    public static final int SINGLE_SAMPLE_MODE = 1;
    public static final int IDLE_MODE = 2;
    
    private int outputRate = OUTPUT_RATE_15_Hz;
    private int samplesAverage = SAMPLES_AVERAGE_8;
    private int measurementMode = NORMAL_MEASUREMENT_MODE;
    private int gain = GAIN_1_3_Ga;
    private int mode = CONINIOUS_MODE;

    public HMC5883L(I2CBus bus) throws IOException {
        device = bus.getDevice(HMC5883L_ADDRESS);
    }
        
    
    public int getOutputRate() {
        return outputRate;
    }


    public void setOutputRate(int outputRate) {
        this.outputRate = outputRate;
    }


    public int getSamplesAverage() {
        return samplesAverage;
    }


    public void setSamplesAverage(int samplesAverage) {
        this.samplesAverage = samplesAverage;
    }


    public int getMeasurementMode() {
        return measurementMode;
    }


    public void setMeasurementMode(int measurementMode) {
        this.measurementMode = measurementMode;
    }


    public int getGain() {
        return gain;
    }


    public void setGain(int gain) {
        this.gain = gain;
    }


    public int getMode() {
        return mode;
    }


    public void setMode(int mode) {
        this.mode = mode;
    }


    public Gyroscope init(Gyroscope triggeringAxis, int triggeringMode) throws IOException {
        enable();
        
        if (triggeringAxis == aX) { aX.setReadTrigger(triggeringMode); } else { aX.setReadTrigger(Gyroscope.READ_NOT_TRIGGERED); }
        if (triggeringAxis == aY) { aY.setReadTrigger(triggeringMode); } else { aY.setReadTrigger(Gyroscope.READ_NOT_TRIGGERED); }
        if (triggeringAxis == aZ) { aZ.setReadTrigger(triggeringMode); } else { aZ.setReadTrigger(Gyroscope.READ_NOT_TRIGGERED); }
        return triggeringAxis;
    }
    
    @Override
    public void enable() throws IOException {
//        byte[] init = new byte[3];
//        
//        init[0] = (byte)((samplesAverage << 5) + (outputRate << 2) + measurementMode);
//        init[1] = (byte)((gain << 5));
//        init[2] = (byte)(mode);
//
//        device.write(0, init, 0, 3);
        device.write(2, (byte)0);
    }


    @Override
    public void disable() throws IOException {
        byte[] init = new byte[3];
        
        init[0] = (byte)((samplesAverage << 5) + (outputRate << 2) + measurementMode);
        init[1] = (byte)((gain << 5));
        init[2] = (byte)(IDLE_MODE);

        device.write(0, init, 0, 3);
    }


    @Override
    public void readGyro() throws IOException {
        long now = System.currentTimeMillis();
        timeDelta = (int)(now - lastRead);
        lastRead = now;

        byte[] data = new byte[6];

        int r = device.read(3, data, 0, 6);
        if (r != 6) {
            throw new IOException("Couldn't read compass data; r=" + r);
        }
        
        int x = ((data[0] & 0xff) << 8) + (data[1] & 0xff);
        int y = ((data[2] & 0xff) << 8) + (data[3] & 0xff);
        int z = ((data[3] & 0xff) << 8) + (data[5] & 0xff);

        aX.setRawValue(x);
        aY.setRawValue(y);
        aZ.setRawValue(z);

    }


    @Override
    public int getTimeDelta() {
        return timeDelta;
    }


    @Override
    public void recalibrateOffset() throws IOException {
        long totalX = 0;
        long totalY = 0;
        long totalZ = 0;
        
        int minX = 10000;
        int minY = 10000;
        int minZ = 10000;
        
        int maxX = -10000;
        int maxY = -10000;
        int maxZ = -10000;
        
        for (int i = 0; i < CALIBRATION_SKIPS; i++) {
            readGyro();
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignore) { }
        }
        
        for (int i = 0; i < CALIBRATION_READS; i ++) {
            readGyro();
            
            int x = aX.getRawValue();
            int y = aY.getRawValue();
            int z = aZ.getRawValue();
            
            totalX = totalX + x;
            totalY = totalY + y;
            totalZ = totalZ + z;
            if (x < minX) { minX = x; }
            if (y < minY) { minY = y; }
            if (z < minZ) { minZ = z; }

            if (x > maxX) { maxX = x; }
            if (y > maxY) { maxY = y; }
            if (z > maxZ) { maxZ = z; }
        }

        aX.setOffset((int)(totalX / CALIBRATION_READS));
        aY.setOffset((int)(totalY / CALIBRATION_READS));
        aZ.setOffset((int)(totalZ / CALIBRATION_READS));

//        aX.setOffset((maxX + minX) / 2);
//        aY.setOffset((maxY + minY) / 2);
//        aZ.setOffset((maxZ + minZ) / 2);
    }


    public static void main(String[] args) throws Exception {
        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_0);

        HMC5883L hmc5883l = new HMC5883L(bus);

        hmc5883l.init(hmc5883l.X, Gyroscope.GET_RAW_VALUE_TRIGGER_READ);

        long now = System.currentTimeMillis();
        
        int measurement = 0;
        
        while (System.currentTimeMillis() - now < 10000) {
            
            String sm = toString(measurement, 3);
            
            String sx = toString(hmc5883l.X.getRawValue(), 7);
            String sy = toString(hmc5883l.Y.getRawValue(), 7);
            String sz = toString(hmc5883l.Z.getRawValue(), 7);
            
            System.out.print(sm + sx + sy + sz);
            for (int i = 0; i < 24; i++) { System.out.print((char)8); }
            
            Thread.sleep(100);
            
            measurement++;
        }
        System.out.println();
    }
    
    public static String toString(int i, int l) {
        String s = Integer.toString(i);
        return "        ".substring(0, l - s.length()) + s;
    }



}
