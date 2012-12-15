package com.pi4j.component.light;

public interface DimmableLight extends Light {
    
    void setLevel(int level);
    int getLevel();
    int getMinLevel();
    int getMaxLevel();
    
}
