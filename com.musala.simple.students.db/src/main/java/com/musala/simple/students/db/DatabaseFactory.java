package com.musala.simple.students.db;

import java.util.Properties;

import com.musala.simple.students.db.DatabaseTypes;

/**
 * This is a Factory class for creating {@link Database} objects. The
 * constructor requires the database properties to be provided in the form of a
 * {@link Properties} object passed to the constructor along with the database
 * type {@link DatabaseTypes} enum.
 * 
 * 
 * @author yoan.petrushinov
 *
 */
public class DatabaseFactory {
	private static final String DEFAULT_DB_NAME = "studentsDb";
	private static final int DEFAULT_DB_PORT = 27017;
	private static final String DEFAULT_DB_HOST = "localhost";
	private static final String DEFAULT_DB_USERNAME = "admin";
	private static final String DEFAULT_DB_PASSWORD = "admin";

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
		this.setPort(dbProperties.getProperty(DB_PORT));
		this.setHost(dbProperties.getProperty(DB_HOST));
		this.setUsername(dbProperties.getProperty(DB_USER));
		this.setPassword(dbProperties.getProperty(DB_PASSWORD));
	}

	/**
	 * Creates a new implementation of the {@link Database}.
	 * 
	 * @return a new database of the required type
	 */
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
			database = new MyMongoDatabase(this.name, this.host, this.port);
			break;
		}

		return database;
	}

	/**
	 * @return the type of database being created by the Factory (as
	 *         {@link DatabaseTypes} enum)
	 */
	public DatabaseTypes getDbType() {
		return this.dbType;
	}

	/**
	 * 
	 * @param type
	 *            DBtype to set (e.g. MongoDb)
	 */
	public void setDbType(DatabaseTypes type) {
		this.dbType = type;
	}

	/**
	 * @return the name of the database being created by the Factory
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 
	 * @param name
	 *            for the database being created
	 */
	public void setName(String name) {
		if (name == null) {
			this.name = DEFAULT_DB_NAME;
		} else {
			this.name = name;
		}
	}

	/**
	 * @return the host of the database being created by the Factory
	 */
	public String getHost() {
		return this.host;
	}

	/**
	 * 
	 * @param host
	 *            for the database being created
	 */
	public void setHost(String host) {
		if (host == null) {
			this.host = DEFAULT_DB_HOST;
		} else {
			this.host = host;
		}
	}

	/**
	 * @return the port of the database being created by the Factory
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * 
	 * @param port
	 *            for the database being created
	 */
	public void setPort(String port) {
		if (port == null) {
			this.port = DEFAULT_DB_PORT;
		} else {
			this.port = Integer.parseInt(port);
		}
	}

	/**
	 * @return the username for authentication with the database being created by
	 *         the Factory
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * 
	 * @param username
	 *            for authentication with the database being created
	 */
	public void setUsername(String username) {
		if (username == null) {
			this.username = DEFAULT_DB_USERNAME;
		} else {
			this.username = username;
		}
	}

	/**
	 * @return the password for authentication with the database being created by
	 *         the Factory
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * 
	 * @param password
	 *            for authentication with the database being created
	 */
	public void setPassword(String password) {
		if (password == null) {
			this.password = DEFAULT_DB_PASSWORD;
		} else {
			this.password = password;
		}
	}
}
