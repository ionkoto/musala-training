package com.musala.simple.students.db.database.impl.test;

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.musala.simple.students.spring.web.database.AbstractDatabase;
import com.musala.simple.students.spring.web.database.DatabaseFactory;
import com.musala.simple.students.spring.web.database.DatabaseType;
import com.musala.simple.students.spring.web.exception.StudentNotFoundException;
import com.musala.simple.students.spring.web.student.Student;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class MyMongoDatabaseTest {

    private MongoServer server;
    private AbstractDatabase mongoDatabase;
    private Student firstStudent;
    private Student secondStudent;
    private Student thirdStudent;

    @BeforeClass
    public void setUpConnection() {
        server = new MongoServer(new MemoryBackend());
        InetSocketAddress serverAddress = server.bind();
        mongoDatabase = DatabaseFactory
                .createDatabase(DatabaseType.MongoDb)
                .withHost(serverAddress.getHostString())
                .withPort(Integer.toString(serverAddress.getPort()))
                .withName("studentsDb")
                .build();
        mongoDatabase.establishConnection();
    }
    
    @Before
    public void setUpTestData() {
        firstStudent = new Student(1, "Alex", 18, 11);
        secondStudent = new Student(2, "Pesho", 20, 12);
        thirdStudent = new Student(3, "Gosho", 19, 12);
    }

    @AfterClass
    public void tearDown() {
        server.shutdown();
    }

    @Test
    public void testAddStudent() throws StudentNotFoundException {
        mongoDatabase.addStudent(firstStudent);
        mongoDatabase.addStudent(secondStudent);

        assertEquals(firstStudent.getName(), mongoDatabase.getStudentById(1).getName());
        assertEquals(secondStudent.getId(), mongoDatabase.getStudentById(2).getId());
        assertEquals(2, mongoDatabase.getAllStudentsArr().length);
    }

    @Test
    public void testAddStudentDuplicate() throws StudentNotFoundException {
        mongoDatabase.addStudent(firstStudent);
        mongoDatabase.addStudent(firstStudent);

        assertEquals(1, mongoDatabase.getAllStudentsArr().length);
    }

    @Test
    public void testDeleteStudent() throws StudentNotFoundException {
        mongoDatabase.addStudent(firstStudent);
        mongoDatabase.addStudent(secondStudent);
        mongoDatabase.addStudent(thirdStudent);

        assertEquals(3, mongoDatabase.getAllStudentsArr().length);

        mongoDatabase.deleteStudentById(1);

        assertEquals(2, mongoDatabase.getAllStudentsArr().length);
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testDeleteStudentNotExists() throws StudentNotFoundException {
        exception.expect(StudentNotFoundException.class);
        mongoDatabase.addStudent(firstStudent);
        mongoDatabase.addStudent(secondStudent);

        assertEquals(2, mongoDatabase.getAllStudentsArr().length);

        mongoDatabase.deleteStudentById(11);
    }

    @Test
    public void testGetStudentById() throws StudentNotFoundException {
        mongoDatabase.addStudent(firstStudent);
        mongoDatabase.addStudent(secondStudent);
        mongoDatabase.addStudent(thirdStudent);

        assertEquals(firstStudent.getName(), mongoDatabase.getStudentById(1).getName());
        assertEquals(secondStudent.getName(), mongoDatabase.getStudentById(2).getName());
        assertEquals(thirdStudent.getName(), mongoDatabase.getStudentById(3).getName());
    }
    
    @Test 
    public void testGetAllStudentsArr() {
        assertEquals(0, mongoDatabase.getAllStudentsArr().length);
        
        mongoDatabase.addStudent(firstStudent);
        mongoDatabase.addStudent(secondStudent);
        mongoDatabase.addStudent(thirdStudent);
        
        assertEquals(3, mongoDatabase.getAllStudentsArr().length);
        assertEquals(true, mongoDatabase.getAllStudentsArr() instanceof Student[]);
    }
    
    @Test 
    public void testGetAllStudentsList() {
        assertEquals(0, mongoDatabase.getAllStudentsList().size());
        
        mongoDatabase.addStudent(firstStudent);
        mongoDatabase.addStudent(secondStudent);
        mongoDatabase.addStudent(thirdStudent);
        
        assertEquals(3, mongoDatabase.getAllStudentsList().size());
        assertEquals(true, mongoDatabase.getAllStudentsList() instanceof List<?>);
    }
    
    @Test 
    public void testAddMultipleStudentsList() {
        assertEquals(0, mongoDatabase.getAllStudentsList().size());
        
        List<Student> studentsList = new ArrayList<Student>();
        studentsList.add(firstStudent);
        studentsList.add(secondStudent);
        studentsList.add(thirdStudent);
        
        mongoDatabase.addMultipleStudents(studentsList);
        
        assertEquals(3, mongoDatabase.getAllStudentsList().size());
    }
    
    @Test 
    public void testAddMultipleStudentsArr() {
        assertEquals(0, mongoDatabase.getAllStudentsArr().length);
        
        Student[] studentsArr = new Student[3];
        studentsArr[0] = (firstStudent);
        studentsArr[1] = (secondStudent);
        studentsArr[2] = (thirdStudent);
        
        mongoDatabase.addMultipleStudents(studentsArr);
        
        assertEquals(3, mongoDatabase.getAllStudentsArr().length);
    }
}
