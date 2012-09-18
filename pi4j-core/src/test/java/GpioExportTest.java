/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioExportTest.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
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
import com.pi4j.wiringpi.GpioUtil;

public class GpioExportTest
{
    public static void main(String args[]) throws InterruptedException
    {
        System.out.println("<--Pi4J--> GPIO export test program");
        
        // set GPIO #07 as the input trigger 
        GpioUtil.export(7, GpioUtil.DIRECTION_IN);
        System.out.println("GPIO 07 is now exported");

        System.out.println("GPIO 07 is exported ? : "+ GpioUtil.isExported(7));
        System.out.println("GPIO 07 edge ? : "+ GpioUtil.getDirection(7));
        
        System.out.println("GPIO 07 edge = 'none'");
        GpioUtil.setEdgeDetection(07,GpioUtil.EDGE_NONE);        
        System.out.println("GPIO 07 edge ? : "+ GpioUtil.getEdgeDetection(7));
        Thread.sleep(5000);
        
        System.out.println("GPIO 07 edge = 'both'");
        GpioUtil.setEdgeDetection(7,GpioUtil.EDGE_BOTH);        
        System.out.println("GPIO 07 edge ? : "+ GpioUtil.getEdgeDetection(7));
        Thread.sleep(5000);
        
        System.out.println("GPIO 07 edge = 'rising'");
        GpioUtil.setEdgeDetection(7,GpioUtil.EDGE_RISING);        
        System.out.println("GPIO 07 edge ? : "+ GpioUtil.getEdgeDetection(7));
        Thread.sleep(5000);
        
        System.out.println("GPIO 07 edge = 'falling'");
        GpioUtil.setEdgeDetection(7,GpioUtil.EDGE_FALLING);        
        System.out.println("GPIO 07 edge ? : "+ GpioUtil.getEdgeDetection(7));
        Thread.sleep(5000);

        System.out.println("GPIO 07 direction = 'out'");
        GpioUtil.setDirection(7, GpioUtil.DIRECTION_OUT);     
        System.out.println("GPIO 07 direction ? : "+ GpioUtil.getDirection(7));
        Thread.sleep(5000);

        System.out.println("GPIO 07 direction = 'in'");
        GpioUtil.setDirection(7, GpioUtil.DIRECTION_IN);     
        System.out.println("GPIO 07 direction ? : "+ GpioUtil.getDirection(7));
        Thread.sleep(5000);
        
        GpioUtil.unexport(7);
        System.out.println("GPIO 07 is now unexported");
        
        System.out.println("GPIO 07 is exported ? : "+ GpioUtil.isExported(7));        
    }
}

