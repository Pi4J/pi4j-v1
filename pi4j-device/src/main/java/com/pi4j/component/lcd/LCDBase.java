package com.pi4j.component.lcd;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  LCDBase.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
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


import java.io.UnsupportedEncodingException;
import com.pi4j.component.ComponentBase;

public abstract class LCDBase extends ComponentBase implements LCD {

    @Override
    public abstract int getLineCount();

    @Override
    public abstract void clear(); 

    @Override
    public abstract void clearLine(int line); 

    @Override
    public abstract void setLine(int line, byte[] data);

    @Override
    public void setLine(int line, String data) {
        try{
            setLine(line, data.getBytes("UTF-8"));
         }
        catch(UnsupportedEncodingException e){
            throw new RuntimeException(e);
         }                
    }

    @Override
    public void setLine(int line, char[] data) {
        byte[] bytes = new byte[data.length];
        for(int i=0;i<data.length;i++) {
           bytes[i] = (byte) data[i];
        }        
        setLine(line, bytes);
    }
}
