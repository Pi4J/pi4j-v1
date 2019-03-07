#!/bin/bash
###
# #%L
# **********************************************************************
# ORGANIZATION  :  Pi4J
# PROJECT       :  Pi4J :: JNI Native Library
# FILENAME      :  install-prerequisites.sh
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
# ----------------------------------
# install prerequisites
# ----------------------------------
if [ ! -z "`type apt-get 2>/dev/null;`" ]; then

  # GCC
  GCC_INSTALLED=$(dpkg-query -W --showformat='${Status}\n' gcc|grep "install ok installed")
  if [[ "" == "$GCC_INSTALLED" ]]; then
    sudo apt-get --force-yes --yes install gcc
  else
    echo " [PREREQUISITE] 'gcc' already installed.";
  fi

  # GIT
  GIT_INSTALLED=$(dpkg-query -W --showformat='${Status}\n' git-core|grep "install ok installed")
  if [[ "" == "$GIT_INSTALLED" ]]; then
    sudo apt-get --force-yes --yes install git-core
  else
    echo " [PREREQUISITE] 'git-core' already installed.";
  fi

  # TREE
  TREE_INSTALLED=$(dpkg-query -W --showformat='${Status}\n' tree|grep "install ok installed")
  if [[ "" == "$TREE_INSTALLED" ]]; then
    sudo apt-get --force-yes --yes install tree
  else
    echo " [PREREQUISITE] 'tree' already installed.";
  fi
fi
