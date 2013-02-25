package com.pi4j.device.piface;

public enum PiFaceRelay {
    K0(0),
    K1(1);
    
    private int index = -1;
    
    private PiFaceRelay(int index){
        this.index = index;
    }
    
    public int getIndex(){
        return index;
    }        
}