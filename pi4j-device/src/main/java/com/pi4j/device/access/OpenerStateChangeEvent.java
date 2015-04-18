package com.pi4j.device.access;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  OpenerStateChangeEvent.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */


import java.util.EventObject;

public class OpenerStateChangeEvent extends EventObject {

    private static final long serialVersionUID = 3497539878284173583L;
    protected final OpenerState oldState;
    protected final OpenerState newState;

    public OpenerStateChangeEvent(Opener opener, OpenerState oldState, OpenerState newState) {
        super(opener);
        this.oldState = oldState;        
        this.newState = newState;
    }

    public Opener getOpener() {
        return (Opener)getSource();
    }
    
    public OpenerState getOldState() {
        return oldState;
    }

    public OpenerState getNewState() {
        return newState;
    }
}
