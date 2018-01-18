package com.musala.simple.students.db;

import java.util.List;

import com.musala.simple.students.db.exception.StudentNotFoundException;
import com.musala.simple.students.db.student.Student;

public interface Database {

	public void addStudent(Student student);

	public void addMultipleStudents(List<Student> students);

	public void addMultipleStudents(Student[] students);

	public void deleteStudentById(int studentId);

	public Student getStudentById(int sudentId) throws StudentNotFoundException;

	public Student[] getAllStudentsArr();

	public List<Student> getAllStudentsList();
}
