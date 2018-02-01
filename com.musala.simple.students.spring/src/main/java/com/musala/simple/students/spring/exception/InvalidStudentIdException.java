package com.musala.simple.students.spring.exception;

/**
 * This is a custom exception class used when a problem with invalid Student id
 * occurs.
 * 
 * @author yoan.petrushinov
 *
 */
public class InvalidStudentIdException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidStudentIdException(String message) {
        super(message);
    }
}