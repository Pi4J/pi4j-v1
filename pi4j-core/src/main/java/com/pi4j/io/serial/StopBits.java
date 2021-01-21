package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  StopBits.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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

public enum StopBits {

    _1(com.pi4j.jni.Serial.STOP_BITS_1),
    _2(com.pi4j.jni.Serial.STOP_BITS_2);

    private int stopBits = 0;

    private StopBits(int stopBits){
        this.stopBits = stopBits;
    }

    public int getValue(){
        return this.stopBits;
    }

    public static StopBits getInstance(int stop_bits){
        for(StopBits sb : StopBits.values()){
            if(sb.getValue() == stop_bits){
                return sb;
            }
        }
        return null;
    }
}
