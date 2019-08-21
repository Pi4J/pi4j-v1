package com.pi4j.gpio.extension.pca;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  PCA9685GpioProvider.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.exception.InvalidPinException;
import com.pi4j.io.gpio.exception.InvalidPinModeException;
import com.pi4j.io.gpio.exception.ValidationException;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

/**
 * <p>
 * This GPIO provider implements the PCA9685 16-channel, 12-bit PWM I2C-bus LED/Servo controller as native Pi4J GPIO pins.
 * The PCA9685 is connected via I2C connection to the Raspberry Pi and provides 16 PWM pins that can be used for PWM output.
 * </p>
 * <p>
 * More information about the PCA9685 can be found here:<br>
 * <a href="http://www.nxp.com/documents/data_sheet/PCA9685.pdf">PCA9685.pdf</a><br><br>
 * ...and especially about the board here:<br>
 * <a href="http://www.adafruit.com/products/815">Adafruit 16-Channel 12-bit PWM/Servo Driver</a>
 * </p>
 *
 * @author Christian Wehrli
 */
public class PCA9685GpioProvider extends GpioProviderBase implements GpioProvider {

    public static final String NAME = "com.pi4j.gpio.extension.pca.PCA9685GpioProvider";
    public static final String DESCRIPTION = "PCA9685 PWM Provider";
    public static final int INTERNAL_CLOCK_FREQ = 25 * 1000 * 1000; // 25 MHz
    public static final BigDecimal MIN_FREQUENCY = new BigDecimal("40"); // 40 Hz
    public static final BigDecimal MAX_FREQUENCY = new BigDecimal("1000"); // 1 kHz
    /**
     * This would result in a period duration of ~22ms which is save for all type of servo
     */
    public static final BigDecimal ANALOG_SERVO_FREQUENCY = new BigDecimal("45.454");
    /**
     * This would result in a period duration of ~11ms which is recommended when using digital servos ONLY!
     */
    public static final BigDecimal DIGITAL_SERVO_FREQUENCY = new BigDecimal("90.909");
    public static final BigDecimal DEFAULT_FREQUENCY = ANALOG_SERVO_FREQUENCY;
    public static final int PWM_STEPS = 4096; // 12 Bit
    // Registers
    private static final int PCA9685A_MODE1 = 0x00;
    private static final int PCA9685A_PRESCALE = 0xFE;
    private static final int PCA9685A_LED0_ON_L = 0x06;
    private static final int PCA9685A_LED0_ON_H = 0x07;
    private static final int PCA9685A_LED0_OFF_L = 0x08;
    private static final int PCA9685A_LED0_OFF_H = 0x09;

    private boolean i2cBusOwner = false;
    private final I2CBus bus;
    private final I2CDevice device;
    private BigDecimal frequency;
    private int periodDurationMicros;

    // custom pin cache
    protected PCA9685GpioProviderPinCache[] cache = new PCA9685GpioProviderPinCache[16]; // support up to pin address 15 (16 pins)

    public PCA9685GpioProvider(int busNumber, int address) throws UnsupportedBusNumberException, IOException {
        // create I2C communications bus instance
        this(I2CFactory.getInstance(busNumber), address);
        i2cBusOwner = true;
    }

    public PCA9685GpioProvider(I2CBus bus, int address) throws IOException {
        this(bus, address, DEFAULT_FREQUENCY, BigDecimal.ONE);
    }

    public PCA9685GpioProvider(I2CBus bus, int address, BigDecimal targetFrequency) throws IOException {
        this(bus, address, targetFrequency, BigDecimal.ONE);
    }

    public PCA9685GpioProvider(I2CBus bus, int address, BigDecimal targetFrequency, BigDecimal frequencyCorrectionFactor) throws IOException {
        // create I2C communications bus instance
        this.bus = bus; // 1
        // create I2C device instance
        device = bus.getDevice(address); // 0x40
        device.write(PCA9685A_MODE1, (byte) 0);
        setFrequency(targetFrequency, frequencyCorrectionFactor);
    }

    /**
     * Target frequency (accuracy is around +/- 5%!)
     *
     * @param frequency desired PWM frequency
     * @see #setFrequency(int, BigDecimal)
     */
    public void setFrequency(BigDecimal frequency) {
        setFrequency(frequency, BigDecimal.ONE);
    }

    /**
     * The built-in Oscillator runs at ~25MHz. For better accuracy user can provide a correction
     * factor to meet desired frequency.<p>
     * <b>Note:</b> correction is limited to a certain degree because the calculated prescale value has to be
     * rounded to an integer value!
     * <p>
     * <b>Example:</b><br>
     * target freq: 50Hz<br>
     * actual freq: 52.93Hz<br>
     * correction factor: 52.93 / 50 = 1.0586<br>
     *
     * @param targetFrequency desired frequency
     * @param frequencyCorrectionFactor 'actual frequency' / 'target frequency'
     */
    public void setFrequency(BigDecimal targetFrequency, BigDecimal frequencyCorrectionFactor) {
        validateFrequency(targetFrequency);
        frequency = targetFrequency;
        periodDurationMicros = calculatePeriodDuration();
        int prescale = calculatePrescale(frequencyCorrectionFactor);
        int oldMode;
        try {
            oldMode = device.read(PCA9685A_MODE1);
            int newMode = (oldMode & 0x7F) | 0x10; // sleep
            device.write(PCA9685A_MODE1, (byte) newMode); // go to sleep
            device.write(PCA9685A_PRESCALE, (byte) prescale);
            device.write(PCA9685A_MODE1, (byte) oldMode);
            Thread.sleep(1);
            device.write(PCA9685A_MODE1, (byte) (oldMode | 0x80));
        } catch (IOException e) {
            throw new RuntimeException("Unable to set prescale value [" + prescale + "]", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set pulse duration in microseconds.<br>
     * Make sure duration doesn't exceed period time(1'000'000/freq)!
     *
     * @param pin represents channel 0..15
     * @param duration pulse duration in microseconds
     */
    @Override
    public void setPwm(Pin pin, int duration) {
        int offPosition = calculateOffPositionForPulseDuration(duration);
        setPwm(pin, 0, offPosition);
    }

    /**
     * The LEDn_ON and LEDn_OFF counts can vary from 0 to 4095 max.<br>
     * The LEDn_ON and LEDn_OFF count registers should never be programmed with the same values.
     * <p>
     * Because the loading of the LEDn_ON and LEDn_OFF registers is via the I2C-bus, and
     * asynchronous to the internal oscillator, we want to ensure that we do not see any visual
     * artifacts of changing the ON and OFF values. This is achieved by updating the changes at
     * the end of the LOW cycle.
     *
     * @param pin represents channel 0..15
     * @param onPosition value between 0 and 4095
     * @param offPosition value between 0 and 4095
     */
    public void setPwm(Pin pin, int onPosition, int offPosition) {
        validatePin(pin, onPosition, offPosition);
        final int channel = pin.getAddress();
        validatePwmValueInRange(onPosition);
        validatePwmValueInRange(offPosition);
        if (onPosition == offPosition) {
            throw new ValidationException("ON [" + onPosition + "] and OFF [" + offPosition + "] values must be different.");
        }
        try {
            device.write(PCA9685A_LED0_ON_L + 4 * channel, (byte) (onPosition & 0xFF));
            device.write(PCA9685A_LED0_ON_H + 4 * channel, (byte) (onPosition >> 8));
            device.write(PCA9685A_LED0_OFF_L + 4 * channel, (byte) (offPosition & 0xFF));
            device.write(PCA9685A_LED0_OFF_H + 4 * channel, (byte) (offPosition >> 8));
        } catch (IOException e) {
            throw new RuntimeException("Unable to write to PWM channel [" + channel + "] values for ON [" + onPosition + "] and OFF [" + offPosition + "] position.", e);
        }
        cachePinValues(pin, onPosition, offPosition);
    }

    /**
     * Permanently sets the output to High (no PWM anymore).<br>
     * The LEDn_ON_H output control bit 4, when set to logic 1, causes the output to be always ON.
     *
     * @param pin represents channel 0..15
     */
    public void setAlwaysOn(Pin pin) {
        final int pwmOnValue = 0x1000;
        final int pwmOffValue = 0x0000;
        validatePin(pin, pwmOnValue, pwmOffValue);
        final int channel = pin.getAddress();
        try {
            device.write(PCA9685A_LED0_ON_L + 4 * channel, (byte) 0x00);
            device.write(PCA9685A_LED0_ON_H + 4 * channel, (byte) 0x10); // set bit 4 to high
            device.write(PCA9685A_LED0_OFF_L + 4 * channel, (byte) 0x00);
            device.write(PCA9685A_LED0_OFF_H + 4 * channel, (byte) 0x00);
        } catch (IOException e) {
            throw new RuntimeException("Error while trying to set channel [" + channel + "] always ON.", e);
        }
        cachePinValues(pin, pwmOnValue, pwmOffValue);
    }

    /**
     * Permanently sets the output to Low (no PWM anymore).<br>
     * The LEDn_OFF_H output control bit 4, when set to logic 1, causes the output to be always OFF.
     * In this case the values in the LEDn_ON registers are ignored.
     *
     * @param pin represents channel 0..15
     */
    public void setAlwaysOff(Pin pin) {
        final int pwmOnValue = 0x0000;
        final int pwmOffValue = 0x1000;
        validatePin(pin, pwmOnValue, pwmOffValue);
        final int channel = pin.getAddress();
        try {
            device.write(PCA9685A_LED0_ON_L + 4 * channel, (byte) 0x00);
            device.write(PCA9685A_LED0_ON_H + 4 * channel, (byte) 0x00);
            device.write(PCA9685A_LED0_OFF_L + 4 * channel, (byte) 0x00);
            device.write(PCA9685A_LED0_OFF_H + 4 * channel, (byte) 0x10); // set bit 4 to high
        } catch (IOException e) {
            throw new RuntimeException("Error while trying to set channel [" + channel + "] always OFF.", e);
        }
        cachePinValues(pin, pwmOnValue, pwmOffValue);
    }

    public BigDecimal getFrequency() {
        return frequency;
    }

    public int getPeriodDurationMicros() {
        return periodDurationMicros;
    }

    /**
     * Calculates the OFF position for a certain pulse duration.
     *
     * @param duration pulse duration in microseconds
     * @return OFF position(value between 1 and 4095)
     */
    public int calculateOffPositionForPulseDuration(int duration) {
        validatePwmDuration(duration);
        int result = (PWM_STEPS - 1) * duration / periodDurationMicros;
        if (result < 1) {
            result = 1;
        } else if (result > PWM_STEPS - 1) {
            result = PWM_STEPS - 1;
        }
        return result;
    }

    private int calculatePeriodDuration() {
        return new BigDecimal("1000000").divide(frequency, 0, RoundingMode.HALF_UP).intValue();
    }

    private int calculatePrescale(BigDecimal frequencyCorrectionFactor) {
        BigDecimal theoreticalPrescale = BigDecimal.valueOf(INTERNAL_CLOCK_FREQ);
        theoreticalPrescale = theoreticalPrescale.divide(BigDecimal.valueOf(PWM_STEPS), 3, RoundingMode.HALF_UP);
        theoreticalPrescale = theoreticalPrescale.divide(frequency, 0, RoundingMode.HALF_UP);
        theoreticalPrescale = theoreticalPrescale.subtract(BigDecimal.ONE);
        return theoreticalPrescale.multiply(frequencyCorrectionFactor).intValue();
    }

    private void validateFrequency(BigDecimal frequency) {
        if (frequency.compareTo(MIN_FREQUENCY) == -1 || frequency.compareTo(MAX_FREQUENCY) == 1) {
            throw new ValidationException("Frequency [" + frequency + "] must be between 40.0 and 1000.0 Hz.");
        }
    }

    private void validatePwmValueInRange(int pwmValue) {
        if (pwmValue < 0 || pwmValue > 4095) {
            throw new ValidationException("PWM position value [" + pwmValue + "] must be between 0 and 4095.");
        }
    }

    private void validatePwmDuration(int duration) {
        if (duration < 1) {
            throw new ValidationException("Duration [" + duration + "] must be >= 1us.");
        }
        if (duration >= periodDurationMicros) {
            throw new ValidationException("Duration [" + duration + "] must be <= period duration [" + periodDurationMicros + "].");
        }
    }

    private void validatePin(Pin pin, int onPosition, int offPosition) {
        if (hasPin(pin) == false) {
            throw new InvalidPinException(pin);
        }
        PinMode mode = getMode(pin);
        if (mode != PinMode.PWM_OUTPUT) {
            throw new InvalidPinModeException(pin, "Invalid pin mode [" + mode.getName() + "]; unable to setPwm(" + onPosition + ", " + offPosition + ")");
        }
    }

    private void cachePinValues(Pin pin, int onPosition, int offPosition) {
        getPinCache(pin).setPwmOnValue(onPosition);
        getPinCache(pin).setPwmOffValue(offPosition);
    }

    /**
     * @param pin represents channel 0..15
     * @return [0]: onValue, [1]: offValue
     */
    public int[] getPwmOnOffValues(Pin pin) {
        if (hasPin(pin) == false) {
            throw new InvalidPinException(pin);
        }
        int pwmValues[] = {
                getPinCache(pin).getPwmOnValue(),
                getPinCache(pin).getPwmOffValue()};
        return pwmValues;
    }

    /**
     * Reset all outputs (set to always OFF)
     */
    public void reset() {
        for (Pin pin : PCA9685Pin.ALL) {
            setAlwaysOff(pin);
        }
    }

    @Override
    protected PCA9685GpioProviderPinCache getPinCache(Pin pin) {
        PCA9685GpioProviderPinCache pc = cache[pin.getAddress()];
        if(pc == null){
            pc = cache[pin.getAddress()] = new PCA9685GpioProviderPinCache(pin);
        }
        return pc;
    }

    @Override
    public int getPwm(Pin pin) {
        throw new UnsupportedOperationException("Use getPwmOnOffValues() instead.");
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void shutdown() {
        if (isShutdown()) {
            return;
        }
        super.shutdown();
        reset();
        try {
            // if we are the owner of the I2C bus, then close it
            if(i2cBusOwner) {
                // close the I2C bus communication
                bus.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
