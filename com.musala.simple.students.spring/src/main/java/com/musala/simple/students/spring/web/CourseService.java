package com.musala.simple.students.spring.web;

import java.util.ArrayList;
import java.util.List;

import com.musala.simple.students.spring.web.database.DatabaseType;
import com.musala.simple.students.spring.web.exception.CourseNotFoundException;
import com.musala.simple.students.spring.web.models.course.Course;
import com.musala.simple.students.spring.web.models.student.Student;

/**
 * This is a Service class acting as a middle layer between REST endpoints and the database/backend,
 * providing methods for database interactions with course entities.
 * 
 * @author yoan.petrushinov
 *
 */
public class CourseService extends DbService{

    private CourseService() {}
    
    public static boolean addCourse(Course course, DatabaseType dbType) {
        switch (dbType) {
            case MySQL:
                return mySqlDb.addCourse(course);
            case MongoDb:
                return mongoDb.addCourse(course);
            default:
                return false;
        }
    }

    public static boolean deleteCourse(DatabaseType dbType, int courseId) {
        switch (dbType) {
            case MySQL:
                try {
                    mySqlDb.deleteCourseById(courseId);
                    return true;
                } catch (CourseNotFoundException e) {
                    return false;
                }
            case MongoDb:
                try {
                    mongoDb.deleteCourseById(courseId);
                    return true;
                } catch (CourseNotFoundException e) {
                    return false;
                }
            default:
                return false;
        }
    }

    public static List<Course> getCourses(DatabaseType dbType) {
        switch (dbType) {
            case MySQL:
                return mySqlDb.getAllCoursesList();
            case MongoDb:
                return mongoDb.getAllCoursesList();
            default:
                return new ArrayList<>();
        }
    }

    public static Course getCourse(DatabaseType dbType, int courseId) {
        switch (dbType) {
            case MySQL:
                try {
                    return mySqlDb.getCourseById(courseId);
                } catch (CourseNotFoundException e) {
                    return null;
                }
            case MongoDb:
                try {
                    return mongoDb.getCourseById(courseId);
                } catch (CourseNotFoundException e) {
                    return null;
                }
            default:
                return null;
        }
    }
    
    public static List<Student> getCourseStudents(DatabaseType dbType, int courseId) {
        switch (dbType) {
            case MySQL:
                return mySqlDb.getCourseStudentsList(courseId);
            case MongoDb:
                return mongoDb.getCourseStudentsList(courseId);
            default:
                return new ArrayList<Student>();
        }
    }
}
