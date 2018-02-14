package com.musala.simple.students.db.student.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.musala.simple.students.spring.web.exception.StudentNotFoundException;
import com.musala.simple.students.spring.web.internal.ErrorMessage;
import com.musala.simple.students.spring.web.models.student.Student;
import com.musala.simple.students.spring.web.models.student.StudentGroup;

public class StudentGroupTest {
    private static Student[] studentsArrFirst;
    private static Student[] studentsArrSecond;
    private static Student[] studentsArrExistingIds;

    @Before
    public void initializeStudentsArr() {
        studentsArrFirst = new Student[] { new Student(1, "Ivan", 3, 5), new Student(2, "Petkan", 4, 2) };
        studentsArrSecond = new Student[] { new Student(5, "Misho", 3, 5), new Student(7, "Gosho", 4, 2) };
        studentsArrExistingIds = new Student[] { new Student(5, "Pesho", 3, 5), new Student(7, "Alex", 4, 2) };
    }

    @Test
    public void testFillStudentGroup() {
        StudentGroup sGroup = new StudentGroup();
        sGroup.fillStudentGroup(studentsArrFirst);
        assertEquals("Ivan", sGroup.getStudents().get(1).getName());
        sGroup.fillStudentGroup(studentsArrSecond);
        assertEquals("Ivan", sGroup.getStudents().get(1).getName());
        assertEquals("Misho", sGroup.getStudents().get(5).getName());
        assertEquals(4, sGroup.getStudents().size());
    }

    @Test
    public void testFillStudentGroupExistingIds() {
        StudentGroup sGroup = new StudentGroup();
        sGroup.fillStudentGroup(studentsArrSecond);
        assertEquals("Misho", sGroup.getStudents().get(5).getName());
        sGroup.fillStudentGroup(studentsArrExistingIds);
        assertEquals("Misho", sGroup.getStudents().get(5).getName());
    }

    @Test
    public void testGetStudents() {
        StudentGroup sGroup = new StudentGroup();
        sGroup.fillStudentGroup(studentsArrSecond);
        assertEquals("Misho", sGroup.getStudents().get(5).getName());
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testGetStudentByIdInvalidId() throws StudentNotFoundException {
        exception.expect(StudentNotFoundException.class);
        exception.expectMessage(ErrorMessage.STUDENT_NOT_EXISTS);
        StudentGroup sGroup = new StudentGroup();
        sGroup.fillStudentGroup(studentsArrSecond);
        sGroup.getStudentById(10);
    }

    @Test
    public void testGetStudentByIdValidId() throws StudentNotFoundException {
        StudentGroup sGroup = new StudentGroup();
        sGroup.fillStudentGroup(studentsArrSecond);
        assertEquals("Misho", sGroup.getStudentById(5).getName());
    }
}
