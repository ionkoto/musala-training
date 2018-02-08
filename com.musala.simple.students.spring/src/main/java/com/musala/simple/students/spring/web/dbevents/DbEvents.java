package com.musala.simple.students.spring.web.dbevents;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Database event types for logging to the event logger.
 * The event "code" property is used when handling the events
 * in the front-end.
 */

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DbEvents {
    DeleteStudentSuccessMySql("Student was deleted successfully!", "3.1.1"),
    DeleteStudentSuccessMongo("Student was deleted successfully!", "3.1.2"),
    DeleteStudentFail("Couldn't delete student", "1.1"),
    AddStudentSuccessMySql("Student was added successfully!", "3.2.1"),
    AddStudentSuccessMongo("Student was added successfully!", "3.2.2"),
    AddStudentFail("Couldn't add student to DB", "1.2"),
    AddDuplicateStudentFail("Student with same ID already exists.", "1.3"),
    AddMultipleSuccess("Students were added successfully!", "2.3"),
    AddMultipleFail("Couldn't add students to DB", "1.4"),
    GetStudentSuccess("Student loaded successfully!", "2.4"),
    GetStudentFail("Couldn't get student from DB", "1.5"),
    GetStudentsSuccess("Students were loaded successfully!", "2.5"),
    GetStudentsFail("Couldn't load students to DB", "1.6");

    private final String message;
    private final String code;

    DbEvents(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
