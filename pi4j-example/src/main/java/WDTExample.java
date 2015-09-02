/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  WDTExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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

import com.pi4j.io.wdt.WDTimer;
import com.pi4j.io.wdt.impl.WDTimerImpl;
import java.io.IOException;

/*
 * Copyright 2015 Pi4J.
 *
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
 */

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
