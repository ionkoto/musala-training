package com.musala.simple.smarthome.devices;

import com.musala.simple.smarthome.ISmartDevice;

/**
 * Music player implementation of the {@link ISmartDevice}. interface.
 * The device can perform a specific action by overriding the 
 * performAction() method.
 * 
 * @author yoan.petrushinov
 *
 */
public class SmartPlayer implements ISmartDevice {

	@Override
	public String performAction() {
		return "Play music";
	}

}
