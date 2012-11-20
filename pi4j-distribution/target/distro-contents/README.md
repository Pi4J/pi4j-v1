==========================================================================
 Pi4J :: Java library for Raspberry Pi
==========================================================================

## PROJECT INFORMATION

 Project website: (http://www.pi4j.com/) 
 Release builds are available from [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ccom.pi4j).

 Copyright (C) 2012 Pi4J

## LICENSE
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0
  
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 
## FUTURE PLANS

   *  Add Java API support for SPI communication. (JNI wrapper for wiringPi SPI already exists)
   *  Enhance/replace serial communication API.
   *  Create additional GpioProvider implementations
   *  Create device abstraction implementations

## RELEASES

2012-11-19 :: 0.0.4-SNAPSHOT
 
   *  Added support for building a Pi4J .deb installer package for Debian/Raspian Linux distribution. (BETA)
   *  Modified NetworkInfo static methods to permit empty array return values when a network property cannot be obtained.
      https://github.com/Pi4J/pi4j/issues/3
   *  Fixed issue in newer Raspberry Pi 512mb firmware where Pi4J was not compiled against latest wiringPi library and thus not detecting the hardware properly.
      https://github.com/Pi4J/pi4j/issues/3
   *  Fixed defect where Pi4J library prevented consuming program from exiting properly.
      https://github.com/Pi4J/pi4j/issues/4 
      https://github.com/Pi4J/pi4j/issues/6

2012-10-22 :: 0.0.3-SNAPSHOT
 
   *  Refactored Gpio interfaces to permit extensible GpioProviders for add-on devices.
   *  Refactored Gpio interfaces to separate digital, analog, and PWM types of input and output pins.
   *  Added support for analog GPIO pins (interfaces, methods and events). 
   *  Added Olimex AVR-IO-M16 GpioProvider to serve as an example of extending a new GpioProvider for GPIO expansion. 
   *  Added Olimex AVR-IO-M16 sources for ARV micro-controller.
   *  Removed setDirection() from GpioPin; embedded this functionality into existing setMode() method.  Eliminating duplication.
   *  Removed old test programs from pi4j-core and moved wiringPi examples to pi4j-example project.  
   *  Added support for I2C communication.
   *  Added blink() methods for GPIO pins


2012-09-23 :: 0.0.2-SNAPSHOT
 
   *  Refactored Gpio interface to permit methods using Pin argument to function without requiring a GpioPin to first be provisioned
   *  Added OSGi manifest 
   *  Optimized GPIO interrupt thread handling to only be created if GpioPin listeners or triggers have been registered.
   *  Now disabling/stopping GPIO interrupt native thread monitoring if all GpioPin listeners or triggers are unregistered.
   *  Added additional convenience methods in GpioPin interface for evaluating state, direction, resistance, mode, etc. 
   *  There were two Gpio classes; renamed one to GpioController to avoid confusion.
   *  Added OSGi service project to export Pi4J GPIO as an OSGi service.  


2012-09-19 :: 0.0.1-SNAPSHOT
 
   *  Initial ALPHA release
 
 