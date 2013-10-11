package com.pi4j.component.servo;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;


/**
 * This interface represents a driver hardware to produce pulses needed for driving
 * a servo.
 * 
 * @author Daniel Sendula
 */
public interface ServoDriver {

    /**
     * This method returns current servo pulse width. Zero may represent
     * this driver stopped producing pulses. Also, value of -1
     * may define undefined situation when this abstraction didn't get
     * initial value yet and there is no way telling what real, hardware
     * or software driver is sending.
     * 
     * @return current servo pulse this driver is producing
     */
    int getServoPulseWidth();
    
    /**
     * Sets servo pulse width in resolution provided by {@link #getServoPulseResolution()}.
     * Zero value may mean that this driver is currently not producing pulse.
     * Negative values may, generally, be invalid.
     * 
     * @param width pulse width in resolution read from {@link #getServoPulseResolution()}
     */
    void setServoPulseWidth(int width);
    
    /**
     * This is read only value driver is to provide to users of this class.
     * It defines resolution {@link #getServoPulseWidth()} and {@link #setServoPulseWidth(int)}
     * methods are operating in. Resolution is provided in 1/n (ms) where value returned
     * from this method is n.
     * 
     * @return resolution of servo pulse widths used in this interface
     */
    int getServoPulseResolution();


    GpioProvider getProvider();

    Pin getPin();
}
