package com.pi4j.component.servo.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  GenericServo.java  
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

import java.util.Map;
import java.util.Map.Entry;

import com.pi4j.component.ComponentBase;
import com.pi4j.component.servo.Servo;
import com.pi4j.component.servo.ServoDriver;
import com.pi4j.io.gpio.exception.ValidationException;

/**
 * @author Christian Wehrli
 */
public class GenericServo extends ComponentBase implements Servo {

    public enum Orientation {
        LEFT, RIGHT
    }
        
    public static final float PWM_MIN = 900; // in micro seconds
    public static final float PWM_NEUTRAL = 1500; // in micro seconds
    public static final float PWM_MAX = 2100; // in micro seconds

    private ServoDriver servoDriver;
    private float position;
    private int pwmDuration;
    private float pwmDurationEndPointLeft = -1;
    private float pwmDurationNeutral = -1;
    private float pwmDurationEndPointRight = -1;
    private boolean isReverse = false;

    public GenericServo(ServoDriver servoDriver, String name) {
        this(servoDriver, name, null);
    }
    
    public GenericServo(ServoDriver servoDriver, String name, Map<String, String> properties) {
        setServoDriver(servoDriver);
        setName(name);
        if (properties != null && properties.isEmpty() == false) {
            for (String key : properties.keySet()) {
                setProperty(key, properties.get(key));
            }
        } else {
            init();
        }
    }

    protected void setServoDriver(ServoDriver servoDriver) {
        this.servoDriver = servoDriver;
    }

    public ServoDriver getServoDriver() {
        return servoDriver;
    }


    @Override
    public void setPosition(float position) {
        this.position = validatePosition(position);
        pwmDuration = calculatePwmDuration(position);
        servoDriver.setServoPulseWidth(pwmDuration);
    }

    @Override
    public float getPosition() {
        return position;
    }

    protected int getPwmDuration() {
        return pwmDuration;
    }

    @Override
    public void setProperty(String key, String value) {
        super.setProperty(key, value);
        init();
    }

    protected void init() {
        pwmDurationEndPointLeft = calculateEndPointPwmDuration(Orientation.LEFT);
        pwmDurationEndPointRight = calculateEndPointPwmDuration(Orientation.RIGHT);
        pwmDurationNeutral = calculateNeutralPwmDuration();
        isReverse = Boolean.parseBoolean(getProperty(PROP_IS_REVERSE, PROP_IS_REVERSE_DEFAULT));
    }

    float calculateEndPointPwmDuration(Orientation orientation) {
        float result;
        String propertyName;
        if (orientation == Orientation.LEFT) {
            propertyName = PROP_END_POINT_LEFT;
        } else if (orientation == Orientation.RIGHT) {
            propertyName = PROP_END_POINT_RIGHT;
        } else {
            throw new UnsupportedOperationException("Unsupported orientation: " + orientation.toString());
        }
        float endPointValue = Float.parseFloat(getProperty(propertyName, PROP_END_POINT_DEFAULT));
        validateEndPoint(endPointValue, propertyName);
        
        float calculatedPwmDuration;
        if (orientation == Orientation.LEFT) {
            calculatedPwmDuration = calculateNeutralPwmDuration() - ((PWM_MAX - PWM_NEUTRAL) / 150 * endPointValue);
        } else {
            calculatedPwmDuration = calculateNeutralPwmDuration() + ((PWM_MAX - PWM_NEUTRAL) / 150 * endPointValue);
        }
        
        
        if (calculatedPwmDuration < PWM_MIN) {
            result = PWM_MIN;
        } else if (calculatedPwmDuration > PWM_MAX) {
            result = PWM_MAX;
        } else {
            result = calculatedPwmDuration;
        }
        return result;
    }

    /**
     * @return pwm pulse duration in microseconds for neutral position considering subtrim
     */
    float calculateNeutralPwmDuration() {
        float subtrimValue = Float.parseFloat(getProperty(PROP_SUBTRIM, PROP_SUBTRIM_DEFAULT));
        validateSubtrim(subtrimValue, PROP_SUBTRIM);
        return PWM_NEUTRAL + subtrimValue;
    }

    /**
     * @param position value between -100 and +100%
     * @return pwm pulse duration in servo driver resolution
     */
    int calculatePwmDuration(float position) {
        float result;
        if (isReverse) {
            position = -position;
        }
        if (position < 0) {
            result = (int)(pwmDurationNeutral + (pwmDurationNeutral - pwmDurationEndPointLeft) * position / 100.00);
        } else if (position > 0) {
            result = (int)(pwmDurationNeutral + (pwmDurationEndPointRight - pwmDurationNeutral) * position / 100.00);
        } else {
            result = (int)pwmDurationNeutral;
        }
        return (int)((result * servoDriver.getServoPulseResolution()) / 1000);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Position [" + position + "]");
        for (Entry<String, String> property : getProperties().entrySet()) {
            result.append(", " + property.getKey() + " [" + property.getValue() + "]");
        }
        return result.toString();
    }

    //------------------------------------------------------------------------------------------------------------------
    // Validator
    //------------------------------------------------------------------------------------------------------------------
    private float validateEndPoint(float endPoint, String propertyName) {
        if (endPoint < Servo.END_POINT_MIN || endPoint > Servo.END_POINT_MAX) {
            throw new ValidationException("Value of property [" + propertyName + "] must be between " + Servo.END_POINT_MIN + " and " + Servo.END_POINT_MAX + " but is [" + endPoint + "]");
        }
        return endPoint;
    }

    private float validateSubtrim(float subtrim, String propertyName) {
        if (subtrim < Servo.SUBTRIM_MAX_LEFT || subtrim > Servo.SUBTRIM_MAX_RIGHT) {
            throw new ValidationException("Value of property [" + propertyName + "] must be between " + Servo.SUBTRIM_MAX_LEFT + " and +" + Servo.SUBTRIM_MAX_RIGHT + " but is [" + subtrim + "]");
        }
        return subtrim;
    }

    private float validatePosition(float position) {
        if (position < Servo.POS_MAX_LEFT || position > Servo.POS_MAX_RIGHT) {
            throw new ValidationException("Position [" + position + "] must be between " + Servo.POS_MAX_LEFT + "(%) and +" + Servo.POS_MAX_RIGHT + "(%) but is [" + position + "]");
        }
        return position;
    }
}
