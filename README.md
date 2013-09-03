==========================================================================
 Riftware/Pi4J :: Fork of Java library for Raspberry Pi
==========================================================================

## PROJECT INFORMATION

Project website: http://www.pi4j.com/ <br />


This fork is for the purpose of adding additional items to pi4j.   My ultimate goal is to 
have my changes pulled back to the pi4j project.   So far contributions include:

com.pi4j.component.lcd.impl.I2CLcdDisplay     - A class that follows the LCD interface
but permits working with i2cDisplays including sainsmart and normal pcf8574 implementation.
Each Pin is defineable in the constructor so weird wiring should be fully supported.
