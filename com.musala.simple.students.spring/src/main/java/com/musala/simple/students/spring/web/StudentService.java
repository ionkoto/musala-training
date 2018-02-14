package com.musala.simple.students.spring.web;

import java.util.ArrayList;
import java.util.List;

import com.musala.simple.students.spring.web.database.DatabaseType;
import com.musala.simple.students.spring.web.exception.StudentNotFoundException;
import com.musala.simple.students.spring.web.models.student.Student;

/**
 * This is a Service class acting as a middle layer between REST endpoints and the database/backend,
 * providing methods for database interactions with student entities.
 * 
 * @author yoan.petrushinov
 *
 */
public class StudentService extends DbService{
    
    private StudentService() {}
    
    public static boolean addStudent(Student student, DatabaseType dbType) {
        switch (dbType) {
            case MySQL:
                return mySqlDb.addStudent(student);
            case MongoDb:
                return mongoDb.addStudent(student);
            default:
                return false;
        }
    }

    public static boolean deleteStudent(DatabaseType dbType, int studentId) {
        switch (dbType) {
            case MySQL:
                try {
                    mySqlDb.deleteStudentById(studentId);
                    return true;
                } catch (StudentNotFoundException e) {
                    return false;
                }
            case MongoDb:
                try {
                    mongoDb.deleteStudentById(studentId);
                    return true;
                } catch (StudentNotFoundException e) {
                    return false;
                }
            default:
                return false;
        }
    }

    public static List<Student> getStudents(DatabaseType dbType) {
        switch (dbType) {
            case MySQL:
                return mySqlDb.getAllStudentsList();
            case MongoDb:
                return mongoDb.getAllStudentsList();
            default:
                return new ArrayList<>();
        }
    }

    public static Student getStudent(DatabaseType dbType, int studentId) {
        switch (dbType) {
            case MySQL:
                try {
                    return mySqlDb.getStudentById(studentId);
                } catch (StudentNotFoundException e) {
                    return null;
                }
            case MongoDb:
                try {
                    return mongoDb.getStudentById(studentId);
                } catch (StudentNotFoundException e) {
                    return null;
                }
            default:
                return null;
        }
    }
}
