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
 * Copyright (C) 2012 - 2018 Pi4J
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

public class FireplaceStateChangeEvent extends EventObject {

	private static final long serialVersionUID = -8323406968173484160L;
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
