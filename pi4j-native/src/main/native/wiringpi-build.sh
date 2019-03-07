#!/bin/bash -e
###
# #%L
# **********************************************************************
# ORGANIZATION  :  Pi4J
# PROJECT       :  Pi4J :: JNI Native Library
# FILENAME      :  wiringpi-build.sh
#
# This file is part of the Pi4J project. More information about
# this project can be found here:  https://pi4j.com/
# **********************************************************************
# %%
# Copyright (C) 2012 - 2019 Pi4J
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

# -- RASPBERRY PI --
# set default wiringPi repository URL if not already defined
if [ -z $WIRINGPI_REPO ]; then
    WIRINGPI_REPO=https://github.com/Pi4J/wiringPi
fi
# set default wiringPi repository branch if not already defined
if [ -z $WIRINGPI_BRANCH ]; then
	WIRINGPI_BRANCH=master
fi
# set default wiringPi directory if not already defined
if [ -z $WIRINGPI_DIRECTORY ]; then
	WIRINGPI_DIRECTORY=wiringPi
fi

# set default wiringPi static build flag if not already defined
if [ -z $WIRINGPI_STATIC ]; then
	WIRINGPI_STATIC=0
fi

echo "============================"
echo "Building wiringPi Library   "
echo "============================"
echo "REPOSITORY : $WIRINGPI_REPO"
echo "BRANCH     : $WIRINGPI_BRANCH"
echo "DIRECTORY  : $WIRINGPI_DIRECTORY"

# ----------------------------------
# clone wiringPi from github
# ----------------------------------
rm -rf wiringPi
git clone $WIRINGPI_REPO -b $WIRINGPI_BRANCH wiringPi
cd wiringPi

# get wiringPi version
if [ -f VERSION ]; then
  WIRINGPI_VERSION=$(cat VERSION)
else
  WIRINGPI_VERSION=2.0
fi

# create target directory for STATICALLY linked compiled library
if [[ $WIRINGPI_STATIC > 0 ]]; then
  mkdir -p lib/static
fi

# create target directory for DYNAMICALLY linked compiled library
mkdir -p lib/dynamic

# ----------------------------------
# build latest wiringPi
# ----------------------------------
echo
echo "============================"
echo "wiringPi Build script"
echo "============================"
echo
echo "Compiling wiringPi library"

cd $WIRINGPI_DIRECTORY

if [[ $WIRINGPI_STATIC > 0 ]]; then
  # make WiringPi static and dynamic lib
  make clean all static -j1 $@

  # move compiled libs to respective paths
  mv libwiringPi.a ../lib/static/libwiringPi.a
  mv libwiringPi.so.$WIRINGPI_VERSION ../lib/dynamic/libwiringPi.so
else
  # make WiringPi dynamic lib
  make clean all -j1 $@

  # move compiled libs to respective paths
  mv libwiringPi.so.$WIRINGPI_VERSION ../lib/dynamic/libwiringPi.so
fi


# ----------------------------------
# build latest wiringPi devLib
# ----------------------------------

echo
echo "============================"
echo "wiringPi devLib Build script"
echo "============================"
echo

cd ../devLib
cp ../$WIRINGPI_DIRECTORY/*.h .

# compile wiringPi devlLib and link STATICALLY
if [[ $WIRINGPI_STATIC > 0 ]]; then
  echo "Compiling wiringPi devLib STATIC library"
  make clean static -j1 LIBS=lib/static $@
  mv libwiringPiDev.a ../lib/static/libwiringPiDev.a
fi

# compile wiringPi devlLib and link DYNAMICALLY
echo "Compiling wiringPi devLib DYNAMIC library"
make all -j1 LIBS=lib/dynamic $@
mv libwiringPiDev.so.$WIRINGPI_VERSION ../lib/dynamic/libwiringPiDev.so
echo
