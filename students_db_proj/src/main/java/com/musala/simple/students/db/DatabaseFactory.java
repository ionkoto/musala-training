package com.musala.simple.students.db;

import java.util.Properties;

import com.musala.simple.students.db.DatabaseTypes;

public class DatabaseFactory {
	private static final String DB_NAME = "name";
	private static final String DB_PORT = "port";
	private static final String DB_HOST = "host";
	private static final String DB_USER = "username";
	private static final String DB_PASSWORD = "password";
	private DatabaseTypes dbType;
	private String name;
	private int port;
	private String host;
	private String username;
	private String password;

	public DatabaseFactory(Properties dbProperties, DatabaseTypes dbType) {
		this.setDbType(dbType);
		this.setName(dbProperties.getProperty(DB_NAME));
		this.setPort(Integer.parseInt(dbProperties.getProperty(DB_PORT)));
		this.setHost(dbProperties.getProperty(DB_HOST));
		this.setUsername(dbProperties.getProperty(DB_USER));
		this.setPassword(dbProperties.getProperty(DB_PASSWORD));
	}

	public Database getDatabase() {
		Database database = null;

		switch (this.getDbType()) {
		case MySQL:
			// TODO
			break;
		case MongoDb:
			database = new MyMongoDatabase(this.name, this.host, this.port);
			break;
		case PostgreSQL:
			// TODO
			break;
		default:
			// TODO implement exception
			break;
		}

		return database;
	}

	public DatabaseTypes getDbType() {
		return this.dbType;
	}

	public void setDbType(DatabaseTypes type) {
		this.dbType = type;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
