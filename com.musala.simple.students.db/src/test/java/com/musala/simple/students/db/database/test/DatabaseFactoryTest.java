package com.musala.simple.students.db.database.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.musala.simple.students.db.database.AbstractDatabase;
import com.musala.simple.students.db.database.DatabaseFactory;
import com.musala.simple.students.db.database.DatabaseType;
import com.musala.simple.students.db.database.impl.MyMongoDatabase;
import com.musala.simple.students.db.database.impl.MySqlDatabase;

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
