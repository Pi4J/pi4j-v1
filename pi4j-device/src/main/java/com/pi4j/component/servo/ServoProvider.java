package com.pi4j.component.servo;

import java.io.IOException;
import java.util.List;

import com.pi4j.io.gpio.Pin;

/**
 * This interface allows factory to create/cache {@link ServoDriver} objects.
 *
 * @author Daniel Sendula
 */
public interface ServoProvider {

    /**
     * This method returns a list of pins this provider implementation
     * can drive.
     * 
     * @return list of pins
     * @throws IOException in case there is an error providing list of pins
     */
    List<Pin> getDefinedServoPins() throws IOException;
    
    /**
     * This method returns a {@link ServoDriver} for asked pin.
     * It may return IOException in case that driver does not know of asked
     * pin or cannot drive servo from it. Or there is any other initialization
     * error.
     * 
     * @param servoPin pin driver is needed for
     * @return a servo driver
     * @throws IOException in case that servo driver cannnot be provided for asked pin
     */
    ServoDriver getServoDriver(Pin servoPin) throws IOException;
    
}
