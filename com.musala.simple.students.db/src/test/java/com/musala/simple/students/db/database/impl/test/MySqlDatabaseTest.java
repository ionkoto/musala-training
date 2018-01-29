package com.musala.simple.students.db.database.impl.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.musala.simple.students.db.database.AbstractDatabase;
import com.musala.simple.students.db.database.DatabaseFactory;
import com.musala.simple.students.db.database.DatabaseType;
import com.musala.simple.students.db.database.impl.MySqlDatabase;
import com.musala.simple.students.db.exception.StudentNotFoundException;
import com.musala.simple.students.db.internal.ErrorMessage;
import com.musala.simple.students.db.student.Student;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;

public class MySqlDatabaseTest {

    private static DB dbServer;
    private static AbstractDatabase mySQLDataBase;
    private Student firstStudent;
    private Student secondStudent;
    private Student thirdStudent;

    @BeforeClass
    public static synchronized void setUpDbConnection() {
        try {
            DBConfigurationBuilder configBuilder = DBConfigurationBuilder.newBuilder();
            configBuilder.setPort(3307);
            dbServer = DB.newEmbeddedDB(configBuilder.build());
            dbServer.start();
            dbServer.source("testDb.sql", "root", null, "test");
        } catch (ManagedProcessException e) {
            throw new IllegalStateException("Error during test setup: " + ErrorMessage.DATABASE_CONNECTION_FAIL, e);
        }

        mySQLDataBase = (MySqlDatabase) DatabaseFactory.createDatabase(DatabaseType.MySQL).withHost("localhost")
                .withPort("3307").withUsername("root").withPassword("").withName("test").build();

        mySQLDataBase.establishConnection();
    }

    @Before
    public void setupTestData() {
        firstStudent = new Student(1, "Alex", 18, 11);
        secondStudent = new Student(2, "Pesho", 20, 12);
        thirdStudent = new Student(3, "Gosho", 19, 12);
    }

    @AfterClass
    public static synchronized void tearDownDbConnection() {
        try {
            dbServer.stop();
        } catch (ManagedProcessException e) {
            throw new IllegalStateException("Error during test tear down: " + ErrorMessage.DATABASE_CONNECTION_FAIL, e);
        }
    }

    @Test
    public void testAddStudent() throws Exception {
        mySQLDataBase.addStudent(firstStudent);
        mySQLDataBase.addStudent(secondStudent);
        mySQLDataBase.addStudent(thirdStudent);
        assertEquals(firstStudent.getName(), mySQLDataBase.getStudentById(1).getName());
        assertEquals("Gosho", mySQLDataBase.getStudentById(3).getName());
        assertEquals(3, mySQLDataBase.getAllStudentsArr().length);
    }

    @Test
    public void testAddMultipleStudentsList() throws Exception {
        List<Student> studentsList = new ArrayList<>();
        studentsList.add(firstStudent);
        studentsList.add(secondStudent);
        studentsList.add(thirdStudent);
        mySQLDataBase.addMultipleStudents(studentsList);
        assertEquals(3, mySQLDataBase.getAllStudentsArr().length);
    }

    @Test
    public void testAddMultipleStudentsArr() throws Exception {
        Student[] studentsArr = new Student[3];
        studentsArr[0] = (firstStudent);
        studentsArr[1] = (secondStudent);
        studentsArr[2] = (thirdStudent);
        mySQLDataBase.addMultipleStudents(studentsArr);
        assertEquals(3, mySQLDataBase.getAllStudentsArr().length);
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testDeleteStudentById() throws StudentNotFoundException {
        exception.expect(StudentNotFoundException.class);

        Student[] studentsList = new Student[3];
        studentsList[0] = (firstStudent);
        studentsList[1] = (secondStudent);
        studentsList[2] = (thirdStudent);

        mySQLDataBase.addMultipleStudents(studentsList);

        assertEquals(3, mySQLDataBase.getAllStudentsArr().length);

        mySQLDataBase.deleteStudentById(2);

        mySQLDataBase.getStudentById(2);
    }

    @Test
    public void testGetStudentById() throws StudentNotFoundException {
        Student[] studentsList = new Student[3];
        studentsList[0] = (firstStudent);
        studentsList[1] = (secondStudent);
        studentsList[2] = (thirdStudent);

        mySQLDataBase.addMultipleStudents(studentsList);

        assertEquals("Alex", mySQLDataBase.getStudentById(1).getName());
        assertEquals("Pesho", mySQLDataBase.getStudentById(2).getName());
        assertEquals("Gosho", mySQLDataBase.getStudentById(3).getName());
    }

    @Test
    public void testGetAllStudentsArr() throws StudentNotFoundException {
        mySQLDataBase.deleteStudentById(1);
        mySQLDataBase.deleteStudentById(2);
        mySQLDataBase.deleteStudentById(3);
        assertEquals(0, mySQLDataBase.getAllStudentsArr().length);

        mySQLDataBase.addStudent(firstStudent);
        mySQLDataBase.addStudent(secondStudent);
        mySQLDataBase.addStudent(thirdStudent);

        assertEquals(3, mySQLDataBase.getAllStudentsArr().length);
        assertEquals(true, mySQLDataBase.getAllStudentsArr() instanceof Student[]);
    }

    @Test
    public void testGetAllStudentsList() throws StudentNotFoundException {
        mySQLDataBase.deleteStudentById(1);
        mySQLDataBase.deleteStudentById(2);
        mySQLDataBase.deleteStudentById(3);
        assertEquals(0, mySQLDataBase.getAllStudentsList().size());

        mySQLDataBase.addStudent(firstStudent);
        mySQLDataBase.addStudent(secondStudent);
        mySQLDataBase.addStudent(thirdStudent);

        assertEquals(3, mySQLDataBase.getAllStudentsList().size());
        assertEquals(true, mySQLDataBase.getAllStudentsList() instanceof List<?>);
    }
}
