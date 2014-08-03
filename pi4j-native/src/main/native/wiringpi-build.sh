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
# Copyright (C) 2012 - 2014 Pi4J
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
