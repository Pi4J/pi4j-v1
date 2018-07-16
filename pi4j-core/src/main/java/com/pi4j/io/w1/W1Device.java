package com.pi4j.io.w1;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  W1Device.java
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

import java.io.IOException;

/**
 * @author Peter Schuebl
 */
public interface W1Device {

    /**
     * Returns the name (id/serial number) of the device e.g. 28-00000698ebb1.
     * @return the unique device name.
     */
    String getId();

    /**
     * Returns a human readable name.
     * @return the human readable name, defaults to ID
     */
    String getName();

    /**
     * Returns the type/family of the device.
     * @return device type, never null.
     */
    int getFamilyId();

    /**
     * Gets the current Value = content of w1_slave file
     * @return
     */
    String getValue() throws IOException;

    /**
     * W1Device should be considered equal based on their ID
     */
    boolean equals(Object obj);

    int hashCode();
}
