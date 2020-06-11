package com.pi4j.component.potentiometer.microchip.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  DeviceControllerTerminalConfiguration.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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
 * The device's terminal-configuration for a certain channel (see 4.2.2.2)
 */
class DeviceControllerTerminalConfiguration {

	private DeviceControllerChannel channel;
	private boolean channelEnabled;
	private boolean pinAEnabled;
	private boolean pinWEnabled;
	private boolean pinBEnabled;

	public DeviceControllerTerminalConfiguration(
            final DeviceControllerChannel channel,
            final boolean channelEnabled,
            final boolean pinAEnabled,
            final boolean pinWEnabled,
            final boolean pinBEnabled) {
		this.channel = channel;
		this.channelEnabled = channelEnabled;
		this.pinAEnabled = pinAEnabled;
		this.pinWEnabled = pinWEnabled;
		this.pinBEnabled = pinBEnabled;
	}

	public DeviceControllerChannel getChannel() {
		return channel;
	}

	/**
	 * @return Whether the entire channel is enabled or disabled
	 */
	public boolean isChannelEnabled() {
		return channelEnabled;
	}

	/**
	 * @return If channel is enabled, whether pin A is enabled
	 */
	public boolean isPinAEnabled() {
		return pinAEnabled;
	}

	/**
	 * @return If channel is enabled, whether pin W is enabled
	 */
	public boolean isPinWEnabled() {
		return pinWEnabled;
	}

	/**
	 * @return If channel is enabled, whether pin B is enabled
	 */
	public boolean isPinBEnabled() {
		return pinBEnabled;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!getClass().equals(obj.getClass())) {
			return false;
		}
		DeviceControllerTerminalConfiguration other = (DeviceControllerTerminalConfiguration) obj;
		if (channel != other.channel) {
			return false;
		}
		if (channelEnabled != other.channelEnabled) {
			return false;
		}
		if (pinAEnabled != other.pinAEnabled) {
			return false;
		}
		if (pinWEnabled != other.pinWEnabled) {
			return false;
		}
		if (pinBEnabled != other.pinBEnabled) {
			return false;
		}
		return true;
	}

}
