package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Baud.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
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

public enum Baud {

    _50(com.pi4j.jni.Serial.BAUD_RATE_50),
    _75(com.pi4j.jni.Serial.BAUD_RATE_75),
    _110(com.pi4j.jni.Serial.BAUD_RATE_110),
    _134(com.pi4j.jni.Serial.BAUD_RATE_134),
    _150(com.pi4j.jni.Serial.BAUD_RATE_150),
    _200(com.pi4j.jni.Serial.BAUD_RATE_200),
    _300(com.pi4j.jni.Serial.BAUD_RATE_300),
    _600(com.pi4j.jni.Serial.BAUD_RATE_600),
    _1200(com.pi4j.jni.Serial.BAUD_RATE_1200),
    _1800(com.pi4j.jni.Serial.BAUD_RATE_1800),
    _2400(com.pi4j.jni.Serial.BAUD_RATE_2400),
    _4800(com.pi4j.jni.Serial.BAUD_RATE_4800),
    _9600(com.pi4j.jni.Serial.BAUD_RATE_9600),
    _19200(com.pi4j.jni.Serial.BAUD_RATE_19200),
    _38400(com.pi4j.jni.Serial.BAUD_RATE_38400),
    _57600(com.pi4j.jni.Serial.BAUD_RATE_57600),
    _115200(com.pi4j.jni.Serial.BAUD_RATE_115200),
    _230400(com.pi4j.jni.Serial.BAUD_RATE_230400);

    private int baud = 0;

    private Baud(int baud){
        this.baud = baud;
    }

    public int getValue(){
        return this.baud;
    }
}
