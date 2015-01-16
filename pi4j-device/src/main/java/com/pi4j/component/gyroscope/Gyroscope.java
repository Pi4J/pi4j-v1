package com.pi4j.component.gyroscope;

import java.io.IOException;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  Gyroscope.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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

public interface Gyroscope {
    
    int READ_NOT_TRIGGERED = 0;
    int GET_ANGLE_TRIGGER_READ = 1;
    int GET_ANGULAR_VELOCITY_TRIGGER_READ = 2;
    int GET_RAW_VALUE_TRIGGER_READ = 4;

    float getAngularVelocity() throws IOException;

    void recalibrateOffset() throws IOException;
    
    void setReadTrigger(int readTrigger);
    
    void setRawValue(int value);
    int getRawValue() throws IOException;
    
    void setOffset(int offset);
    int getOffset();

    void setAngle(float angle);
    float getAngle() throws IOException;

}
