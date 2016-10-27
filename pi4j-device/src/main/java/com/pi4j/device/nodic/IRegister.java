package com.pi4j.device.nodic;
/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  www.nowcode.cn
 * PROJECT       :  Pi4J :: Device/ NRF24L01
 * FILENAME      :  ADS1015DistanceSensorExample.java
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
public interface IRegister {
	/*nRF24L01 register24 in total*/
	public static final short CONFIG = 0x00;// config register
	public static final short EN_AA = 0x01;// enable auto ack
	public static final short EN_RXADDR = 0x02;// receive channel enable 0-5
	public static final short SETUP_AW = 0x03;// setup address width 3-5
	public static final short SETUP_RETR = 0x04;// setup auto retry
	public static final short RF_CH = 0x05;// RF channel setup
	public static final short RF_SETUP = 0x06;// RF register register
	public static final short STATUS = 0x07;// status register
	public static final short OBSERVE_TX = 0x08;// observe TX resiter
	public static final short CD = 0x09;// Carrier
	public static final short RX_ADDR_P0 = 0x0a;// data channel 0 address
	public static final short RX_ADDR_P1 = 0x0b;// data channel 1 address
	public static final short RX_ADDR_P2 = 0x0c;// data channel 2 address
	public static final short RX_ADDR_P3 = 0x0d;// data channel 3 address
	public static final short RX_ADDR_P4 = 0x0e;// data channel 4 address
	public static final short RX_ADDR_P5 = 0x0f;// data channel 5 address
	public static final short TX_ADDR = 0x10;// TX address
	public static final short RX_PW_P0 = 0x11;// P0 chanel data width setup
	public static final short RX_PW_P1 = 0x12;// P1 chanel data width setup
	public static final short RX_PW_P2 = 0x13;// P2 chanel data width setup
	public static final short RX_PW_P3 = 0x14;// P3 chanel data width setup
	public static final short RX_PW_P4 = 0x15;// P4 chanel data width setup
	public static final short RX_PW_P5 = 0x16;// P5 chanel data width setup
	public static final short FIFO_STATUS = 0x17;// FIFO
	public static final short FEATURE = 0x1D;// Additional features register,
												// needed to enable the
												// additional
												// commands
	/*nRF24L01register operation commands*/
	public static final short R_REGISTER = 0x00;// read register
	public static final short W_REGISTER = 0x20;// write
	public static final short R_RX_PAYLOAD = 0x61;// read rx payload
	public static final short W_TX_PAYLOAD = 0xa0;// write rx payload
	public static final short FLUSH_TX = 0xe1;// clear TXFIFO
	public static final short FLUSH_RX = 0xe2;// clear RXFIFO
	public static final short REUSE_TX_PL = 0xe3;// reuse last data package load
	public static final short NOP = 0xff;// NULL operation
	
}