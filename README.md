
 Pi4J :: Java I/O Library for Raspberry Pi
==========================================================================
[![Build Status](https://travis-ci.org/Pi4J/pi4j.svg?branch=master)](https://travis-ci.org/Pi4J/pi4j?branch=master) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.pi4j/pi4j-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.pi4j/pi4j-core)

## PROJECT INFORMATION

Project website: https://pi4j.com/ <br />
Project discussion group: https://groups.google.com/forum/#!forum/pi4j <br />
Project issues list: https://github.com/Pi4J/pi4j/issues <br />
<br />
Release builds are available from:
   *  [Maven Central] http://search.maven.org/#search%7Cga%7C1%7Ccom.pi4j
   *  [Downloads] https://pi4j.com/download.html

Snapshot builds are available from:
   *  [Sonatype OSS] https://oss.sonatype.org/index.html#nexus-search;quick~pi4j
   *  [Downloads] https://pi4j.com/download.html


Copyright (C) 2012-2019 Pi4J

## LICENSE

 Pi4J Version 2.0 and later is licensed under the Apache License,
 Version 2.0 (the "License"); you may not use this file except in
 compliance with the License.  You may obtain a copy of the License at:
      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.


## PROJECT OVERVIEW

  Starting with the Pi4J 2.0 builds, the Pi4J project is prioritizing focus
  on providing Java programs access, control and communication with the core
  I/O capabilities of the Raspberry Pi platform.  Earlier versions of Pi4J
  were perhaps too ambitious in scope and that led to significant project
  bloat to the point that the project was becoming unsustainable.  The goal
  moving forward is to limit scope to that of the raw I/O capabilities
  of the Raspberry Pi platform and provide timely updates and releases for
  bug fixed and new RaspberryPi model introductions.  Reducing the scope of
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


## IN DEVELOPMENT

2019-02-27 :: 2.0-SNAPSHOT

  * Changed project to Apache License, Version 2.0
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
