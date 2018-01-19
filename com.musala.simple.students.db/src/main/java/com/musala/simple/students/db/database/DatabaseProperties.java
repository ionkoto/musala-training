package com.musala.simple.students.db.database;

public interface DatabaseProperties {
	public void setName(String name);
	
	public String getName();
	
	public void setHost(String host);
	
	public String getHost();
	
	public void setPort(int port);
	
	public int getPort();
	
	public void setUsername(String username);
	
	public String getUsername();
	
	public void setPassword(String password);
	
	public String getPassword();
}
