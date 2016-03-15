package com.pi4j.component.potentiometer.microchip.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  DeviceControllerChannel.java
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
 */

import com.pi4j.component.potentiometer.microchip.MicrochipPotentiometerChannel;

/**
 * The wiper - used for devices knowing more than one wiper.
 */
enum DeviceControllerChannel {

	A(MicrochipPotentiometerDeviceController.MEMADDR_WIPER0, MicrochipPotentiometerDeviceController.MEMADDR_WIPER0_NV,
			MicrochipPotentiometerDeviceController.MEMADDR_TCON01, MicrochipPotentiometerDeviceController.TCON_RH02HW,
			MicrochipPotentiometerDeviceController.TCON_RH02A, MicrochipPotentiometerDeviceController.TCON_RH02B, MicrochipPotentiometerDeviceController.TCON_RH02W),
	B(MicrochipPotentiometerDeviceController.MEMADDR_WIPER1, MicrochipPotentiometerDeviceController.MEMADDR_WIPER1_NV,
			MicrochipPotentiometerDeviceController.MEMADDR_TCON01, MicrochipPotentiometerDeviceController.TCON_RH13HW,
			MicrochipPotentiometerDeviceController.TCON_RH13A, MicrochipPotentiometerDeviceController.TCON_RH13B, MicrochipPotentiometerDeviceController.TCON_RH13W),
	C(MicrochipPotentiometerDeviceController.MEMADDR_WIPER2, MicrochipPotentiometerDeviceController.MEMADDR_WIPER2_NV,
			MicrochipPotentiometerDeviceController.MEMADDR_TCON23, MicrochipPotentiometerDeviceController.TCON_RH02HW,
			MicrochipPotentiometerDeviceController.TCON_RH02A, MicrochipPotentiometerDeviceController.TCON_RH02B, MicrochipPotentiometerDeviceController.TCON_RH02W),
	D(MicrochipPotentiometerDeviceController.MEMADDR_WIPER3, MicrochipPotentiometerDeviceController.MEMADDR_WIPER3_NV,
			MicrochipPotentiometerDeviceController.MEMADDR_TCON23, MicrochipPotentiometerDeviceController.TCON_RH13HW,
			MicrochipPotentiometerDeviceController.TCON_RH13A, MicrochipPotentiometerDeviceController.TCON_RH13B, MicrochipPotentiometerDeviceController.TCON_RH13W);

	private byte volatileMemoryAddress;
	private byte nonVolatileMemoryAddress;
	private byte terminalControllAddress;
	private int hardwareConfigControlBit;
	private int terminalAConnectControlBit;
	private int terminalBConnectControlBit;
	private int wiperConnectControlBit;

	private DeviceControllerChannel(byte volatileMemoryAddress,
                                    byte nonVolatileMemoryAddress, byte terminalControllAddress,
                                    int hardwareConfigControlBit, int terminalAConnectControlBit,
                                    int terminalBConnectControlBit, int wiperConnectControlBit) {
		this.volatileMemoryAddress = volatileMemoryAddress;
		this.nonVolatileMemoryAddress = nonVolatileMemoryAddress;
		this.terminalControllAddress = terminalControllAddress;
		this.hardwareConfigControlBit = hardwareConfigControlBit;
		this.terminalAConnectControlBit = terminalAConnectControlBit;
		this.terminalBConnectControlBit = terminalBConnectControlBit;
		this.wiperConnectControlBit = wiperConnectControlBit;
	}

	byte getVolatileMemoryAddress() {
		return volatileMemoryAddress;
	}

	byte getNonVolatileMemoryAddress() {
		return nonVolatileMemoryAddress;
	}

	byte getTerminalControllAddress() {
		return terminalControllAddress;
	}

	int getHardwareConfigControlBit() {
		return hardwareConfigControlBit;
	}

	int getTerminalAConnectControlBit() {
		return terminalAConnectControlBit;
	}

	int getTerminalBConnectControlBit() {
		return terminalBConnectControlBit;
	}

	int getWiperConnectControlBit() {
		return wiperConnectControlBit;
	}


    static DeviceControllerChannel valueOf(final MicrochipPotentiometerChannel channel) {

        if (channel == null) {
            return null;
        }

        for (final DeviceControllerChannel dcChannel : values()) {

            if (dcChannel.name().equals(channel.name())) {
                return dcChannel;
            }

        }

        throw new RuntimeException(
                "There is no pendant for the given "
                        + channel
                        + "'! Maybe there was another channel introduced for new "
                        + "devices but only to MicrochipPotentiometerChannel but "
                        + "in DeviceControllerChannel it is still missing.");

    }
}
