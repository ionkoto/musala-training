package com.musala.simple.students.spring.web.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.musala.simple.students.spring.web.dbevents.Event;
import com.musala.simple.students.spring.web.dbevents.EventLogger;

public abstract class AbstractDatabase implements DatabaseCommands, DatabaseProperties {
    private String name;
    private String host;
    private int port;
    private String username;
    private String password;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * Gives the user a {@link Logger} instance for logging
     * messages.
     *
     * @return the Logger instance
     */
    protected Logger getLogger() {
        return this.logger;
    }

    /**
     * Abstract method for the user to initialize database connection
     * after creating a new {@link AbstractDatabase} implementation instance.
     *
     */
    public abstract void establishConnection();
    

    /**
     * Registers event to the static event logger to indicate the result
     * of a certain database operation.
     *
     * @param object of class Event - the event type being logged
     */
    public void registerEvent(Event event) {
        EventLogger.addEvent(event);
    }
}
