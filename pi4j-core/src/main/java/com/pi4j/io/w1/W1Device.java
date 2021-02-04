package com.pi4j.io.w1;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  W1Device.java
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
