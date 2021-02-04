
 Pi4J :: Java I/O Library for Raspberry Pi
==========================================================================
[![Build Status](https://travis-ci.org/Pi4J/pi4j.svg?branch=master)](https://travis-ci.org/Pi4J/pi4j?branch=master) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.pi4j/pi4j-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.pi4j/pi4j-core) ![Snyk Security Badge](https://snyk-widget.herokuapp.com/badge/mvn/com.pi4j/pi4j-core/badge.svg)

## PROJECT INFORMATION

Project website: https://pi4j.com/ <br />
Pi4J Community Forum (*new*): https://forum.pi4j.com/ <br />
Version 2.0 Project Discussions (*new*): https://forum.pi4j.com/category/6/version-2-0 <br />
Project issues list: https://github.com/Pi4J/pi4j/issues <br />
<br />
Release builds are available from:
   *  [Maven Central] http://search.maven.org/#search%7Cga%7C1%7Ccom.pi4j
   *  [Downloads] https://pi4j.com/download.html

Snapshot builds are available from:
   *  [Sonatype OSS] https://oss.sonatype.org/index.html#nexus-search;quick~pi4j
   *  [Downloads] https://pi4j.com/download.html
   
 
Copyright (C) 2012-2021 Pi4J

## LICENSE

 Pi4J Version 1.4 and later is licensed under the Apache License,
 Version 2.0 (the "License"); you may not use this file except in
 compliance with the License.  You may obtain a copy of the License at:

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.


## PROJECT OVERVIEW

  Starting with the Pi4J 1.4 builds, the Pi4J project is prioritizing focus 
  on providing Java programs access, control and communication with the core 
  I/O capabilities of the Raspberry Pi platform.  Earlier versions of Pi4J
  were perhaps too ambitious in scope and that led to significant project
  bloat to the point that the project was becoming unsustainable.  The goal
  moving forward is to limit scope to that of the raw I/O capabilities 
  of the Raspberry Pi platform and provide timely updates and releases for
  bug fixes and new RaspberryPi model introductions.  Reducing the scope of 
  the project should better serve the Java community for basic I/O access by
  reducing complexity.
  
  The following features have been removed from the Pi4J library:
  
  * **IO Expanders** -- IO expansion is still supported but concrete 
  implementations should be provided outside the core Pi4J core project such 
  that they can be maintained and extended independently.
   
  * **Other Platforms** -- Other platforms such as Odroid, BananaPi, NanoPi, 
  OrangePi, etc. have been removed and will no longer be supported.  The  
  challenge with supporting these additional platforms is that Pi4J depends on  
  the underlying WiringPi project and WiringPi ports for these other platforms 
  is not well supported by the various SoC vendors or community.  The various 
  WiringPi ports for these other platforms are also inconsistent causing 
  inconsistent features and functionality of Pi4J.  Additionally, regression
  testing of bug fixes and new features in Pi4J is compounded with each 
  additional supported platform.
  
  * **Components & Devices** -- Pi4J originally provided higher level 
  interfaces for components and devices that provided an abstraction
  layer between real world devices (things) and lower-level I/O interfaces.  
  While a noble goal, unfortunately this part of the project never received 
  the attention and time that it deserved and never gained much adoption
  by the community.  We are removing these to allow Pi4J to focus solely on
  the raw I/O supported by the Raspberry Pi platform.

## KNOWN ISSUES

  *  The original WiringPi library has been **DEPRECATED**. \
     _(See: http://wiringpi.com/wiringpi-deprecated/)_

     > **NOTE:**  To support RaspberryPi 4B and newer systems you must install the latest *unofficial* WiringPi version which is 
     maintained here: https://github.com/WiringPi/WiringPi.  _(As of 2021-01-12, this is version 2.60.)_
     
     Example installation commands on a RaspberryPi:
     ```
     sudo apt-get remove wiringpi -y
     sudo apt-get --yes install git-core gcc make
     cd ~
     git clone https://github.com/WiringPi/WiringPi --branch master --single-branch wiringpi
     cd ~/wiringpi
     sudo ./build
     ```
  *  Please note that the Pi4J v1.x codebase is no longer being actively developed. Pi4J v1.x will only be maintained
     and updated for major bug fixes. This codebase has been largely stable for several years and is compatible across
     a wide variety of Raspberry Pi SoCs and you are welcome to continue using it.  However, for new projects, it
     is highly recommended to migrate to the Pi4J Version 2.x codebase.  See https://v2.pi4j.com for more information.

  *  This project requires Java 11 (JDK) to build; however, it fails to compile using the following Oracle JDKs.  The 
     project will successfully compile using OpenJDK, Liberica, Zulu and AdoptOpenJDK distributions.
     - Oracle JDK 11.0.06
     - Oracle JDK 11.0.07
     - Oracle JDK 11.0.09
     - Oracle JDK 11.0.10
     - Oracle JDK 11.0.10 (_and possibly newer_)
    
     \
     Example installation command on a RaspberryPi:
     ```
     sudo apt-get install openjdk-11-jdk
     ```

## Building

Please see the [BUILD.md](BUILD.md) instructions for details on how to compile/build this project.

## IN DEVELOPMENT

2020-06-20 :: 1.4-SNAPSHOT

  * Changed project to Apache License, Version 2.0
  * Added support for 64-bit architecture.
  * Removed `pi4j-device` library.  _(See comments above)_
  * Removed `pi4j-gpio-extension` library.  _(See comments above)_
  * Removed platform support for `Odroid`.  _(See comments above)_
  * Removed platform support for `BananaPi`.  _(See comments above)_
  * Removed platform support for `BPi`.  _(See comments above)_
  * Removed platform support for `NanoPi`.  _(See comments above)_
  * Removed platform support for `OrangePi`.  _(See comments above)_


## PREVIOUS RELEASES

For previous 1.x release notes and source code, please see the 1.x branch
or release tags in the source repository:

  * **Releases**: https://github.com/Pi4J/pi4j/releases
  * **Source Code**: https://github.com/Pi4J/pi4j/branches
