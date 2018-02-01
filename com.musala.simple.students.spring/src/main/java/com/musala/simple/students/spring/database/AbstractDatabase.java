package com.musala.simple.students.spring.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
