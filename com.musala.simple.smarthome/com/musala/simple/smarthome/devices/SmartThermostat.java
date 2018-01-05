package com.musala.simple.smarthome.devices;

import com.musala.simple.smarthome.ISmartDevice;

public class SmartThermostat implements ISmartDevice {

	@Override
	public String performAction() {
		return "Measure temperature";
	}

}
