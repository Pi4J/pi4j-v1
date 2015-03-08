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
 * Copyright (C) 2012 - 2015 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * The wiper - used for devices knowing more than one wiper.
 */
public enum DeviceControllerChannel {
	
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
	
}
