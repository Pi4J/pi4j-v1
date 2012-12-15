package com.pi4j.component.lcd;

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
