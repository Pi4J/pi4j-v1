package com.pi4j.device.nodic;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  www.nowcode.cn
 * PROJECT       :  Pi4J :: Device/ NRF24L01
 * FILENAME      :  ReceiveListener
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
 * @author Alex maoanapex88@163.com
 */
public interface ReceiveListener {

	/**
	 * @param data data bytes arrived
	 */
	void dataRecived(int[] data);

}
