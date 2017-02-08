/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  MaestroServoExample.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2017 Pi4J
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
import com.pi4j.component.servo.ServoProvider;
import com.pi4j.component.servo.impl.MaestroServoDriver;
import com.pi4j.component.servo.impl.MaestroServoProvider;


/**
 * Test program for the Pololu Maestro series of Servo Controllers.
 * @see MaestroServoProvider for details.
 *
 * @author Bob Brodt (rbrodt@gmail.com)
 *
 */
public class MaestroServoExample {

    public static void main(String[] args) throws Exception {
        ServoProvider servoProvider = new MaestroServoProvider();
        MaestroServoDriver servo0 = (MaestroServoDriver) servoProvider.getServoDriver(servoProvider.getDefinedServoPins().get(0));
        long start = System.currentTimeMillis();

		int min = servo0.getMinValue();
		int max = servo0.getMaxValue();
		servo0.setAcceleration(30);

        while (System.currentTimeMillis() - start < 120000) { // 2 minutes
           	servo0.setServoPulseWidth(min);
            Thread.sleep(1500);
           	servo0.setServoPulseWidth(max);
            Thread.sleep(1500);
        }
        System.out.println("Exiting MaestroServoExample");
    }
}

