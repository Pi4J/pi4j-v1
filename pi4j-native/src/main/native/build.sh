#!/bin/bash -e
###
# #%L
# **********************************************************************
# ORGANIZATION  :  Pi4J
# PROJECT       :  Pi4J :: JNI Native Library
# FILENAME      :  build.sh
#
# This file is part of the Pi4J project. More information about
# this project can be found here:  https://pi4j.com/
# **********************************************************************
# %%
# Copyright (C) 2012 - 2020 Pi4J
# %%
#
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
#
# #L%
###

echo
echo "**********************************************************************"
echo "*                                                                    *"
echo "*                    Pi4J NATIVE BUILD <STARTED>                     *"
echo "*                                                                    *"
echo "**********************************************************************"
echo

# set executable permissions on build scripts
chmod +x install-prerequisites.sh
chmod +x build-wiringpi.sh
chmod +x build-libpi4j.sh

# ------------------------------------------------------
# INSTALL BUILD PREREQUISITES
# ------------------------------------------------------
ARCHITECTURE=$(uname -m)
if [[ ( "$ARCHITECTURE" = "armv7l") || ("$ARCHITECTURE" = "armv6l") ]]; then
   echo
   echo "**********************************************************************"
   echo "*                 INSTALLING Pi4J BUILD PREREQUISITES                *"
   echo "**********************************************************************"
   echo
   # download and install development prerequisites
   ./install-prerequisites.sh
fi

# ------------------------------------------------------
# JAVA_HOME ENVIRONMENT VARIABLE
# ------------------------------------------------------
echo
echo "**********************************************************************"
echo "*           CHECKING JAVA_HOME ENVIRONMENT VARIABLE                  *"
echo "**********************************************************************"
echo
if [[ -n "$JAVA_HOME" ]]; then
   echo "'JAVA_HOME' already defined as: $JAVA_HOME";
else
   export JAVA_HOME=$(readlink -f /usr/bin/javac | sed "s:bin/javac::")
   echo "'JAVA_HOME' was not defined; attempting to use: $JAVA_HOME";
fi

# ------------------------------------------------------
# BUILD NATIVE LIBRARIES FOR ARMv6 32-BIT (ARM)
# USING THE LOCALLY INSTALLED ARM CROSS-COMPILER
# ------------------------------------------------------
export CC=arm-linux-gnueabi-gcc
export ARCH=arm
./build-libpi4j.sh

# ------------------------------------------------------
# BUILD NATIVE LIBRARIES FOR ARMv6,ARMv7,ARMv8 32-BIT (ARMHF)
# USING THE LOCALLY INSTALLED ARM CROSS-COMPILER
# ------------------------------------------------------
export CC=arm-linux-gnueabihf-gcc
export ARCH=armhf
./build-libpi4j.sh

# ------------------------------------------------------
# BUILD NATIVE LIBRARIES FOR ARMv8 64-BIT (ARM64)
# USING THE LOCALLY INSTALLED ARM64 CROSS-COMPILER
# ------------------------------------------------------
export CC=aarch64-linux-gnu-gcc
export ARCH=aarch64
./build-libpi4j.sh

echo
echo "**********************************************************************"
echo "*                    Pi4J NATIVE LIBRARY ARTIFACTS                   *"
echo "**********************************************************************"
tree lib

echo
echo "**********************************************************************"
echo "*                                                                    *"
echo "*                    Pi4J NATIVE BUILD <FINISHED>                    *"
echo "*                                                                    *"
echo "**********************************************************************"
echo
