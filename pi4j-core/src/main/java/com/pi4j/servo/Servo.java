package com.pi4j.servo;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Servo.java  
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

/**
 * <p> This is abstraction an servo. It has dual way into servo position: float point where 'normal' 
 * servo arm position is defined with value between 0.0f to 1.0f, while values below zero and above
 * one are still allowed (providing that servo driver supports them). </p>
 * <p> Normally 0.0f should correspond to ordinary RC servo pulse size of1000us while 
 * pulse size of 2000us is defined by value of 1.0f.</p
 * <p> In parallel to float point there is ability to drive servo using raw, integer values.
 * Implementation should provide minimum and maximum int values supported by servo abstraction
 * implementation.</p>
 * 
 * @author Daniel Sendula
 *
 */
public interface Servo {

    void setPosition(float position) throws IOException;

    float getPosition() throws IOException;
    
    void setRawPosition(int position) throws IOException;

    int getRawPosition() throws IOException;
    
    int getMinRawValue();
    
    int getMaxRawValue();
}
