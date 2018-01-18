package com.musala.simple.students.db.exception;

/**
 * This is a custom exception class. The user can throw
 * this exception, when a studente object can't be found
 * when looked for in a Student Group or Database
 * 
 * @author yoan.petrushinov
 *
 */
public class StudentNotFoundException extends Exception {
	public StudentNotFoundException(String message) {
		super(message);
	}
}