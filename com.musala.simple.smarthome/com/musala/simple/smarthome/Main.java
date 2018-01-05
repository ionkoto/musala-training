package com.musala.simple.smarthome;

import com.musala.simple.smarthome.devices.SmartBulb;
import com.musala.simple.smarthome.devices.SmartMotionDetector;
import com.musala.simple.smarthome.devices.SmartPlayer;
import com.musala.simple.smarthome.devices.SmartThermostat;

public class Main {
	public static void main(String[] args) {
		
		int input;
		
		if (args.length > 0) {
		    try {
		    	input = Integer.parseInt(args[0]);
		    	
		    	switch (input) {
					case 1:
						SmartPlayer sp = new SmartPlayer();
						System.out.println(sp.performAction());
						break;
					case 2:
						SmartBulb sb = new SmartBulb();
						System.out.println(sb.performAction());
						break;
					case 3:
						SmartThermostat st = new SmartThermostat();
						System.out.println(st.performAction());
						break;
					case 4:
						SmartMotionDetector smd = new SmartMotionDetector();
						System.out.println(smd.performAction());
						break;
					default:
						System.out.println("Requested device is not supported.");
						break;
				}
		    	
		    } catch (NumberFormatException e) {
		        System.err.println("Incorrect input");
		    }
		} else {
			System.out.println("Incorrect input");
		}
	}
}
