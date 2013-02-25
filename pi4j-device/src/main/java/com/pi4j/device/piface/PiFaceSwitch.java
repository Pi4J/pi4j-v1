package com.pi4j.device.piface;

public enum PiFaceSwitch {
    S1(0),
    S2(1),
    S3(2),
    S4(3);
    
    private int index = -1;
    
    private PiFaceSwitch(int index){
        this.index = index;
    }
    
    public int getIndex(){
        return index;
    }
}