
 Pi4J :: Service to remotely control a PI
==========================================================================
[![Build Status](https://travis-ci.org/Pi4J/pi4j.svg?branch=master)](https://travis-ci.org/Pi4J/pi4j?branch=master) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.pi4j/pi4j-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.pi4j/pi4j-core)

## PROJECT INFORMATION

See parent README

## LICENSE

See parent README

## HOW TO USE THE SERVICE


For testing on a non-PI system:
* Based on work done in https://github.com/Pi4J/pi4j/pull/400
* Two environment variables must be defined:
    * PI4J_PLATFORM=Simulated
    * SimulatedPlatform=<Real Platform's Name>
        * E.g. SimulatedPlatform=RaspberryPi GPIO Provider
* To do this in IntelliJ > Run/Debug configurations > Environment variables
    * E.g. "PI4J_PLATFORM=Simulated;SimulatedPlatform=RaspberryPi GPIO Provider" 

