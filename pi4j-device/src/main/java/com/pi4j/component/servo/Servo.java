package com.pi4j.component.servo;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  Servo.java  
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


import com.pi4j.component.Component;

/**
 * Represents characteristics/settings of an R/C-Servo.
 * 
 * @author Christian Wehrli
 */
public interface Servo extends Component {

    //------------------------------------------------------------------------------------------------------------------
    // Limits
    //------------------------------------------------------------------------------------------------------------------
    float POS_MAX_LEFT = -100.0f; // Consider this to be the max. left position of an R/C-Radio stick
    float POS_NEUTRAL = 0.0f; // Consider this to be the neutral position of an R/C-Radio stick
    float POS_MAX_RIGHT = 100.0f; // Consider this to be the max. right position of an R/C-Radio stick

    float END_POINT_MIN = 0.0f;
    float END_POINT_MAX = 150.0f;

    float SUBTRIM_MAX_LEFT = -200.0f;
    float SUBTRIM_NEUTRAL = 0.0f;
    float SUBTRIM_MAX_RIGHT = 200.0f;

    //------------------------------------------------------------------------------------------------------------------
    // Defaults
    //------------------------------------------------------------------------------------------------------------------
    String PROP_END_POINT_DEFAULT = "100.0";

    String PROP_SUBTRIM_DEFAULT = Float.toString(SUBTRIM_NEUTRAL);

    String PROP_IS_REVERSE_DEFAULT = "false";

    //------------------------------------------------------------------------------------------------------------------
    // Properties
    //------------------------------------------------------------------------------------------------------------------
    /** 
     * Integer value between 0 and 150.<br>
     *   0: no travel<br>
     * 150: max. travel (0.9ms pulse)
     */
    String PROP_END_POINT_LEFT = "endPointLeft";

    /** 
     * Integer value between 0 and 150.<br>
     *   0: no travel<br>
     * 150: max. travel (2.1ms pulse)
     */
    String PROP_END_POINT_RIGHT = "endPointRight";

    /**
     * Value between -200 and +200<p>
     * -200: neutral position changed to 1.3ms<br>
     *    0: neutral position unchanged at 1.5ms<br>
     *  200: neutral position changed to 1.7ms<p>
     *  Endpoints will be adjusted accordingly!
     */
    String PROP_SUBTRIM = "subtrim";

    /**
     * If TRUE servo travelling direction is reversed.<p>
     * <b>Note:</b> subtrim and endpoints will not change!
     */
    String PROP_IS_REVERSE = "isReverse";

    /**
     * Sets the servos desired position by providing a percentage value.<p>
     * E.g.: a position value of -100 would force the servo to travel to its max. left position as defined in 
     *       property {@link #PROP_END_POINT_LEFT}.
     * @param position value between -100 and +100 according to {@link #POS_MAX_LEFT} respectively {@link #POS_MAX_RIGHT}.
     */
    void setPosition(float position);

    /**
     * @return current position value between -100 and +100[%]
     */
    float getPosition();

    /**
     * Returns servo driver this servo is attached to
     * @return servo driver
     */
    ServoDriver getServoDriver();
}
