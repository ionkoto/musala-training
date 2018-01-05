package com.musala.simple.smarthome;

/**
 * The ISmartDevice interface is the backbone for the implementation of 
 * particular devices in a Smart Home configuration. The user of this
 * interface has control over the action the different devices are able
 * to perform.
 * 
 * @author yoan.petrushinov
 *
 */
public interface ISmartDevice {
	
	/**
	 * Performs an action specific to the Class (Smart device) implementing it.
	 * 
	 * @return a String value describing the action that has been performed.
	 */
	public String performAction();
	
}
