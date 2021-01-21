package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  DataBits.java
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

public enum DataBits {

    _5(com.pi4j.jni.Serial.DATA_BITS_5),
    _6(com.pi4j.jni.Serial.DATA_BITS_6),
    _7(com.pi4j.jni.Serial.DATA_BITS_7),
    _8(com.pi4j.jni.Serial.DATA_BITS_8);

    private int dataBits = 0;

    private DataBits(int dataBits){
        this.dataBits = dataBits;
    }

    public int getValue(){
        return this.dataBits;
    }

    public static DataBits getInstance(int data_bits){
        for(DataBits db : DataBits.values()){
            if(db.getValue() == data_bits){
                return db;
            }
        }
        return null;
    }

}
