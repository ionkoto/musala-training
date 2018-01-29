package com.musala.simple.students.db.exception;

/**
 * This is a custom exception class. The user can throw
 * this exception, when a student object can't be found
 * when looked for in a Student Group or some
 * AbstractDatabase implementation (e.g. MyMongoDatabase)
 * 
 * @author yoan.petrushinov
 *
 */
public class StudentNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public StudentNotFoundException(String message) {
        super(message);
    }
}