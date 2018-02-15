package com.musala.simple.students.spring.web.internal;

/**
 * This is an internal class, holding constant variables,
 * used for Info-messaging.
 * 
 * @author yoan.petrushinov
 *
 */
public class InfoMessage {
    public static final String DATABASE_CONNECTION_SUCCESS = "Database connection established successfully!";
    public static final String STUDENT_DELETE_SUCCESS = "Student with id %d successfully deleted!";
    public static final String TEACHER_DELETE_SUCCESS = "Teacher with id %d successfully deleted!";
    public static final String COURSE_DELETE_SUCCESS = "Course with id %d successfully deleted!";
    public static final String STUDENT_GET_ALL_SUCCESS = "Successfully retrieved all the students from the database.";
    public static final String TEACHER_GET_ALL_SUCCESS = "Successfully retrieved all the teachers from the database.";
    public static final String COURSE_GET_ALL_SUCCESS = "Successfully retrieved all the courses from the database.";
    public static final String COURSE_GET_STUDENTS_SUCCESS = "Successfully retrieved all students enrolled in this course";
    public static final String DEFAULT_DATABASE_INITIALIZATION = "A valid database type was'n provided. A new Mongo Database will be initiliazed as a default option.";

    private InfoMessage() {

    }
}
