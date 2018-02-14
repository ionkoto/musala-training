package com.musala.simple.students.spring.web.database;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.musala.simple.students.spring.web.database.impl.MyMongoDatabase;
import com.musala.simple.students.spring.web.database.impl.MySqlDatabase;
import com.musala.simple.students.spring.web.internal.InfoMessage;

/**
 * This is a Factory class for creating {@link DatabaseStudentCommands} objects. The
 * constructor requires the database properties to be provided in the form of a
 * {@link Properties} object passed to the constructor along with the database
 * type {@link DatabaseType} enum.
 * 
 * 
 * @author yoan.petrushinov
 *
 */
public class DatabaseFactory {
    private static final int DEFAULT_DB_PORT_INT = 27017;

    private AbstractDatabase database;

    private static Logger logger = LoggerFactory.getLogger(DatabaseFactory.class);

    public DatabaseFactory(AbstractDatabase database) {
        this.database = database;
    }

    /**
     * Instantiates a new database implementation according to the dbType
     * provided and passes that implementation to the DatabaseFactory constructor.
     * 
     * @param dbType the type of the database being created
     * @return a call to the DatabaseFactory constructor
     */
    public static DatabaseFactory createDatabase(DatabaseType dbType) {
        AbstractDatabase database = null;
        switch (dbType) {
            case MySQL:
                database = MySqlDatabase.getInstance();
                break;
            case MongoDb:
                database = MyMongoDatabase.getInstance();
                break;
            default:
                logger.info(InfoMessage.DEFAULT_DATABASE_INITIALIZATION);
                database = MyMongoDatabase.getInstance();
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

    /**
     * 
     * @return the current database instance
     */
    public AbstractDatabase build() {
        return this.database;
    }
}
