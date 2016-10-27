package com.pi4j.device.nordic;

import static com.pi4j.wiringpi.Gpio.INPUT;
import static com.pi4j.wiringpi.Gpio.OUTPUT;
import static com.pi4j.wiringpi.Gpio.PUD_UP;
import static com.pi4j.wiringpi.Gpio.digitalRead;
import static com.pi4j.wiringpi.Gpio.digitalWrite;
import static com.pi4j.wiringpi.Gpio.pinMode;
import static com.pi4j.wiringpi.Gpio.pullUpDnControl;
import static com.pi4j.wiringpi.Gpio.wiringPiSetup;
import static com.pi4j.wiringpi.GpioUtil.DIRECTION_IN;
import static com.pi4j.wiringpi.GpioUtil.DIRECTION_OUT;
import static com.pi4j.wiringpi.GpioUtil.export;
import static com.pi4j.wiringpi.GpioUtil.unexport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  www.nowcode.cn
 * PROJECT       :  Pi4J :: Device/ NRF24L01
 * FILENAME      :  NRF24L01.java
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
/**
 * NRF24L01.java is the driver for nodic nRF24L01 seriser 2.4G RF chip. see http://www.nordicsemi.com/eng/Products/2.4GHz-RF/nRF24L01P
 * it is simplex communication, but it abstract the chip as duplex. It owns one data listener for receiving. 
 * this app mocked SPI bus for communication implementation.
 * 
 * <h1>Sample Usage</h1>
 * <pre>
 * NRF24L01 nrf=NRF24L01.getInstance();
 * nrf.start();//there will be thread daemon up, if your system does not have other user thread, setup one for dummy
 * nrf.setReceiverListener(.....);
 * 
 * <br>somewhere to send
 * nrf.send(.....);
 * 
 * <br>somewhere to shutdown
 * nrf.shutdown();
 * </pre>
 * 
 * @author Alex maoanapex88@163.com
 *
 */
public class NRF24L01 implements IRegister, Runnable {
	/**
	 * one led bubble, GPIO 21, if working, it will light up
	 */
	private static final int LED = 21;
	/**
	 * irq
	 */
	private static final int IRQ = 2;
	/**
	 * miso
	 */
	private static final int MISO = 12;
	/**
	 * ce
	 */
	private static final int CE = 11;
	/**
	 * mosi
	 */
	private static final int MOSI = 13;
	/**
	 * sclk
	 */
	private static final int SCLK = 14;
	/**
	 * csn
	 */
	private static final int CSN = 0;
	/**
	 * default data width 16, NRF support 32 in most
	 */
	private static final short RECEIVE_DATA_WIDTH = 16;
	
	/**
	 * default local listening chanel
	 */
	private int localRFChanel=96;
	private int[] localRFAddress={ 53, 69, 149, 231, 231 };
	private volatile boolean running=false;
	private Thread irqWatchThread;
	private ReceiveListener listener=new EmptyReceiveListener();
	/*send FIFO*/
	private BlockingQueue<DataPackage> fifo=new LinkedBlockingQueue<DataPackage>(16);
	/**
	 * one thread pool for processing data listener
	 */
	private ExecutorService executorService;
	static {
		wiringPiSetup();
	}
	
	private static final NRF24L01 nrf=new NRF24L01();
	
	public static final NRF24L01 getInstance() {
		return nrf;
	}
	
	private NRF24L01() {
		/*CSN*/
		export(CSN, DIRECTION_OUT);
		pinMode(CSN, OUTPUT);
		
		/*SCLK*/
		export(SCLK, DIRECTION_OUT);
		pinMode(SCLK, OUTPUT);
		
		/*mosi*/
		export(MOSI, DIRECTION_OUT);
		pinMode(MOSI, OUTPUT);
		
		/*ce*/
		export(CE, DIRECTION_OUT);
		pinMode(CE, OUTPUT);
		
		/*miso*/
		export(MISO, DIRECTION_IN);
		pinMode(MISO, OUTPUT);
		pullUpDnControl(MISO, PUD_UP);
		
		/*irq*/
		export(IRQ, DIRECTION_IN);
		pinMode(MISO, INPUT);
		pullUpDnControl(IRQ, PUD_UP);
		
		/*LED light*/
		export(LED, DIRECTION_OUT);
		pinMode(LED, OUTPUT);
		pullUpDnControl(LED, PUD_UP);
		
		init();
		setRxMode(localRFChanel, 5, localRFAddress);
	}
	private final void setRxMode(int rfChannel, int addrWidth, int[] rxAddr) {
		digitalWrite(CE, 0);
		writeRegister((W_REGISTER+SETUP_AW), (addrWidth - 2)); // set address width
		writeBuffer((W_REGISTER+RX_ADDR_P0), rxAddr, addrWidth); // use channel 0, same address for receive
		writeRegister((W_REGISTER+RF_CH), rfChannel); // RF setup
		writeRegister((W_REGISTER+RX_PW_P0), RECEIVE_DATA_WIDTH); // channel 0 same data width
		writeRegister((W_REGISTER+RF_SETUP), 0x27); // inital 250Kbps, power 0dBm
		// (+22dBm with PA), LNA?
		writeRegister((W_REGISTER+STATUS), 0x7f); // clear RX_DR,TX_DS,MAX_RT flags
		writeRegister((W_REGISTER+CONFIG), 0x3f); // enable RX_DR irq, block TX_DS+MAX_RT, enable CRC powerup, in receive mode

		digitalWrite(CE, 1);// in PRX
	}

	/**
	 * 
	 */
	private final void init() {
		digitalWrite(CE, 0);
		digitalWrite(CSN, 1);
		digitalWrite(SCLK, 0);
		
		writeRegister((W_REGISTER+EN_AA), 0x01);
		writeRegister((W_REGISTER+EN_RXADDR), 0x01); // enable channel 0
		writeRegister((W_REGISTER+SETUP_RETR), 0x1f ); // set auto retry delay 500us, retry 15 times
		writeRegister((W_REGISTER+STATUS), 0x7e); // clear RX_DR,TX_DS,MAX_RT flags
		writeRegister((W_REGISTER+CONFIG), 0x7e); // enable RX_DR irq, block TX_DS+MAX_RT, enable CRC powerup, in receive mode PTX
		
		flushTx();
		flushRx();
		
		/*light one led bubble*/
		digitalWrite(LED, 1);
		/**
		 * https://github.com/Pi4J/pi4j/blob/master/pi4j-native/src/main/native/com_pi4j_wiringpi_GpioInterrupt.c
		 * you can see it is not good performance, it is c pthread watching
		 * so I watch irq by myself, in java code
		 */
		//GpioInterrupt.enablePinStateChangeCallback(IRQ);
	}
	private final int spiReadWrite(int spiData) {
		for (int i = 0; i < 8; i++) {
			if ((0x80 & spiData) == 0x80) digitalWrite(MOSI, 1);
			else digitalWrite(MOSI, 0);
			
			spiData <<= 1;
			digitalWrite(SCLK, 1);
			
			if (digitalRead(MISO)==1) spiData |= 0x01; 
			digitalWrite(SCLK, 0);
		}
		return spiData & 0xff;
	}
	
	
	private final int writeRegister(int regAddr, int writeData) {
		digitalWrite(CSN, 0);
		int val = spiReadWrite(regAddr);
		spiReadWrite(writeData);
		digitalWrite(CSN, 1);
		return val;
	}
	private final int readRegister(int regAddr) {
		digitalWrite(CSN, 0);
		spiReadWrite(regAddr);
		int val = spiReadWrite(0x00);
		digitalWrite(CSN, 1);
		return val;
	}
	
	private final int writeBuffer(int regAddr, int[] txData, int dataLen) {
		digitalWrite(CSN, 0);
		int val = spiReadWrite(regAddr);
		for (int i = 0; i < dataLen; i++) {
			spiReadWrite(txData[i]);
		}
		digitalWrite(CSN, 1);
		return val;

	}
	private final int readBuffer(int regAddr, int[] rxData, int dataLen) {
		digitalWrite(CSN, 0);
		int val = spiReadWrite(regAddr);
		for (int i = 0; i < dataLen; i++) {
			rxData[i] = spiReadWrite(0);
		}
		digitalWrite(CSN, 1);
		return val;
	}
	
	/**
	 * 
	 */
	private final void flushRx() {
		digitalWrite(CSN, 0);
		spiReadWrite(FLUSH_RX);
		digitalWrite(CSN, 1);
	}

	/**
	 * 
	 */
	private final void flushTx() {
		digitalWrite(CSN, 0);
		spiReadWrite(FLUSH_TX);
		digitalWrite(CSN, 1);
	}
	/**
	 * @return
	 */
	private final boolean isDataAvaid() {
		int status;
		if (digitalRead(IRQ)==0) {
			status = readRegister(R_REGISTER+STATUS);
			// System.out.println("receive status:"+status);
			if ((status & 0x40) == 0x40)
			{
				// read FIFO
				status = readRegister(R_REGISTER+FIFO_STATUS);
				if ((status & 0x01) == 0x01) {
					writeRegister(W_REGISTER+STATUS, 0x40);
					// clear FIFO 
				} else {
					// RX FIFO is not null, data in FIFO
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * @return
	 */
	private final int[] nrfGetOneDataPacket() {
		int[] dataBuffer = new int[RECEIVE_DATA_WIDTH];
		readBuffer(R_RX_PAYLOAD, dataBuffer, RECEIVE_DATA_WIDTH);
		return dataBuffer;
	}
	/**
	 * 
	 */
	public final void start() {
		if(running) {
			System.err.println("It is already started, call start do nothing");
			return ;
		}
		running=true;
		
		irqWatchThread=new Thread(this, "NRF24L01+ Daemon");
		irqWatchThread.setPriority(Thread.MAX_PRIORITY);//in high priority
		irqWatchThread.setDaemon(true);//daemon
		irqWatchThread.start();
		executorService=Executors.newCachedThreadPool();
	}
	
	public final void shutdown() {
		running=false;
		executorService.shutdown();
		/*LED off*/
		digitalWrite(LED, 0);
		/*all down*/
		digitalWrite(CSN, 0);
		digitalWrite(SCLK, 0);
		digitalWrite(MOSI, 0);
		digitalWrite(CE, 0);
		digitalWrite(MISO, 0);
		digitalWrite(IRQ, 0);
		digitalWrite(LED, 0);
		
		/*unexport pins*/
		/*CSN*/
		unexport(CSN);
		/*SCLK*/
		unexport(SCLK);
		/*mosi*/
		unexport(MOSI);
		/*ce*/
		unexport(CE);
		/*miso*/
		unexport(MISO);
		/*irq*/
		unexport(IRQ);
		/*LED light*/
		unexport(LED);
	}
	public final void run() {
		while(running){
			/*receive*/
			while (isDataAvaid()) {
				final int[] data = nrfGetOneDataPacket();
				executorService.execute(new Runnable() {
					public void run() {
						listener.dataRecived(data);
					}
				});
			}
			
			/*read FIFO for data send*/
			try {
				DataPackage pkg = fifo.poll(25, TimeUnit.MILLISECONDS);
				if(pkg==null) continue;
				int retry = nrfSendData(pkg.chanel, pkg.power, pkg.maxRetry, pkg.addrWidth, pkg.txAddr, pkg.dataWidth, pkg.txData);
				pkg.sentTimestamp=System.currentTimeMillis();
				pkg.retry = retry;
				setRxMode(this.localRFChanel, 5, this.localRFAddress);//切换回接收模式
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * send data out
	 * @param rfChannel
	 * @param rfPower can be 1,2,3,4 enum  250Kbps+-18dBm, 250Kbps+-12dBm, 250Kbps+-6dBm, 250Kbps+0dBm
	 * @param maxRetry how many software retry, it is NOT chipset retry!
	 * @param addrWidth it is 5
	 * @param txAddr 5 bytes of RF data address
	 * @param dataWidth your data length
	 * @param txData your data payload
	 * @return result of send, >=253 for failed
	 */
	private final int nrfSendData(int rfChannel, int rfPower, int maxRetry, int addrWidth, int[] txAddr, int dataWidth, int[] txData) {
		int ret = 0;
		int retryCnt = 0;
		// check params
		if (rfChannel > 125 || rfPower == 0 || rfPower > 4 || maxRetry > 9 || addrWidth < 3 || addrWidth > 5 || dataWidth == 0 || dataWidth > 32) {
			return 253;
		}
		digitalWrite(CE, 0);
		writeRegister((W_REGISTER+SETUP_AW), (addrWidth - 2)); 
		writeBuffer((W_REGISTER+TX_ADDR), txAddr, addrWidth); 
		writeBuffer((W_REGISTER+RX_ADDR_P0), txAddr, addrWidth); 
		if (rfPower == 1){
			writeRegister((W_REGISTER+RF_SETUP), 0x21); // 250Kbps -18dBm
		}
		// (+7dBm with PA), LNA?
		else if (rfPower == 2) {
			writeRegister((W_REGISTER+RF_SETUP), 0x23); // 250Kbps -12dBm
		}
		// (+15dBm with PA), LNA?
		else if (rfPower == 3) {
			writeRegister((W_REGISTER+RF_SETUP), 0x25); // 250Kbps -6dBm
		}
		// (+20dBm with PA), LNA?
		else if (rfPower == 4) {
			writeRegister((W_REGISTER+RF_SETUP), 0x27); // 250Kbps 0dBm
		}
		// (+22dBm with PA),
		// LNA?
		writeRegister((W_REGISTER+RF_CH), rfChannel); 
		writeRegister((W_REGISTER+STATUS), 0x7f); // clear RX_DR,TX_DS,MAX_RT flags
		writeRegister((W_REGISTER+CONFIG), 0x4e); // block RX_DR irq
		// irq TX_DS,MAX_RT，CRC enable PTX power on
		for (retryCnt = 0; retryCnt <= maxRetry; retryCnt++) {
			writeBuffer(W_TX_PAYLOAD, txData, dataWidth);//write
			digitalWrite(CE, 1);
			while (digitalRead(IRQ)==1) {
				
			}
			digitalWrite(CE, 0);// must wait IRQ then set CE!
			ret = checkSendStatus();
			if (ret <= 15) {
				ret += (16 * retryCnt);
				break;
			}
		}
		
		return ret;
	}
	private final int checkSendStatus() {
		int status = readRegister(R_REGISTER+STATUS);
		//LOGGER.debug("status={0}", status);
		if ((status & 0x20) == 0x20) {
			writeRegister((W_REGISTER + STATUS), 0x7f);
			return (readRegister(R_REGISTER + OBSERVE_TX) & 0x0f);
		} 
		else if ((status & 0x10) == 0x10) {
			writeRegister((W_REGISTER + STATUS), 0x7f);
			flushTx();
			return 255;
		} 
		else {
			return 252;
		}
	}
	/**
	 * set other chanel and address for this chip
	 * @param chanel
	 * @param addr
	 */
	public final void setLocalAddress(int chanel, int[] addr) {
		this.localRFChanel=chanel;
		this.localRFAddress=addr;
		setRxMode(localRFChanel, localRFAddress.length, localRFAddress);
	}
	/**
	 * set your business data listener
	 * @param l
	 */
	public final void setReceiveListener(ReceiveListener l) {
		this.listener = l;
	}
	public final void removeReceiveListener(){
		this.listener = new EmptyReceiveListener();
	}
	/**
	 * one default data listener, it is empty, just print out bytes in console
	 *
	 */
	private final class EmptyReceiveListener implements ReceiveListener {
		private final DateFormat df=new SimpleDateFormat("hh:mm:ss - ");
		public void dataRecived(int[] data) {
			System.out.print(df.format(new Date()));
			for(int i=0;i<data.length;i++) {
				System.out.print(data[i]&0x00ff);
				System.out.print(", ");
			}
			System.out.println();
		}
	}
	/**
	 * the data package structure
	 *
	 */
	public final class DataPackage {
		public int retry;//result of times retry
		public final int chanel;//send to chanel
		public final int power; // power
		public final int maxRetry;//maxtry
		public final int addrWidth;
		public final int[] txAddr;
		public final int dataWidth;
		public final int[] txData;//payload
		public final long createTimestamp;//time to create data package
		public long sentTimestamp;//time send out
		
		public DataPackage(int chanel, int power, int maxRetry, int addrWidth, int[] txAddr, int dataWidth, int[] txData) {
			super();
			this.chanel = chanel;
			this.power = power;
			this.maxRetry = maxRetry;
			this.addrWidth = addrWidth;
			this.txAddr = txAddr;
			this.dataWidth = dataWidth;
			this.txData = txData;
			createTimestamp=System.currentTimeMillis();
		}
		/**
		 * the letency
		 * @return
		 */
		public final int getLetency(){
			return (int)(sentTimestamp - createTimestamp);
		}
		/*retry times of this package*/
		public final int getRetry(){
			return retry;
		}
	}
}
