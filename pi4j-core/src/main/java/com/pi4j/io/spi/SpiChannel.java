package com.pi4j.io.spi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SpiChannel.java  
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
