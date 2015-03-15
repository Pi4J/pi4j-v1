package com.pi4j.device.fireplace;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  FireplaceStateChangeEvent.java  
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

public class FireplaceStateChangeEvent extends EventObject {

    protected final FireplaceState oldState;
    protected final FireplaceState newState;

    public FireplaceStateChangeEvent(Fireplace fireplaceComponent, FireplaceState oldState, FireplaceState newState) {
        super(fireplaceComponent);
        this.oldState = oldState;        
        this.newState = newState;
    }

    public Fireplace getFireplace() {
        return (Fireplace)getSource();
    }
    
    public FireplaceState getOldState() {
        return oldState;
    }

    public FireplaceState getNewState() {
        return newState;
    }

    public boolean isNewState(FireplaceState state) {
        return (newState == state);
    }

    public boolean isOldState(FireplaceState state) {
        return (oldState == state);
    }

}
