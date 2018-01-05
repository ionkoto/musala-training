package com.musala.simple.smarthome.devices;

import com.musala.simple.smarthome.ISmartDevice;

public class SmartMotionDetector implements ISmartDevice {

	@Override
	public String performAction() {
		return "Detect motion";
	}

}
