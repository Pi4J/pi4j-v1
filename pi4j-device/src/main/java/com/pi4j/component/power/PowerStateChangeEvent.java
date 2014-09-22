package com.pi4j.component.power;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PowerStateChangeEvent.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
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

public class PowerStateChangeEvent extends EventObject {

    private static final long serialVersionUID = 71037855732224580L;
    protected final PowerState oldState;
    protected final PowerState newState;

    public PowerStateChangeEvent(Power power, PowerState oldState, PowerState newState) {
        super(power);
        this.oldState = oldState;        
        this.newState = newState;
    }

    public Power getPower() {
        return (Power)getSource();
    }
    
    public PowerState getOldState() {
        return oldState;
    }

    public PowerState getNewState() {
        return newState;
    }
}
