package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  FlowControl.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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

public enum FlowControl {

    NONE(com.pi4j.jni.Serial.FLOW_CONTROL_NONE),
    HARDWARE(com.pi4j.jni.Serial.FLOW_CONTROL_HARDWARE),
    SOFTWARE(com.pi4j.jni.Serial.FLOW_CONTROL_SOFTWARE);

    private int index = 0;

    private FlowControl(int index){
        this.index = index;
    }

    public int getIndex(){
        return this.index;
    }

    public static FlowControl getInstance(String flow_control) {
        return FlowControl.valueOf(flow_control.toUpperCase());
    }

}
