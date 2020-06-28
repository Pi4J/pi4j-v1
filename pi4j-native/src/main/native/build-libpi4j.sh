#!/bin/bash -e
###
# #%L
# **********************************************************************
# ORGANIZATION  :  Pi4J
# PROJECT       :  Pi4J :: JNI Native Library
# FILENAME      :  build-libpi4j.sh
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

# ------------------------------------------------------
# RASPBERRY-PI
# ------------------------------------------------------
echo
echo "**********************************************************************"
echo " STARTED BUILDING Pi4J NATIVE LIBRARIES:"
echo " - FOR ARCHITECTURE : ${ARCH}"
echo " - USING COMPILER   : ${CC}"
echo "**********************************************************************"
echo

# build the wiringPi library because we need to dynamically link against it
#export WIRINGPI_REPO=git://git.drogon.net/wiringPi
#export WIRINGPI_REPO=https://github.com/Pi4J/wiringPi
export WIRINGPI_REPO=https://github.com/WiringPi/WiringPi
export WIRINGPI_BRANCH=master
export WIRINGPI_DIRECTORY=wiringPi
export WIRINGPI_STATIC=0

echo "======================================================="
echo "BUILDING NATIVE LIBRARY: [libwiringPi-${ARCH}.so]"
echo "======================================================="
echo
rm --recursive --force wiringPi
./build-wiringpi.sh $@

# compile the 'libpi4j.so' JNI native shared library with dynamically linked dependencies
echo "======================================================="
echo "BUILDING NATIVE LIBRARY: [libpi4j-${ARCH}.so]"
echo "======================================================="
echo
mkdir -p lib/raspberrypi/dynamic
make clean dynamic TARGET=lib/raspberrypi/dynamic/libpi4j-${ARCH}.so CC=$CC ARCH=$ACRH $@

echo "**********************************************************************"
echo " FINISHED BUILDING Pi4J NATIVE LIBRARIES:"
echo " - FOR ARCHITECTURE : ${ARCH}"
echo " - USING COMPILER   : ${CC}"
echo "**********************************************************************"
echo
