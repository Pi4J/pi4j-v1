###
# #%L
# **********************************************************************
# ORGANIZATION  :  Pi4J
# PROJECT       :  Pi4J :: JNI Native Library
# FILENAME      :  build-local.sh
#
# This file is part of the Pi4J project. More information about
# this project can be found here:  http://www.pi4j.com/
# **********************************************************************
# %%
# Copyright (C) 2012 - 2015 Pi4J
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

echo "-------------------------------------------"
echo "BUILDING Pi4J NATIVE LIBRARY"
echo "-------------------------------------------"

# ----------------------------------
# COPY SOURCES TO TARGET FOLDER
# ----------------------------------
cp -R src/main/native target
cd target/native

echo "-------------------------------------------"
echo " -- INSTALLING PREREQUISITES"
echo "-------------------------------------------"
sudo chmod +x install-prerequisites.sh
./install-prerequisites.sh

echo "-------------------------------------------"
echo " -- BUILDING LATEST WIRINGPI"
echo "-------------------------------------------"
sudo chmod +x wiringpi-build.sh
./wiringpi-build.sh


echo "-------------------------------------------"
echo " -- COMPILING LIBPI4J.SO JNI NATIVE LIBRARY"
echo "-------------------------------------------"
make clean
make all

echo "-------------------------------------------"
echo " -- COPYING FINAL LIBPI4J.SO TO TARGET"
echo "-------------------------------------------"
cp libpi4j.so ../libpi4j.so

echo "-------------------------------------------"
echo " -- DONE BUILDING Pi4J NATIVE LIBRARY"
echo "-------------------------------------------"
