package com.musala.simple.students.spring.web;

import com.musala.simple.students.spring.web.database.AbstractDatabase;
import com.musala.simple.students.spring.web.database.DatabaseType;
import com.musala.simple.students.spring.web.helper.DbHelper;

public class DbService {
    protected static AbstractDatabase mongoDb;
    protected static AbstractDatabase mySqlDb;

    public static void establishDbConnection() {
        mongoDb = DbHelper.initializeDatabase(DatabaseType.MongoDb);
        mySqlDb = DbHelper.initializeDatabase(DatabaseType.MySQL);
    }
}
