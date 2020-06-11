/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  W1TempExample.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.io.w1.W1Master;
import com.pi4j.temperature.TemperatureScale;

import java.io.IOException;

/**
 * @author Peter Schuebl
 */
public class W1TempExample {

    public static void main(String args[]) throws InterruptedException, IOException {
        //W1Master w1Master = new W1Master("C:\\work\\pi4j\\pi4j-device\\target\\test-classes\\w1\\sys\\bus\\w1\\devices");
        W1Master w1Master = new W1Master();

        System.out.println(w1Master);

        for (TemperatureSensor device : w1Master.getDevices(TemperatureSensor.class)) {
            System.out.printf("%-20s %3.1f°C %3.1f°F\n", device.getName(), device.getTemperature(),
                    device.getTemperature(TemperatureScale.FARENHEIT));
        }

        System.out.println("Exiting W1TempExample");
    }
}
