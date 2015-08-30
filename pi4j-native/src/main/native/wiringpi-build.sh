#!/bin/bash
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

# ----------------------------------
# clone wiringPi from github
# ----------------------------------
rm -rf wiringPi
git clone git://git.drogon.net/wiringPi
cd wiringPi

# ----------------------------------
# uninstall any previous copies
# ----------------------------------
cd wiringPi
#sudo -E make uninstall

# ----------------------------------
# build latest wiringPi 
# ----------------------------------
echo
echo "============================"
echo "wiringPi Build script"
echo "============================"
echo
echo "Compiling wiringPi STATIC library"
make clean
make static -j5 $@

# ----------------------------------
# build latest wiringPi devLib
# ----------------------------------
echo
echo "============================"
echo "wiringPi devLib Build script"
echo "============================"
echo
echo "Copying wiringPi header files and static lib"
cd ../devLib
cp ../wiringPi/*.h .
cp ../wiringPi/*.a .
echo "Compiling wiringPi devLib STATIC library"
make clean
make static -j5 $@
echo
