package com.pi4j.component.button;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  Button.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2017 Pi4J
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


import com.pi4j.component.ObserveableComponent;

public interface Button extends ObserveableComponent {

    boolean isPressed();
    boolean isReleased();
    ButtonState getState();
    boolean isState(ButtonState state);

    void addListener(ButtonStateChangeListener... listener);
    void removeListener(ButtonStateChangeListener... listener);
    void addListener(ButtonPressedListener... listener);
    void removeListener(ButtonPressedListener... listener);
    void addListener(ButtonReleasedListener... listener);
    void removeListener(ButtonReleasedListener... listener);
    void addListener(long duration, ButtonHoldListener... listener);
    void removeListener(ButtonHoldListener... listener);
}
