package com.pi4j.component.display.impl;

import com.pi4j.component.display.utils.Command;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PCD8544Constants.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 * 
 * @author SrMouraSilva
 * Based in <a href="https://github.com/Wicker25/Rpi-hw/blob/db4d9c0dd9d765a3f92f63b6aa413316f01be15e/include/rpi-hw/display/pcd8544.hpp">Rpi-hd PCD8544 implementation</a>
 */

/**
 * Group the PCD8544 constants
 */
class PCD8544Constants {

    enum SysCommand implements Command {
        /** Display control. */
        DISPLAY(0x08), 
        /** Function set */
        FUNC   (0x20),
        /** Set Y address of RAM */
        YADDR  (0x40),
        /** Set Y address of RAM */
        XADDR  (0x80),

        /** Temperature control */
        TEMP   (0x04),
        /** Bias system */
        BIAS   (0x10),
        /** Set Vop */
        VOP    (0x80); 

        private byte command;

        private SysCommand(int command) {
            this.command = (byte) command;
        }

        @Override
        public byte cmd() {
            return command;
        }
    };
    
    enum Setting implements Command {
        /** Sets display configuration */
        DISPLAY_E    (0x01),
        /** Sets display configuration */
        DISPLAY_D    (0x04),

        /** Extended instruction set */
        FUNC_H        (0x01),
        /** Entry mode */
        FUNC_V        (0x02),
        /** Power down control */
        FUNC_PD        (0x04),

        /** Set bias system */
        BIAS_BS0    (0x01),
        /** Set bias system */
        BIAS_BS1    (0x02),
        /** Set bias system */
        BIAS_BS2    (0x04);


        private byte command;

        private Setting(int command) {
            this.command = (byte) command;
        }

        @Override
        public byte cmd() {
            return command;
        }
    }

    interface DisplaySize {
        public static final int WIDTH  = 84;
        public static final int HEIGHT = 48;
    }

    @Deprecated
    enum BitOrderFirst {
        LSB,
        MSB
    }
}
