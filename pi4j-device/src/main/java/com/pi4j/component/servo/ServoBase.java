package com.pi4j.component.servo;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  ServoBase.java  
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


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map.Entry;

import com.pi4j.component.ComponentBase;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.exception.ValidationException;

/**
 * @author Christian Wehrli
 */
public abstract class ServoBase extends ComponentBase implements Servo {

    public enum Orientation {
        LEFT, RIGHT
    };

    protected static final int PWM_MIN = 900;
    protected static final int PWM_NEUTRAL = 1500;
    protected static final int PWM_MAX = 2100;

    private GpioProvider provider;
    private Pin pin;
    private int position;
    private int pwmDuration;
    private int pwmDurationEndPointLeft = -1;
    private int pwmDurationNeutral = -1;
    private int pwmDurationEndPointRight = -1;
    private boolean isReverse = false;

    protected void setProvider(GpioProvider provider) {
        this.provider = provider;
    }

    @Override
    public GpioProvider getProvider() {
        return provider;
    }

    protected void setPin(Pin pin) {
        this.pin = pin;
    }

    @Override
    public Pin getPin() {
        return pin;
    }

    @Override
    public void setPosition(int position) {
        this.position = validatePosition(position);
        pwmDuration = calculatePwmDuration(position);
    }

    @Override
    public int getPosition() {
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

    int calculateEndPointPwmDuration(Orientation orientation) {
        int result;
        String propertyName;
        int sign;
        switch (orientation) {
            case LEFT : {
                propertyName = PROP_END_POINT_LEFT;
                sign = -1;
                break;
            }
            case RIGHT : {
                propertyName = PROP_END_POINT_RIGHT;
                sign = 1;
                break;
            }
            default : {
                throw new UnsupportedOperationException("Unsupported orientation: " + orientation.toString());
            }
        }
        int endPointValue = Integer.parseInt(getProperty(propertyName, PROP_END_POINT_DEFAULT));
        validateEndPoint(endPointValue, propertyName);
        int calculatedPwmDuration = calculateNeutralPwmDuration() + ((PWM_MAX - PWM_NEUTRAL) / 150 * endPointValue * sign);
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
    int calculateNeutralPwmDuration() {
        int subtrimValue = Integer.parseInt(getProperty(PROP_SUBTRIM, PROP_SUBTRIM_DEFAULT));
        validateSubtrim(subtrimValue, PROP_SUBTRIM);
        return PWM_NEUTRAL + subtrimValue;
    }

    /**
     * @param position value between -100 and +100%
     * @return pwm pulse duration in microseconds
     */
    int calculatePwmDuration(int position) {
        int result;
        if (isReverse) {
            position *= -1;
        }
        if (position < 0) {
            result = pwmDurationNeutral + new BigDecimal(pwmDurationNeutral - pwmDurationEndPointLeft).divide(new BigDecimal("100.00"), 3, RoundingMode.HALF_UP).multiply(new BigDecimal(position)).intValue();
        } else if (position > 0) {
            result = pwmDurationNeutral + new BigDecimal(pwmDurationEndPointRight - pwmDurationNeutral).divide(new BigDecimal("100.00"), 3, RoundingMode.HALF_UP).multiply(new BigDecimal(position)).intValue();
        } else {
            result = pwmDurationNeutral;
        }
        return result;
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
    private int validateEndPoint(int endPoint, String propertyName) {
        if (endPoint < Servo.END_POINT_MIN || endPoint > Servo.END_POINT_MAX) {
            throw new ValidationException("Value of property [" + propertyName + "] must be between " + Servo.END_POINT_MIN + " and " + Servo.END_POINT_MAX + " but is [" + endPoint + "]");
        }
        return endPoint;
    }

    private int validateSubtrim(int subtrim, String propertyName) {
        if (subtrim < Servo.SUBTRIM_MAX_LEFT || subtrim > Servo.SUBTRIM_MAX_RIGHT) {
            throw new ValidationException("Value of property [" + propertyName + "] must be between " + Servo.SUBTRIM_MAX_LEFT + " and +" + Servo.SUBTRIM_MAX_RIGHT + " but is [" + subtrim + "]");
        }
        return subtrim;
    }

    private int validatePosition(int position) {
        if (position < Servo.POS_MAX_LEFT || position > Servo.POS_MAX_RIGHT) {
            throw new ValidationException("Position [" + position + "] must be between " + Servo.POS_MAX_LEFT + "(%) and +" + Servo.POS_MAX_RIGHT + "(%) but is [" + position + "]");
        }
        return position;
    }
}
