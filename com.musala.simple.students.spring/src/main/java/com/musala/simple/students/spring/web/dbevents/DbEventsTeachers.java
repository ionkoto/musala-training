package com.musala.simple.students.spring.web.dbevents;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Database event types for logging teacher related events to the event logger.
 * The event "code" property is used when handling the events
 * in the front-end.
 */

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DbEventsTeachers {
    DeleteTeacherSuccessMySql("Teacher was deleted successfully!", "3.1.1"),
    DeleteTeacherSuccessMongo("Teacher was deleted successfully!", "3.1.2"),
    DeleteTeacherFail("Couldn't delete teacher", "1.1"),
    AddTeacherSuccessMySql("Teacher was added successfully!", "3.2.1"),
    AddTeacherSuccessMongo("Teacher was added successfully!", "3.2.2"),
    AddTeacherFail("Couldn't add teacher to DB", "1.2"),
    AddDuplicateTeacherFail("Teacher with same ID already exists.", "1.3"),
    AddMultipleSuccess("Teachers were added successfully!", "2.3"),
    AddMultipleFail("Couldn't add teachers to DB", "1.4"),
    GetTeacherSuccess("Teacher loaded successfully!", "2.4"),
    GetTeacherFail("Couldn't get teacher from DB", "1.5"),
    GetTeachersSuccess("Teachers were loaded successfully!", "2.5"),
    GetTeachersFail("Couldn't load teachers to DB", "1.6");

    private final String message;
    private final String code;

    DbEventsTeachers(String message, String code) {
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
