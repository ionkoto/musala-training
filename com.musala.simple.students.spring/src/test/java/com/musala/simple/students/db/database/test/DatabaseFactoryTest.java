package com.musala.simple.students.db.database.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.musala.simple.students.spring.web.database.AbstractDatabase;
import com.musala.simple.students.spring.web.database.DatabaseFactory;
import com.musala.simple.students.spring.web.database.DatabaseType;
import com.musala.simple.students.spring.web.database.impl.MyMongoDatabase;
import com.musala.simple.students.spring.web.database.impl.MySqlDatabase;

public class DatabaseFactoryTest {

    @Test
    public void testCreateDatabaseMongo() {
        AbstractDatabase mongoDb = DatabaseFactory.createDatabase(DatabaseType.MongoDb).build();
        assertEquals(true, mongoDb instanceof MyMongoDatabase);
    }

    @Test
    public void testCreateDatabaseMySql() {
        AbstractDatabase mySqlDb = DatabaseFactory.createDatabase(DatabaseType.MySQL).build();
        assertEquals(true, mySqlDb instanceof MySqlDatabase);
    }
}
