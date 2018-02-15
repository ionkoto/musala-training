package com.musala.simple.students.spring.web.dbevents;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Database event types for logging course related events to the event logger.
 * The event "code" property is used when handling the events
 * in the front-end.
 */

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DbEventsCourses {
    DeleteCourseSuccessMySql("Course was deleted successfully!", "3.1.1"),
    DeleteCourseSuccessMongo("Course was deleted successfully!", "3.1.2"),
    DeleteCourseFail("Couldn't delete course", "1.1"),
    AddCourseSuccessMySql("Course was added successfully!", "3.2.1"),
    AddCourseSuccessMongo("Course was added successfully!", "3.2.2"),
    AddCourseFail("Couldn't add course to DB", "1.2"),
    AddDuplicateCourseFail("Course with same ID already exists.", "1.3"),
    AddMultipleSuccess("Courses were added successfully!", "2.3"),
    AddMultipleFail("Couldn't add courses to DB", "1.4"),
    GetCourseSuccess("Course loaded successfully!", "2.4"),
    GetCourseFail("Couldn't get course from DB", "1.5"),
    GetCoursesSuccess("Courses were loaded successfully!", "2.5"),
    GetCoursesFail("Couldn't load courses from DB", "1.6"),
    GetCourseStudentsSuccess("Course's students were loaded successfully!", "2.6"),
    GetCourseStudentsFail("Couldn't load course's students from DB", "1.7");

    private final String message;
    private final String code;

    DbEventsCourses(String message, String code) {
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
