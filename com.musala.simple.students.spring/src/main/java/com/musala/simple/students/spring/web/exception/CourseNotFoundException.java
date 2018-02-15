package com.musala.simple.students.spring.web.exception;

/**
 * This is a custom exception class. The user can throw
 * this exception, when a teacher object can't be found
 * when looked for in some 
 * AbstractDatabase implementation (e.g. MyMongoDatabase)
 * 
 * @author yoan.petrushinov
 *
 */
public class CourseNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public CourseNotFoundException(String message) {
        super(message);
    }
}