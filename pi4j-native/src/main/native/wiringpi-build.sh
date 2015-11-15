#!/bin/bash -e
###
# #%L
# **********************************************************************
# ORGANIZATION  :  Pi4J
# PROJECT       :  Pi4J :: JNI Native Library
# FILENAME      :  wiringpi-build.sh
#
# This file is part of the Pi4J project. More information about
# this project can be found here:  http://www.pi4j.com/
# **********************************************************************
# %%
# Copyright (C) 2012 - 2015 Pi4J
# %%
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Lesser General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Lesser Public License for more details.
#
# You should have received a copy of the GNU General Lesser Public
# License along with this program.  If not, see
# <http://www.gnu.org/licenses/lgpl-3.0.html>.
# #L%
###

# -- RASPBERRY PI --
# set default wiringPi repository URL if not already defined
if [ -z $WIRINGPI_REPO ]; then
    WIRINGPI_REPO=git://git.drogon.net/wiringPi
fi
# set default wiringPi repository branch if not already defined
if [ -z $WIRINGPI_BRANCH ]; then
	WIRINGPI_BRANCH=master
fi
# set default wiringPi directory if not already defined
if [ -z WIRINGPI_DIRECTORY ]; then
	WIRINGPI_DIRECTORY=wiringPi
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
# (NOTE: The Lemaker (BananaPi) and Hardkernel (Odroid) versions of WiringPi
#  are a bit outdated and do not have the VERSION file in the repo.)
if [ -f VERSION ]; then
  WIRINGPI_VERSION=$(cat VERSION)
else
  WIRINGPI_VERSION=2.0
fi

# create target directories to hold compiled libraries
mkdir -p lib/static
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
make clean all static -j1 $@
mv libwiringPi.a ../lib/static/libwiringPi.a
mv libwiringPi.so.$WIRINGPI_VERSION ../lib/dynamic/libwiringPi.so


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
echo "Compiling wiringPi devLib STATIC library"
make clean static -j1 LIBS=lib/static $@
mv libwiringPiDev.a ../lib/static/libwiringPiDev.a

echo "Compiling wiringPi devLib DYNAMIC library"
make all -j1 LIBS=lib/dynamic $@
mv libwiringPiDev.so.$WIRINGPI_VERSION ../lib/dynamic/libwiringPiDev.so
echo
