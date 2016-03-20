package com.pi4j.util;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  AppUtil.java
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

public class AppUtil {

    private static boolean exitPending = false;

    public synchronized static void waitForExit(){
        System.out.println("[[-- PRESS CTRL-C TO EXIT --]]");
        AppUtil.exitPending = false;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                AppUtil.exitPending = true;
                System.out.println("[[-- GOODBYE --]]");
            }
        });
    }

//    public synchronized static void waitForExitAsync(){
//        (new Thread(new ExitRunnable())).start();
//    }

    public synchronized static boolean isExitPending(){
        return exitPending;
    }

//    private static class ExitRunnable implements Runnable {
//        public void run() {
//            AppUtil.waitForExit();
//        }
//    }
}
