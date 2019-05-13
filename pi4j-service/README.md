
 Pi4J :: Service to remotely control a PI
==========================================================================
[![Build Status](https://travis-ci.org/Pi4J/pi4j.svg?branch=master)](https://travis-ci.org/Pi4J/pi4j?branch=master) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.pi4j/pi4j-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.pi4j/pi4j-core)

## PROJECT INFORMATION

See parent README

## LICENSE

See parent README

## HOW TO USE THE SERVICE

### Testing on a non-PI system:
* Based on work done in https://github.com/Pi4J/pi4j/pull/400
* Two environment variables must be defined:
    * PI4J_PLATFORM=Simulated
    * SimulatedPlatform=<Real Platform's Name>
        * E.g. SimulatedPlatform=RaspberryPi GPIO Provider
* To do this in IntelliJ > Run/Debug configurations > Environment variables
    * E.g. "PI4J_PLATFORM=Simulated;SimulatedPlatform=RaspberryPi GPIO Provider" 
    
### REST
* GET 
   * http://localhost:8080/ = "Hello World" to test if the service started successfully
   * http://localhost:8080/pins/state = Get the state of the pins which were already provisioned
* POST 
   * http://localhost:8080/pin/state/set
   * http://localhost:8080/pins/toggle
   * http://localhost:8080/pins/pulse
   
### WebSocket:
