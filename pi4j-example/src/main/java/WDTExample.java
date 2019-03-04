/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  WDTExample.java
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

import com.pi4j.io.wdt.WDTimer;
import com.pi4j.io.wdt.impl.WDTimerImpl;
import java.io.IOException;

/**
 *
 * @author zerog
 */
public class WDTExample {
    public static void main(String[] args) throws IOException, InterruptedException {

        //get watchdog instance
        WDTimer watchdog =  WDTimerImpl.getInstance();

        watchdog.open();
        System.out.println("WatchDog working!");

        int timeout = watchdog.getTimeOut();
        System.out.println("Timeout of WatchDog is "+timeout);

        //set new timeout
        watchdog.setTimeOut(15);
        timeout = watchdog.getTimeOut();
        System.out.println("Timeout of WatchDog is "+timeout);

        //4x ping watchdog
        for (int i = 0; i < 4; i++) {
            watchdog.heartbeat();
            System.out.println("Watchdog heartbeat (pinging)");
            Thread.sleep(1000*13);
        }


        watchdog.disable(); //comment this line causes RaspberryPi reboot
        System.out.println("WatchDog disabled!");

        watchdog.close();
        System.out.println("WatchDog closed.");
    }
}
