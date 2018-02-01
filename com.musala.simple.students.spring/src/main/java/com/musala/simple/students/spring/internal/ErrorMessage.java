package com.musala.simple.students.spring.internal;

/**
 * This is an internal class, holding constant variables,
 * used for Error-messaging.
 * 
 * @author yoan.petrushinov
 *
 */
public class ErrorMessage {
    public static final String PATH_MISSING = "Path is missing.";
    public static final String INVALID_PATH = "Invalid path specified.";
    public static final String NOT_A_FILE = "The path specified is not an existing file.";
    public static final String FILE_NOT_JSON = "The file specified is not of '.json' format.";
    public static final String NOT_VALID_JSON = "The content of the JSON file is not a valid JSON string.";
    public static final String STUDENT_NOT_FOUND = "Requested student does not exist in the current Student group.";
    public static final String STUDENT_ID_MISSING = "Student Id was not provided.";
    public static final String NEGATIVE_ID = "The ID can't be a negative integer!";
    public static final String STUDENT_NOT_EXISTS = "A student with the given ID does not exist.";
    public static final String INVALID_ID_FORMAT = "The id must be a valid not-negative number!.";
    public static final String DATABASE_EMPTY = "The students database is currently empty.";
    public static final String DATABASE_CONNECTION_FAIL = "Cannot connect the database!";

    private ErrorMessage() {

    }
}
