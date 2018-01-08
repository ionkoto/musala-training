package com.musala.simple.smarthome.devices;

import com.musala.simple.smarthome.ISmartDevice;

/**
 * Light bulb implementation of the @ISmartDevice interface.
 * The device can perform a specific action by overriding the 
 * performAction() method.
 * 
 * @author yoan.petrushinov
 *
 */
public class SmartBulb implements ISmartDevice {

	@Override
	public String performAction() {
		return "Light is on";
	}

}
