package com.musala.simple.students.db.database;

public interface DatabaseProperties {
	
	/**
	 * Sets the name of the Database implementation object
	 * @param  the name of the database
	 */
	public void setName(String name);
	
	/**
	 * Gets the name of the database
	 */
	public String getName();
	
	/**
	 * Sets the host of the Database implementation object
	 * @param  the host of the database
	 */
	public void setHost(String host);
	
	/**
	 * Gets the host of the database
	 */
	public String getHost();
	
	/**
	 * Sets the port of the Database implementation object
	 * @param  the port of the database
	 */
	public void setPort(int port);
	
	/**
	 * Gets the port of the database
	 */
	public int getPort();
	
	/**
	 * Sets the username of the Database implementation object
	 * @param  the username of the database
	 */
	public void setUsername(String username);
	
	/**
	 * Gets the username of the database
	 */
	public String getUsername();
	
	/**
	 * Sets the password of the Database implementation object
	 * @param  the password of the database
	 */
	public void setPassword(String password);
	
	/**
	 * Gets the password of the database
	 */
	public String getPassword();
}
