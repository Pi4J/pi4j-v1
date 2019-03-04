package com.pi4j.util;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  ConsoleColor.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
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
public enum ConsoleColor {

    RESET("\033[0m"),
    BLACK("\033[30m"),
    RED("\033[31m"),
    GREEN("\033[32m"),
    YELLOW("\033[33m"),
    BLUE("\033[34m"),
    MAGENTA("\033[35m"),
    CYAN("\033[36m"),
    WHITE("\033[37m");

    private final String escapeSequence;

    private ConsoleColor(String escapeSequence) {
        this.escapeSequence = escapeSequence;
    }

    public static CharSequence build(ConsoleColor color, Object ... data){
        return build(color, true, data);
    }

    public static CharSequence build(ConsoleColor color, boolean reset, Object ... data){
        StringBuilder sb = new StringBuilder(color.getEscapeSequence());
        for(Object d : data){
            sb.append(d);
        }
        if(reset) sb.append(RESET.getEscapeSequence());
        return sb.toString();
    }

    public static CharSequence conditional(boolean condition, ConsoleColor positive, ConsoleColor negative, Object ... data) {
        if (condition) {
            return positive.build(data);
        }
        else{
            return negative.build(data);
        }
    }

    @Override
    public String toString(){
        return getEscapeSequence();
    }

    public String toString(Object ... data){
        return build(data).toString();
    }

    public String getEscapeSequence(){
        return escapeSequence;
    }

    public CharSequence build(Object ... data){
        return ConsoleColor.build(this, data);
    }

    public CharSequence build(boolean reset, Object ... data){
        return ConsoleColor.build(this, reset, data);
    }
}
