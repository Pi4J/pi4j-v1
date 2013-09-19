package com.pi4j.component.servo2;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  ServoDriver.java  
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

import java.util.List;

/**
 * This is abstraction of an servo driver class. 
 * 
 * @author Daniel Sendula
 *
 */
public interface ServoDriver {
    
    /**
     * This method returns list of all servos associated with this driver.
     * 
     * @return list of all servo ids
     */
    List<Object> getAllServoIds();
    
    /**
     * This method returns an instance of a servo abstraction.
     * 
     * @param id servo id
     * @return servo abstraction
     */
    Servo getServo(Object id);
    
}
