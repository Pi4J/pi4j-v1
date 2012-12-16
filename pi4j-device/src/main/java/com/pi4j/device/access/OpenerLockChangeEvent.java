package com.pi4j.device.access;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  OpenerLockChangeEvent.java  
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


import java.util.EventObject;

public class OpenerLockChangeEvent extends EventObject {

    private static final long serialVersionUID = -1012641430880121426L;
    protected final boolean isLocked;

    public OpenerLockChangeEvent(Opener opener, boolean isLocked) {
        super(opener);
        this.isLocked = isLocked;        
    }

    public Opener getOpener() {
        return (Opener)getSource();
    }
    
    public boolean isLocked() {
        return isLocked;
    }
}
