#!/bin/bash -e
###
# #%L
# **********************************************************************
# ORGANIZATION  :  Pi4J
# PROJECT       :  Pi4J :: JNI Native Library
# FILENAME      :  build-docker.sh
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

echo
echo "**********************************************************************"
echo "**********************************************************************"
echo "*                                                                    *"
echo "*  COMPILE Pi4J NATIVE LIBRARIES USING DOCKER CROSS-COMPILER IMAGE   *"
echo "*                                                                    *"
echo "**********************************************************************"
echo "**********************************************************************"
echo

# set executable permissions on build scripts
chmod +x install-prerequisites.sh
chmod +x build-wiringpi.sh
chmod +x build-libpi4j.sh
chmod +x build.sh

# -------------------------------------------------------------
# BUILD NATIVE LIBRARIES USING THE DOCKER CROSS-COMPILER IMAGE
#   FOR ARMv6,ARMv7, ARMv8  32-BIT (ARMHF)
#   FOR ARMv8               64-BIT (ARM64)
# -------------------------------------------------------------
docker run --user "$(id -u):$(id -g)" -v $PWD:/build pi4j/raspberrypi-cross-compiler:latest $@
