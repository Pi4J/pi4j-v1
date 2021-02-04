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
