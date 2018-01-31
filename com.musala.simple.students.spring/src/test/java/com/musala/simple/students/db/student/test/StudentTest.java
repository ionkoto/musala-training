package com.musala.simple.students.db.student.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.musala.simple.students.spring.student.Student;

public class StudentTest {

    private Student student = new Student(2, "Ivan", 23, 10);

    @Test
    public void testToString() {
        assertEquals("2   Ivan                 23    10   ", student.toString());
    }

    @Test
    public void testGetId() {
        assertEquals(2, student.getId());
    }
    
    @Test
    public void testGetName() {
        assertEquals("Ivan", student.getName());
    }
    
    @Test
    public void testGetGrade() {
        assertEquals(10, student.getGrade());
    }
    
    @Test
    public void testGetAge() {
        assertEquals(23, student.getAge());
    }
}
