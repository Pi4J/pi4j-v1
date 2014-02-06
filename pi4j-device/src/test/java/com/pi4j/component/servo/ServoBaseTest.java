package com.pi4j.component.servo;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  ServoBaseTest.java  
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

import static org.junit.Assert.assertEquals;

import com.pi4j.component.servo.Servo;
import com.pi4j.component.servo.ServoBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.pi4j.component.servo.ServoBase.Orientation;

/*
 * Class:     ServoBaseTest
 * Created:   May 1, 2013
 *
 * @author Christian Wehrli
 * @version 1.0, May 1, 2013
 */
public class ServoBaseTest {

    //------------------------------------------------------------------------------------------------------------------
    // calculatePwmDuration()
    //------------------------------------------------------------------------------------------------------------------
    @Test
    public void testCalculatePwmDurationTESTmidLeft() {
        // Setup fixture
        int positionMidLeft = -50;
        // Exercise SUT
        int actual = sut.calculatePwmDuration(positionMidLeft);
        // Verify outcome
        int expectedPwmValue = 1300; // 1500 + ((1500-900) / 150*100 / 100*(-50))
        assertEquals(expectedPwmValue, actual);
    }

    @Test
    public void testCalculatePwmDurationTESTfullRight() {
        // Setup fixture
        int positionFullRight = 100;
        // Exercise SUT
        int actual = sut.calculatePwmDuration(positionFullRight);
        // Verify outcome
        int expectedPwmValue = 1900; // 1500 + ((2100-1500) / 150*100 / 100*100)
        assertEquals(expectedPwmValue, actual);
    }

    @Test
    public void testCalculatePwmDurationTESTsubtrimAndMaxTravel() {
        // Setup fixture
        sut.setProperty(Servo.PROP_END_POINT_RIGHT, "150");
        sut.setProperty(Servo.PROP_SUBTRIM, "-200");
        int positionMidRight = 50;
        // Exercise SUT
        int actual = sut.calculatePwmDuration(positionMidRight);
        // Verify outcome
        int expectedPwmValue = 1600; // 1300 + ((2100-1500) / 150*150 / 100*50)
        assertEquals(expectedPwmValue, actual);
    }

    @Test
    public void testCalculatePwmDurationTESTisReversed() {
        // Setup fixture
        sut.setProperty(Servo.PROP_IS_REVERSE, Boolean.TRUE.toString());
        int positionMidLeft = -50;
        // Exercise SUT
        int actual = sut.calculatePwmDuration(positionMidLeft);
        // Verify outcome
        int expectedPwmValue = 1700; // 1500 + ((2100-1500) / 150*100 / 100*50)
        assertEquals(expectedPwmValue, actual);
    }

    @Test
    public void testCalculatePwmDurationTESTisReversedAndSubtrim() {
        // Setup fixture
        sut.setProperty(Servo.PROP_IS_REVERSE, Boolean.TRUE.toString());
        sut.setProperty(Servo.PROP_SUBTRIM, "-125");
        int positionMidRight = 50;
        // Exercise SUT
        int actual = sut.calculatePwmDuration(positionMidRight);
        // Verify outcome
        int expectedPwmValue = 1175; // 1375 + ((2100-1500) / 150*100 / 100*(-50))
        assertEquals(expectedPwmValue, actual);
    }

    @Test
    public void testCalculatePwmDurationTESTisReversedAndSubtrimAndCustomEndpoint() {
        // Setup fixture
        sut.setProperty(Servo.PROP_IS_REVERSE, Boolean.TRUE.toString());
        sut.setProperty(Servo.PROP_SUBTRIM, "-125");
        sut.setProperty(Servo.PROP_END_POINT_LEFT, "125");
        // -> this combination of subtrim and endpoint would lead into a left-side endpoint-overflow, therefore
        //    max. left position is limited to 900us!
        int positionMidRight = 50;
        // Exercise SUT
        int actual = sut.calculatePwmDuration(positionMidRight);
        // Verify outcome
        int expectedPwmValue = 1138; // 1375 + ((1375-900) / 100*(-50))
        assertEquals(expectedPwmValue, actual);
    }

    @Test
    public void testCalculatePwmDurationTESTsmallTravel() {
        // Setup fixture
        sut.setProperty(Servo.PROP_END_POINT_LEFT, "10");
        int positionFullLeft = -100;
        // Exercise SUT
        int actual = sut.calculatePwmDuration(positionFullLeft);
        // Verify outcome
        int expectedPwmValue = 1460; // 1500 + ((1500-900) / 150*10 / 100*(-100))
        assertEquals(expectedPwmValue, actual);
    }

    //------------------------------------------------------------------------------------------------------------------
    // calculateNeutralPwmDuration()
    //------------------------------------------------------------------------------------------------------------------
    @Test
    public void testCalculateNeutralPwmDurationTESTnoSubtrim() {
        // Setup fixture
        sut.setProperty(Servo.PROP_SUBTRIM, "0");
        // Exercise SUT
        int actual = sut.calculateNeutralPwmDuration();
        // Verify outcome
        Assert.assertEquals(ServoBase.PWM_NEUTRAL, actual);
    }

    @Test
    public void testCalculateNeutralPwmDurationTESTsubtrimMaxLeft() {
        // Setup fixture
        sut.setProperty(Servo.PROP_SUBTRIM, "-200");
        // Exercise SUT
        int actual = sut.calculateNeutralPwmDuration();
        // Verify outcome
        assertEquals(ServoBase.PWM_NEUTRAL - 200, actual);
    }

    @Test
    public void testCalculateNeutralPwmDurationTESTsubtrimMaxRight() {
        // Setup fixture
        sut.setProperty(Servo.PROP_SUBTRIM, "200");
        // Exercise SUT
        int actual = sut.calculateNeutralPwmDuration();
        // Verify outcome
        assertEquals(ServoBase.PWM_NEUTRAL + 200, actual);
    }

    //------------------------------------------------------------------------------------------------------------------
    // calculateEndPointPwmDuration()
    //------------------------------------------------------------------------------------------------------------------
    @Test
    public void testCalculateEndPointPwmDurationTESTfullLeft() {
        // Setup fixture
        sut.setProperty(Servo.PROP_END_POINT_LEFT, "150");
        // Exercise SUT
        int actual = sut.calculateEndPointPwmDuration(Orientation.LEFT);
        // Verify outcome
        assertEquals(900, actual);
    }

    @Test
    public void testCalculateEndPointPwmDurationTEST100PercentLeft() {
        // Setup fixture
        sut.setProperty(Servo.PROP_END_POINT_LEFT, "100");
        // Exercise SUT
        int actual = sut.calculateEndPointPwmDuration(Orientation.LEFT);
        // Verify outcome
        assertEquals(1100, actual);
    }

    @Test
    public void testCalculateEndPointPwmDurationTEST33PercentLeft() {
        // Setup fixture
        sut.setProperty(Servo.PROP_END_POINT_LEFT, "33");
        // Exercise SUT
        int actual = sut.calculateEndPointPwmDuration(Orientation.LEFT);
        // Verify outcome
        assertEquals(1368, actual);
    }

    @Test
    public void testCalculateEndPointPwmDurationTESTzeroTravelLeft() {
        // Setup fixture
        sut.setProperty(Servo.PROP_END_POINT_LEFT, "0");
        // Exercise SUT
        int actual = sut.calculateEndPointPwmDuration(Orientation.LEFT);
        // Verify outcome
        assertEquals(1500, actual);
    }

    @Test
    public void testCalculateEndPointPwmDurationTESTzeroTravelRight() {
        // Setup fixture
        sut.setProperty(Servo.PROP_END_POINT_RIGHT, "0");
        // Exercise SUT
        int actual = sut.calculateEndPointPwmDuration(Orientation.RIGHT);
        // Verify outcome
        assertEquals(1500, actual);
    }

    @Test
    public void testCalculateEndPointPwmDurationTEST100PercentRight() {
        // Setup fixture
        sut.setProperty(Servo.PROP_END_POINT_RIGHT, "100");
        // Exercise SUT
        int actual = sut.calculateEndPointPwmDuration(Orientation.RIGHT);
        // Verify outcome
        assertEquals(1900, actual);
    }

    @Test
    public void testCalculateEndPointPwmDurationTEST66PercentRight() {
        // Setup fixture
        sut.setProperty(Servo.PROP_END_POINT_RIGHT, "66");
        // Exercise SUT
        int actual = sut.calculateEndPointPwmDuration(Orientation.RIGHT);
        // Verify outcome
        assertEquals(1764, actual);
    }

    @Test
    public void testCalculateEndPointPwmDurationTESTfullRight() {
        // Setup fixture
        sut.setProperty(Servo.PROP_END_POINT_RIGHT, "150");
        // Exercise SUT
        int actual = sut.calculateEndPointPwmDuration(Orientation.RIGHT);
        // Verify outcome
        assertEquals(2100, actual);
    }

    @Test
    public void testCalculateEndPointPwmDurationTESTsubtrimLeft() {
        // Setup fixture
        sut.setProperty(Servo.PROP_SUBTRIM, "-200");
        // Exercise SUT
        int actualLeft = sut.calculateEndPointPwmDuration(Orientation.LEFT);
        int actualRight = sut.calculateEndPointPwmDuration(Orientation.RIGHT);
        // Verify outcome
        assertEquals(900, actualLeft);
        assertEquals(1700, actualRight);
    }

    @Test
    public void testCalculateEndPointPwmDurationTESTsubtrimRight() {
        // Setup fixture
        sut.setProperty(Servo.PROP_SUBTRIM, "50");
        // Exercise SUT
        int actualLeft = sut.calculateEndPointPwmDuration(Orientation.LEFT);
        int actualRight = sut.calculateEndPointPwmDuration(Orientation.RIGHT);
        // Verify outcome
        assertEquals(1150, actualLeft);
        assertEquals(1950, actualRight);
    }

    @Test
    public void testCalculateEndPointPwmDurationTESTmaxSubtrimLeftMaxEndpoints() {
        // Setup fixture
        sut.setProperty(Servo.PROP_SUBTRIM, "-200");
        sut.setProperty(Servo.PROP_END_POINT_LEFT, "150");
        sut.setProperty(Servo.PROP_END_POINT_RIGHT, "150");
        // Exercise SUT
        int actualLeft = sut.calculateEndPointPwmDuration(Orientation.LEFT);
        int actualRight = sut.calculateEndPointPwmDuration(Orientation.RIGHT);
        // Verify outcome
        assertEquals(900, actualLeft);
        assertEquals(1900, actualRight);
    }

    @Test
    public void testCalculateEndPointPwmDurationTESTsubtrimRightCustomEndpoints() {
        // Setup fixture
        sut.setProperty(Servo.PROP_SUBTRIM, "75");
        sut.setProperty(Servo.PROP_END_POINT_LEFT, "125");
        sut.setProperty(Servo.PROP_END_POINT_RIGHT, "80");
        // Exercise SUT
        int actualLeft = sut.calculateEndPointPwmDuration(Orientation.LEFT);
        int actualRight = sut.calculateEndPointPwmDuration(Orientation.RIGHT);
        // Verify outcome
        assertEquals(1075, actualLeft);
        assertEquals(1895, actualRight);
    }

    @Test
    public void testCalculateEndPointPwmDurationTESTsubtrimRightCausesEndpointOverflow() {
        // Setup fixture
        sut.setProperty(Servo.PROP_SUBTRIM, "150");
        sut.setProperty(Servo.PROP_END_POINT_RIGHT, "140");
        // Exercise SUT
        int actual = sut.calculateEndPointPwmDuration(Orientation.RIGHT);
        // Verify outcome
        assertEquals(2100, actual);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Setup
    //------------------------------------------------------------------------------------------------------------------
    private ServoBase sut;

    @Before
    public void setUp() throws Exception {
        sut = new TestServoBase();
    }

    private class TestServoBase extends ServoBase {

        public TestServoBase() {
            init();
        }
    }
}
