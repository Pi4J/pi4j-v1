package com.pi4j.util;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Console.java
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



public class Console {

    private static final int LINE_WIDTH = 60;
    public static final String CLEAR_SCREEN_ESCAPE_SEQUENCE = "\033[2J\033[1;1H";
    public static final String ERASE_LINE_ESCAPE_SEQUENCE = "\033[K";

    public static final char LINE_SEPARATOR_CHAR = '*';
    public static final String LINE_SEPARATOR = StringUtil.repeat(LINE_SEPARATOR_CHAR, LINE_WIDTH);

    protected boolean exiting = false;

    public synchronized Console println(String format, Object ... args){
        return println(String.format(format, args));
    }

    public synchronized Console print(String format, Object ... args){
        return print(String.format(format, args));
    }

    public synchronized Console println(String line){
        System.out.println(line);
        return this;
    }

    public synchronized Console println(Object line){
        System.out.println(line);
        return this;
    }

    public synchronized Console println(){
        return println("");
    }

    public synchronized Console print(Object data){
        System.out.print(data);
        return this;
    }

    public synchronized Console print(String data){
        System.out.print(data);
        return this;
    }

    public synchronized Console println(char character, int repeat){
        return println(StringUtil.repeat(character, repeat));
    }

    public synchronized Console emptyLine(){
        return emptyLine(1);
    }

    public synchronized Console emptyLine(int number){
        for(int index = 0; index < number; index++){
            println();
        }
        return this;
    }

    public synchronized Console separatorLine(){
        return println(LINE_SEPARATOR);
    }

    public synchronized Console separatorLine(char character){
        return separatorLine(character, LINE_WIDTH);
    }

    public synchronized Console separatorLine(char character, int length){
        return println(StringUtil.repeat(character, length));
    }

    public synchronized Console title(String ... title){
        clearScreen().separatorLine().separatorLine().emptyLine();
        for(String s : title) {
            println(StringUtil.center(s, LINE_WIDTH));
        }
        emptyLine().separatorLine().separatorLine().emptyLine();
        return this;
    }

    public synchronized Console box(String ... lines) {
        return box(2, lines);
    }

    public synchronized Console box(int padding, String ... lines) {
        int max_length = 0;
        for(String l : lines) {
            if (l.length() > max_length) {
                max_length = l.length();
            }
        }
        separatorLine('-', max_length + padding * 2 + 2);
        String left  = StringUtil.padRight("|", padding);
        String right = StringUtil.padLeft("|", padding);
        for(String l : lines){
            println(StringUtil.concat(left, StringUtil.padRight(l, max_length - l.length()), right));
        }
        separatorLine('-', max_length + padding * 2 + 2);
        return this;
    }

    public synchronized Console goodbye() {
        emptyLine();
        separatorLine();
        println(StringUtil.center("GOODBYE", LINE_WIDTH));
        separatorLine();
        emptyLine();
        return this;
    }

    public synchronized Console clearScreen(){
        return print(CLEAR_SCREEN_ESCAPE_SEQUENCE);
    }

    public synchronized Console eraseLine(){
        return print(ERASE_LINE_ESCAPE_SEQUENCE);
    }

    public synchronized Console promptForExit(){
        box(4, "PRESS CTRL-C TO EXIT");
        emptyLine();
        exiting = false;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                exiting = true;
                goodbye();
            }
        });
        return this;
    }

    public void waitForExit() throws InterruptedException {
        while(!exiting){
            Thread.sleep(50);
        }
    }

    public synchronized boolean exiting(){
        return exiting;
    }
    public synchronized boolean isRunning(){
        return !exiting;
    }
}
