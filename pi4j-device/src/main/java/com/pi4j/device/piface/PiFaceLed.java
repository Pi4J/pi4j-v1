package com.pi4j.device.piface;

public enum PiFaceLed {
    LED0(0),
    LED1(1),
    LED2(2),
    LED3(3),
    LED4(4),
    LED5(5),
    LED6(6),
    LED7(7);
    
    private int index = -1;
    
    private PiFaceLed(int index){
        this.index = index;
    }
    
    public int getIndex(){
        return index;
    }                
}
