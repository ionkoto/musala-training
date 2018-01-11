package com.musala.simple.students;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The class {@code StudentGroup} groups together a certain amount 
 * of {@link Student} objects and provides the user with
 * methods for filling a group with students, getting a List of
 * all students from a group and getting a specific student by id.
 * 
 * @author yoan.petrushinov
 *
 */
public class StudentGroup {
	private List<Student> students;
	
	public StudentGroup() { 
		this.students = new ArrayList<Student>();
	}
	
	/**
	 * Populates the {@link StudentGroup}'s students property with
	 * {@link Student} objects.
	 *
	 * @param  studentsArr  Array of Student objects
	 */
	public void fillStudentGroup (Student[] studentsArr) {
		students = Arrays.asList(studentsArr);
	}

	/**
	 * Casts the {@link StudentGroup#students} field to an unmodifiable
	 * List of Student objects and returns it to the user.
	 *
	 * @return		An unmodifiable List of Student objects
	 */
	public List<Student> getStudents() {
		return Collections.unmodifiableList(this.students);
	}
	
	/**
	 * Validates the id provided as a parameter and returns the requested
	 * Student object. If the Id is out of the bounds of the list or
	 * a student with the provided Id does not exist, the method throws
	 * an Exception.
	 *
	 * @param  id        Id of the user being requested
	 * @return student   A Student object from the {@link StudentGroup#students}
	 * 					 corresponding to the provided id
	 */
	public Student getStudentById (int id) {
		// Validate id
		if ((id > this.students.size() - 1)) { 
			// Id is out of the bounds of the List<Student>
			throw new IllegalArgumentException("A student with the given ID does not exist.");
		}
		
		Student student = this.students.get(id);
		
		if (student == null) {  
			throw new IllegalArgumentException("A student with the given ID does not exist.");
		}
		return student;
	}
}
