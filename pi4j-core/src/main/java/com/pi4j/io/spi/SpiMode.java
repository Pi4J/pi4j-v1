package com.pi4j.io.spi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SpiMode.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
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

public enum SpiMode {
    MODE_0(0), MODE_1(1), MODE_2(2), MODE_3(3);

    private final short mode;

    private SpiMode(int mode) {
        this.mode = (short) mode;
    }

    public short getMode() {
        return mode;
    }

    public static SpiMode getByNumber(short modeNumber){
        return getByNumber((int)modeNumber);
    }

    public static SpiMode getByNumber(int modeNumber){
        for(SpiMode item : SpiMode.values()){
            if(item.getMode() == modeNumber){
                return item;
            }
        }
        return null;
    }
}
