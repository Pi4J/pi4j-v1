package com.pi4j.component.servo.impl;

import java.io.IOException;

import com.pi4j.component.servo.ServoDriver;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiGpioProvider;

public class RPIServoBlasterServoDriver implements ServoDriver {

    protected Pin servoPin;
    protected int index;
    protected String pinString;
    protected int servoPosition;
    protected RPIServoBlasterProvider provider;
    protected static GpioProvider gpioProvider;
    
    static {
        GpioProvider provider = GpioFactory.getDefaultProvider();
        if (provider instanceof RaspiGpioProvider) {
            gpioProvider = provider; 
        } else {
            gpioProvider = new RaspiGpioProvider();
        }
    }
    
    protected RPIServoBlasterServoDriver(Pin servoPin, int index, String pinString, RPIServoBlasterProvider provider) throws IOException {
        this.index = index;
        this.servoPin = servoPin;
        this.pinString = pinString;
        this.provider = provider;
    }
    
    
    public int getServoPulseWidth() {
        return servoPosition;
    }
    
    public void setServoPulseWidth(int width) {
        this.servoPosition = width;
        provider.updateServo(pinString, width);
    }
    
    public int getServoPulseResolution() {
        return 100;
    }

    @Override
    public GpioProvider getProvider() {
        return gpioProvider;
    }

    @Override
    public Pin getPin() {
        return servoPin;
    }
}