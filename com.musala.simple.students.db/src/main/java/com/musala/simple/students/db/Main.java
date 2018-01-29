package com.musala.simple.students.db;

import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.musala.simple.students.db.database.AbstractDatabase;
import com.musala.simple.students.db.database.DatabaseType;
import com.musala.simple.students.db.helper.DbHelper;
import com.musala.simple.students.db.internal.InfoMessage;

public class Main {
    private static final String PROMPT_USER = "Choose database: \nPress '1' for MySql, press '2' for MongoDb, press '3' for both. Press 'ENTER' to confirm.";

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        /*
         * Sets up a default configuration for the log4j logger. This is suitable for printing
         * out logging statements to the standard console. For writing logs to a file you need
         * to create a configuration file with more specific settings. Please refer to:
         * http://www.codejava.net/coding/how-to-configure-log4j-as-logging-mechanism-in-java
         */
        BasicConfigurator.configure();

        System.out.println(PROMPT_USER);
        Scanner scanner = new Scanner(System.in);

        AbstractDatabase mySqlDb;
        AbstractDatabase mongoDb;

        int dbChoice = scanner.nextInt();
        scanner.close();
        switch (dbChoice) {
            case 1:
                mySqlDb = DbHelper.initializeDatabase(DatabaseType.MySQL);
                DbHelper.performDatabaseActions(args, mySqlDb);
                break;
            case 2:
                mongoDb = DbHelper.initializeDatabase(DatabaseType.MongoDb);
                DbHelper.performDatabaseActions(args, mongoDb);
                break;
            case 3:
                mySqlDb = DbHelper.initializeDatabase(DatabaseType.MySQL);
                mongoDb = DbHelper.initializeDatabase(DatabaseType.MongoDb);
                DbHelper.performDatabaseActions(args, mySqlDb);
                DbHelper.performDatabaseActions(args, mongoDb);
                break;
            default:
                mongoDb = DbHelper.initializeDatabase(DatabaseType.MongoDb);
                DbHelper.performDatabaseActions(args, mongoDb);
                logger.info(InfoMessage.DEFAULT_DATABASE_INITIALIZATION);
                break;
        }
    }
}
