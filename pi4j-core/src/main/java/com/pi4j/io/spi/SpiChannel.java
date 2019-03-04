package com.pi4j.io.spi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SpiChannel.java
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

public enum SpiChannel {
    CS0(0), CS1(1);

    private final short channel;

    private SpiChannel(int channel) {
        this.channel = (short) channel;
    }

    public short getChannel() {
        return channel;
    }

    public static SpiChannel getByNumber(short channelNumber){
        return getByNumber((int)channelNumber);
    }

    public static SpiChannel getByNumber(int channelNumber){
        for(SpiChannel channel : SpiChannel.values()){
            if(channel.getChannel() == channelNumber){
                return channel;
            }
        }
        return null;
    }

}
