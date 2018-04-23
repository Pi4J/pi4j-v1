#!/bin/bash -e
###
# #%L
# **********************************************************************
# ORGANIZATION  :  Pi4J
# PROJECT       :  Pi4J :: JNI Native Library
# FILENAME      :  build-nanopi.sh
#
# This file is part of the Pi4J project. More information about
# this project can be found here:  http://www.pi4j.com/
# **********************************************************************
# %%
# Copyright (C) 2012 - 2018 Pi4J
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

# set executable permissions on build scripts
chmod +x install-prerequisites.sh
chmod +x wiringpi-build.sh

# ------------------------------------------------------
# INSTALL BUILD PREREQUISITES
# ------------------------------------------------------
ARCHITECTURE=$(uname -m)
echo "PLATFORM ARCH: $ARCHITECTURE"
if [[ ( "$ARCHITECTURE" = "armv7l") || ("$ARCHITECTURE" = "armv6l") ]]; then
   echo
   echo "**********************************************************************"
   echo "*                                                                    *"
   echo "*                 INSTALLING Pi4J BUILD PREREQUISITES                *"
   echo "*                                                                    *"
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
echo "*                                                                    *"
echo "*           CHECKING JAVA_HOME ENVIRONMENT VARIABLE                  *"
echo "*                                                                    *"
echo "**********************************************************************"
echo
if [[ -n "$JAVA_HOME" ]]; then
   echo "'JAVA_HOME' already defined as: $JAVA_HOME";
else
   export JAVA_HOME=$(readlink -f /usr/bin/javac | sed "s:bin/javac::")
   echo "'JAVA_HOME' was not defined; attempting to use: $JAVA_HOME";
fi

# ------------------------------------------------------
# BANANA-PI
# ------------------------------------------------------
echo
echo "**********************************************************************"
echo "*                                                                    *"
echo "*              BUILDING Pi4J FOR THE 'NANOPI' PLATFORM               *"
echo "*                                                                    *"
echo "**********************************************************************"
echo
WIRINGPI_PLATFORM=nanopi

# build wiringPi
# export WIRINGPI_REPO=https://github.com/friendlyarm/WiringNP
export WIRINGPI_REPO=https://github.com/Pi4J/WiringNP
export WIRINGPI_BRANCH=master
export WIRINGPI_DIRECTORY=wiringPi
rm --recursive --force wiringPi
./wiringpi-build.sh $@

# compile the 'lib4j.so' JNI native shared library with statically linked dependencies
echo
echo "==========================================="
echo "Building Pi4J JNI library (statically linked)"
echo "==========================================="
echo
mkdir -p lib/$WIRINGPI_PLATFORM/static
make clean static TARGET=lib/$WIRINGPI_PLATFORM/static/libpi4j.so $@

# compile the 'lib4j.so' JNI native shared library with dynamically linked dependencies
echo
echo "=============================================="
echo "Building Pi4J JNI library (dynamically linked)"
echo "=============================================="
echo
mkdir -p lib/$WIRINGPI_PLATFORM/dynamic
make clean dynamic TARGET=lib/$WIRINGPI_PLATFORM/dynamic/libpi4j.so $@

echo
echo "**********************************************************************"
echo "*                                                                    *"
echo "*         Pi4J JNI BUILD COMPLETE FOR THE 'NANOPI' PLATFORM          *"
echo "*                                                                    *"
echo "**********************************************************************"
echo
tree lib
