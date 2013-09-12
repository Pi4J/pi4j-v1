package com.pi4j.servo.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  RPIServoBlasterDriver.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.pi4j.servo.Servo;
import com.pi4j.servo.ServoDriver;

/**
 * Implementation of https://github.com/richardghirst/PiBits/tree/master/ServoBlaster
 * 
 *
 * @author Daniel Sendula
 */
public class RPIServoBlasterDriver implements ServoDriver {

    public static final Integer SERVO_0 = 0;
    public static final Integer SERVO_1 = 1;
    public static final Integer SERVO_2 = 2;
    public static final Integer SERVO_3 = 3;
    public static final Integer SERVO_4 = 4;
    public static final Integer SERVO_5 = 5;
    public static final Integer SERVO_6 = 6;
    public static final Integer SERVO_7 = 7;

    public static final List<Object> ALL_SERVOS = Arrays.asList((Object)SERVO_0, SERVO_1,
            SERVO_2, SERVO_3, SERVO_4, SERVO_5, SERVO_6, SERVO_7);
    
    protected Servo[] servos = new Servo[8];
    
    public static final String SERVO_BLASTER_DEV = "/dev/servoblaster";
    
    protected File servoBlasterDev;
    
    /**
     * Constructor. It checks if /dev/servoblaster file exists.
     * 
     * @throws IOException thrown in case file /dev/servoblaster does not exist.
     */
    public RPIServoBlasterDriver() throws IOException {
        servoBlasterDev = new File(SERVO_BLASTER_DEV);
        if (!servoBlasterDev.exists()) {
            throw new FileNotFoundException("File " + SERVO_BLASTER_DEV + " is not present." +
                    " Please check https://github.com/richardghirst/PiBits/tree/master/ServoBlaster for details.");
        }
    }
    
    @Override
    public List<Object> getAllServoIds() {
        return ALL_SERVOS;
    }

    
    /**
     * Returns instance of {@link ServoImpl}. It always returns same instance for the same id.
     * (Instances are cached). Instances are thread safe (to the extend that they do not 
     * safeguard get/set position methods, but do not fail in case of concurrent calls).
     * 
     * @param id servo id as defined above.
     * @return instance of {@link ServoImpl}.
     */
    @Override
    public Servo getServo(Object id) {
        if (!(id instanceof Integer)) {
            throw new IllegalArgumentException("Wrong ID supplied; " + id);
        }
        int index = ((Integer)id);
        if (index < 0 || index > 7) {
            throw new IllegalArgumentException("Wrong ID supplied; " + id);
        }
        if (servos[index] == null) {
            servos[index] = new ServoImpl(index);
        }
        return servos[index];
    }
    
    protected class ServoImpl implements Servo {
        
        protected int index;
        protected int position;
        
        public ServoImpl(int index) {
            this.index = index;
        }
        
        @Override
        public void setPosition(float position) throws IOException {
            if (position < 0.0f) {
                position = 0.0f;
            } else if (position > 1.0f) {
                position = 1.0f;
            }
            int rawPosition = (int)(position * getMaxRawValue());
            setRawPosition(rawPosition);
        }

        @Override
        public float getPosition() throws IOException {
            return getRawPosition() / getMaxRawValue();
        }

        @Override
        public void setRawPosition(int position) throws IOException {
            this.position = position;
            FileOutputStream out = new FileOutputStream(servoBlasterDev);
            try {
                out.write((index + "=" + position + "\n").getBytes());
                out.flush();
            } finally {
                out.close();
            }
        }

        @Override
        public int getRawPosition() throws IOException {
            return position;
        }

        @Override
        public int getMinRawValue() {
            return 0;
        }

        @Override
        public int getMaxRawValue() {
            return 200;
        }
    }

}
