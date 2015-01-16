package com.pi4j.component.switches;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  SwitchStateChangeEvent.java  
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


import java.util.EventObject;

public class SwitchStateChangeEvent extends EventObject {

    private static final long serialVersionUID = 482071067043836024L;
    protected final SwitchState oldState;
    protected final SwitchState newState;

    public SwitchStateChangeEvent(Switch switchComponent, SwitchState oldState, SwitchState newState) {
        super(switchComponent);
        this.oldState = oldState;        
        this.newState = newState;
    }

    public Switch getSwitch() {
        return (Switch)getSource();
    }
    
    public SwitchState getOldState() {
        return oldState;
    }

    public SwitchState getNewState() {
        return newState;
    }
}
