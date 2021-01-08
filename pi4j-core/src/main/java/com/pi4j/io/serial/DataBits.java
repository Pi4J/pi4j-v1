package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  DataBits.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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
