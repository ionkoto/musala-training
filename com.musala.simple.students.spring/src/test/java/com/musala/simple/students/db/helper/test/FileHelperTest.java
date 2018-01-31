package com.musala.simple.students.db.helper.test;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.musala.simple.students.spring.database.DatabaseType;
import com.musala.simple.students.spring.helper.FileHelper;

public class FileHelperTest {

    @Test
    public void testReadFileValidFile() {
        String path = "src/main/resources/test.txt";
        assertEquals("This is a test.", FileHelper.readFile(path));
    }
    
    @Test
    public void testReadFileInvalidValidFile() {
        String path = "src/main/resources/test2.txt";
        assertEquals(null, FileHelper.readFile(path));
    }

    @Test
    public void testReadDbPropertiesMongoValid() {
        DatabaseType databaseType = DatabaseType.MongoDb;
        assertEquals("27017", FileHelper.readDbPropertiesFile(databaseType).getProperty("port"));
    }
    
    @Test
    public void testReadDbPropertiesMySqlValid() {
        DatabaseType databaseType = DatabaseType.MySQL;
        assertEquals("3306", FileHelper.readDbPropertiesFile(databaseType).getProperty("port"));
    }
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void testReadDbPropertiesInvalid() {
        exception.expect(NullPointerException.class);
        assertEquals("3306", FileHelper.readDbPropertiesFile(null).getProperty("port"));
    }
}
