package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Baud.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
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

    public static Baud getInstance(int baud_rate){
        for(Baud b : Baud.values()){
            if(b.getValue() == baud_rate){
                return b;
            }
        }
        return null;
    }
}
