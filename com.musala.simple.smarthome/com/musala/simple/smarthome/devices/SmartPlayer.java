package com.musala.simple.smarthome.devices;

import com.musala.simple.smarthome.ISmartDevice;

public class SmartPlayer implements ISmartDevice {

	@Override
	public String performAction() {
		return "Play music";
	}

}
