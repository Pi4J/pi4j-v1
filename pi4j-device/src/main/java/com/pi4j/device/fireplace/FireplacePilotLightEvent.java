package com.pi4j.device.fireplace;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  FireplacePilotLightEvent.java  
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

public class FireplacePilotLightEvent extends EventObject {

    protected final boolean isPilotLightOn;

    public FireplacePilotLightEvent(Fireplace fireplaceComponent, boolean isPilotLightOn) {
        super(fireplaceComponent);
        this.isPilotLightOn = isPilotLightOn;
    }

    public Fireplace getFireplace() {
        return (Fireplace)getSource();
    }
    
    public boolean isPilotLightOn() {
        return isPilotLightOn;
    }
    public boolean isPilotLightOff() {
        return !isPilotLightOn;
    }
}
