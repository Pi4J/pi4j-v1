package com.pi4j.component.sensor;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  DistanceSensorBase.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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


import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import com.pi4j.component.ComponentListener;
import com.pi4j.component.ObserveableComponentBase;


public abstract class DistanceSensorBase extends ObserveableComponentBase implements DistanceSensor {

    protected Date lastDistanceTimestamp = null;
    protected SortedMap<Double, Double> coordinates = new TreeMap<>();

    @Override
    public Date getLastDistanceTimestamp() {
        return lastDistanceTimestamp;
    }

    @Override
    public void addListener(DistanceSensorListener... listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(DistanceSensorListener... listener) {
        super.removeListener(listener);
    }

    protected synchronized void notifyListeners(DistanceSensorChangeEvent event) {
        // cache last detected timestamp
        lastDistanceTimestamp = event.timestamp;

        // raise events to listeners
        for(ComponentListener listener : super.listeners) {
            ((DistanceSensorListener)listener).onDistanceChange(event);
        }
    }

    @Override
    public boolean isValue(double value) {
        return (getValue() == value);
    }

    @Override
    public boolean isValueInRange(double min, double max){
        double value = getValue();
        return (value >= min && value <= max);
    }

    @Override
    public double getDistance(){
        return getDistance(getValue());
    }

    @Override
    public double getDistance(double value){
        Double lower = null;
        Double upper = null;

        for(Double coordinate : coordinates.keySet()){

            if(value == coordinate){
                // return distance value
                return coordinates.get(coordinate);
            }
            else if(value > coordinate){
                // set lower coordinate
                lower = coordinate;
                //System.out.println("LOWER: " + coordinate);
            }
            else if(value < coordinate){
                // set upper coordinate
                upper = coordinate;
                //System.out.println("UPPER: " + coordinate);
                break;
            }
        }


        // out of range - below minimum distance
        if(lower == null)
            return coordinates.get(coordinates.firstKey());

        // out of range - over maximum distance
        if(upper == null)
            return coordinates.get(coordinates.lastKey());

        // get the minimum and maximum distances in range
        Double minDistance = coordinates.get(lower);
        Double maxDistance = coordinates.get(upper);

        // calculate the percentage difference
        double diffPercentage = (value-lower)/(upper-lower);
        double diffDistance = (maxDistance - minDistance) * diffPercentage;

        // return the minimum range distance plus the calculated percentage difference
        return minDistance + diffDistance;
    }

    @Override
    public boolean isDistance(double distance) {
        return (getDistance() == distance);
    }

    @Override
    public boolean isDistanceInRange(double min, double max){
        double distance = getDistance();
        return (distance >= min && distance <= max);
    }

    @Override
    public void addCalibrationCoordinate(double value, double distance){
        coordinates.put(value, distance);
    }
}
