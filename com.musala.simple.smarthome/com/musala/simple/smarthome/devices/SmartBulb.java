package com.musala.simple.smarthome.devices;

import com.musala.simple.smarthome.ISmartDevice;

public class SmartBulb implements ISmartDevice {

	@Override
	public String performAction() {
		return "Light is on";
	}

}
