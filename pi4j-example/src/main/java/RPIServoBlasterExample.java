/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  RPIServoBlasterExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
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
import com.pi4j.servo.Servo;
import com.pi4j.servo.ServoDriver;
import com.pi4j.servo.impl.RPIServoBlasterDriver;


public class RPIServoBlasterExample {

    public static void main(String[] args) throws Exception {
        ServoDriver driver = new RPIServoBlasterDriver();
        
        Servo servo7 = driver.getServo(RPIServoBlasterDriver.SERVO_7);
        
        long start = System.currentTimeMillis();
        
        while (System.currentTimeMillis() - start < 120000) { // 2 minutes
            
            for (int i = 50; i < 150; i++) {
                servo7.setRawPosition(i);
                Thread.sleep(50);
            }
            
            for (int i = 150; i > 50; i--) {
                servo7.setRawPosition(i);
                Thread.sleep(50);
            }
            
        }
        
    }
    
}

