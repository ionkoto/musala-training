package com.musala.simple.students.exception;

/**
 * This is a custom exception class used when a
 * problem with invalid Student id occurs.
 * 
 * @author yoan.petrushinov
 *
 */
public class InvalidStudentIdException extends Exception {
	public InvalidStudentIdException(String message) {
        super(message);
    }
}