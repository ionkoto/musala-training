package com.musala.simple.students.db.database;

import java.util.List;

import com.musala.simple.students.db.exception.StudentNotFoundException;
import com.musala.simple.students.db.student.Student;

/**
 * The interface serves as an middle abstraction layer for 
 * database communication. It gives the user the basic 
 * functionality when working with a database, providing the
 * opportunity to use different implementation depending on
 * the preferred database type.
 * 
 * @author yoan.petrushinov
 *
 */
public interface DatabaseCommands {

	/**
	 * Adds a student to the database.
	 *
	 * @param  A {@link Student} object to add
	 * 		   to the database
	 */
	public void addStudent(Student student);

	/**
	 * Adds multiple  students to the database.
	 *
	 * @param  A List of {@link Student} objects 
	 * 		   to add to the database
	 */
	public void addMultipleStudents(List<Student> students);

	/**
	 * Adds multiple  students to the database.
	 *
	 * @param  An array of {@link Student} objects 
	 * 		   to add to the database
	 */
	public void addMultipleStudents(Student[] students);

	/**
	 * Deletes a single student from the database by
	 * given id.
	 *
	 * @param  An int id to query the student by.
	 * @throws StudentNotFoundException 
	 */
	public void deleteStudentById(int studentId) throws StudentNotFoundException;

	/**
	 * Retrieves a single student from the database by
	 * given id. The method throws a {@link StudentNotFoundException}
	 * in case a student with the given id does not exist
	 * in the database.
	 *
	 * @param  An int id to query the student by.
	 */
	public Student getStudentById(int sudentId) throws StudentNotFoundException;

	/**
	 * Retrieves all students from the database as 
	 * an Array of {@link Student} objects.
	 *
	 * @param  An int id to query the student by.
	 */
	public Student[] getAllStudentsArr();

	/**
	 * Retrieves all students from the database as 
	 * a List of {@link Student} objects.
	 *
	 * @param  An int id to query the student by.
	 */
	public List<Student> getAllStudentsList();
}
