package com.musala.simple.students.db.database;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.musala.simple.students.db.database.DatabaseTypes;
import com.musala.simple.students.db.database.impl.MyMongoDatabase;

/**
 * This is a Factory class for creating {@link DatabaseCommands} objects. The
 * constructor requires the database properties to be provided in the form of a
 * {@link Properties} object passed to the constructor along with the database
 * type {@link DatabaseTypes} enum.
 * 
 * 
 * @author yoan.petrushinov
 *
 */
public class DatabaseFactory {
	private static final int DEFAULT_DB_PORT_INT = 27017;

	private Database database;
	
	private static Logger logger = LoggerFactory.getLogger(DatabaseFactory.class);
	
	public DatabaseFactory (Database database) {
		this.database = database;
	}

	/**
	 * Creates a new implementation of the {@link DatabaseCommands}.
	 * 
	 * @return a new database of the required type
	 */
	public static DatabaseFactory createDatabase(DatabaseTypes dbType) {
		Database database = null;
		switch (dbType) {
			case MySQL:
				// TODO
				break;
			case MongoDb:
				database = new MyMongoDatabase();
				break;
			case PostgreSQL:
				// TODO
				break;
			default:
				database = new MyMongoDatabase();
				break;
		}
		return new DatabaseFactory(database);
	}

	/**
	 * 
	 * @param name
	 *            for the database being created
	 */
	public DatabaseFactory withName(String name) {
		this.database.setName(name);
		return this;
	}

	/**
	 * 
	 * @param host
	 *            for the database being created
	 */
	public DatabaseFactory withHost(String host) {
		this.database.setHost(host);
		return this;
	}

	/**
	 * 
	 * @param port
	 *            for the database being created
	 */
	public DatabaseFactory withPort(String port) {
		try {
			this.database.setPort(Integer.parseInt(port));
		} catch (NumberFormatException e) {
			this.database.setPort(DEFAULT_DB_PORT_INT);
			logger.error("The port specified in the database configuration is invalid!");
			logger.info(String.format("The database port will be set to the default port: %d", DEFAULT_DB_PORT_INT));
		}
		return this;
	}

	/**
	 * 
	 * @param username
	 *            for authentication with the database being created
	 */
	public DatabaseFactory withUsername(String username) {
		this.database.setUsername(username);
		return this;
	}

	/**
	 * 
	 * @param password
	 *            for authentication with the database being created
	 */
	public DatabaseFactory withPassword(String password) {
		this.database.setPassword(password);
		return this;
	}

	public Database build() {
		return this.database;
	}
}
