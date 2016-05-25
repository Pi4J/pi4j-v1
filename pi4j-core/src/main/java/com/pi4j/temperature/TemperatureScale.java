package com.pi4j.temperature;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  TemperatureScale.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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

@SuppressWarnings("unused")
public enum TemperatureScale {

    CELSIUS("Celsius","°C",TemperatureConversion.ABSOLUTE_ZERO_CELSIUS),
    FARENHEIT("Farenheit","°F", TemperatureConversion.ABSOLUTE_ZERO_FARENHEIT),
    KELVIN("Kelvin","K", TemperatureConversion.ABSOLUTE_ZERO_KELVIN),
    RANKINE("Rankine","°R", TemperatureConversion.ABSOLUTE_ZERO_RANKINE);

    private String name;
    private String units;
    private double absoluteZero = 0;

    TemperatureScale(String name, String units, double absoluteZero){
        this.name= name;
        this.units = units;
        this.absoluteZero = absoluteZero;
    }

    public String getName() {
        return name;
    }

    public String getUnits() {
        return units;
    }

    public String getValueString(double temperature) {
        return temperature + " " + units;
    }

    public double getAbsoluteZero() {
        return absoluteZero;
    }

    @Override
    public String toString() {
        return name;
    }

}
