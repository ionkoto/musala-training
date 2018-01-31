package com.musala.simple.students.db.database.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.musala.simple.students.spring.database.AbstractDatabase;
import com.musala.simple.students.spring.database.DatabaseFactory;
import com.musala.simple.students.spring.database.DatabaseType;
import com.musala.simple.students.spring.database.impl.MyMongoDatabase;
import com.musala.simple.students.spring.database.impl.MySqlDatabase;

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
