#!/bin/sh

###
# #%L
# **********************************************************************
# ORGANIZATION  :  Pi4J
# PROJECT       :  Pi4J :: Java Examples
# FILENAME      :  build.sh  
# 
# This file is part of the Pi4J project. More information about 
# this project can be found here:  http://www.pi4j.com/
# **********************************************************************
# %%
# Copyright (C) 2012 Pi4J
# %%
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#      http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# #L%
###

echo "---------------------------------------------------------"

# THE FOLLOWING INSTRUCTIONS WILL COMPILE ALL THE EXAMPLE PROJECTS
echo "[01 of 11] ... compiling : BlinkGpioExample.java"
javac -classpath .:classes:/opt/pi4j/lib/'*' -d . BlinkGpioExample.java

echo "[02 of 11] ... compiling : BlinkTriggerGpioExample.java"
javac -classpath .:classes:/opt/pi4j/lib/'*' -d . BlinkTriggerGpioExample.java

echo "[03 of 11] ... compiling : ControlGpioExample.java"
javac -classpath .:classes:/opt/pi4j/lib/'*' -d . ControlGpioExample.java

echo "[04 of 11] ... compiling : I2CWiiMotionPlusExample.java"
javac -classpath .:classes:/opt/pi4j/lib/'*' -d . I2CWiiMotionPlusExample.java

echo "[05 of 11] ... compiling : ListenGpioExample.java"
javac -classpath .:classes:/opt/pi4j/lib/'*' -d . ListenGpioExample.java

echo "[06 of 11] ... compiling : OlimexGpioExample.java"
javac -classpath .:classes:/opt/pi4j/lib/'*' -d . OlimexGpioExample.java

echo "[07 of 11] ... compiling : SerialExample.java"
javac -classpath .:classes:/opt/pi4j/lib/'*' -d . SerialExample.java

echo "[08 of 11] ... compiling : ShutdownGpioExample.java"
javac -classpath .:classes:/opt/pi4j/lib/'*' -d . ShutdownGpioExample.java

echo "[09 of 11] ... compiling : SystemInfoExample.java"
javac -classpath .:classes:/opt/pi4j/lib/'*' -d . SystemInfoExample.java

echo "[10 of 11] ... compiling : TriggerGpioExample.java"
javac -classpath .:classes:/opt/pi4j/lib/'*' -d . TriggerGpioExample.java

echo "[11 of 11] ... compiling : UsageGpioExample.java"
javac -classpath .:classes:/opt/pi4j/lib/'*' -d . UsageGpioExample.java

echo "---------------------------------------------------------"

# THE FOLLOWING INSTRUCTIONS WILL EXECUTE ONE OF THE EXAMPLES
# sudo java -classpath .:classes:/opt/pi4j/lib/'*' BlinkGpioExample
echo "The following command syntax can be used to execute the sample projects:"
echo "  sudo java -classpath .:classes:/opt/pi4j/lib/'*' BlinkGpioExample"
echo "---------------------------------------------------------"