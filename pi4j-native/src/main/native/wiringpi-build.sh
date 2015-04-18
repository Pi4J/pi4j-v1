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
git clone git://git.drogon.net/wiringPi
cd wiringPi

# ----------------------------------
# uninstall any previous copies
# ----------------------------------
cd wiringPi
sudo make uninstall

# ----------------------------------
# build latest wiringPi 
# ----------------------------------
echo "wiringPi Build script"
echo "====================="
echo
echo "Compiling WiringPi STATIC library"
make static
sudo make install
sudo make install-static

# ----------------------------------
# build latest wiringPi devLib
# ----------------------------------
cd ../devLib
sudo make uninstall

echo "wiringPi devLib Build script"
echo "============================"
echo
echo "Compiling WiringPi devLib STATIC library"
make static
sudo make install
sudo make install-static
