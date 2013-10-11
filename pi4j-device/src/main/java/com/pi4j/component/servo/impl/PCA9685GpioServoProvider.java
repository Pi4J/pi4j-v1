package com.pi4j.component.servo.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pi4j.component.servo.ServoDriver;
import com.pi4j.component.servo.ServoProvider;
import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.Pin;

public class PCA9685GpioServoProvider implements ServoProvider {

    private PCA9685GpioProvider provider;

    protected Map<Pin, PCA9685GpioServoDriver> allocatedDrivers = new HashMap<Pin, PCA9685GpioServoDriver>();
    
    public PCA9685GpioServoProvider(PCA9685GpioProvider provider) {
        this.provider = provider;
    }

    @Override
    public List<Pin> getDefinedServoPins() throws IOException {
        return Arrays.asList(PCA9685Pin.ALL);
    }

    @Override
    public synchronized ServoDriver getServoDriver(Pin servoPin) throws IOException {
        List<Pin> servoPins = getDefinedServoPins();
        int index = servoPins.indexOf(servoPin);
        if (index < 0) {
            throw new IllegalArgumentException("Servo driver cannot drive pin " + servoPin);
        }

        PCA9685GpioServoDriver driver = allocatedDrivers.get(servoPin);
        if (driver == null) {
            driver = new PCA9685GpioServoDriver(provider, servoPin);
        }
        
        return driver;
    }

}
