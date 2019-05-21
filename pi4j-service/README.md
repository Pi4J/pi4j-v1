 Pi4J :: Service to remotely control a PI via REST
==========================================================================
[![Build Status](https://travis-ci.org/Pi4J/pi4j.svg?branch=master)](https://travis-ci.org/Pi4J/pi4j?branch=master) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.pi4j/pi4j-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.pi4j/pi4j-core)

## PROJECT INFORMATION

See parent README

## LICENSE

See parent README

## HOW TO BUILD
With Maven > package

## HOW TO USE THE SERVICE

### Testing on a non-PI system:
Based on work done in https://github.com/Pi4J/pi4j/pull/400
* Two environment variables must be defined:
    * PI4J_PLATFORM=Simulated
    * SimulatedPlatform=<Real Platform's Name>
* To do this in IntelliJ > Run/Debug configurations > Environment variables:
```
PI4J_PLATFORM=Simulated;SimulatedPlatform=RaspberryPi GPIO Provider" 
```
    
### REST
* http://{IP}:8080/
     * Page to show that the service has started successfully.
* http://{IP}:8080/swagger-ui.html
     * Automatic generated documentation and test functions for all REST interfaces.