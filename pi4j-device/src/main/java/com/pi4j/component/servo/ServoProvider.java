package com.pi4j.component.servo;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  ServoProvider.java  
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
