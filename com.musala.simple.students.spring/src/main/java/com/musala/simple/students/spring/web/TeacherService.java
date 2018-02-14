package com.musala.simple.students.spring.web;

import java.util.ArrayList;
import java.util.List;

import com.musala.simple.students.spring.web.database.DatabaseType;
import com.musala.simple.students.spring.web.exception.TeacherNotFoundException;
import com.musala.simple.students.spring.web.models.teacher.Teacher;

/**
 * This is a Service class acting as a middle layer between REST endpoints and the database/backend,
 * providing methods for database interactions with teacher entities.
 * 
 * @author yoan.petrushinov
 *
 */
public class TeacherService extends DbService{

    private TeacherService() {}
    
    public static boolean addTeacher(Teacher teacher, DatabaseType dbType) {
        switch (dbType) {
            case MySQL:
                return mySqlDb.addTeacher(teacher);
            case MongoDb:
                return mongoDb.addTeacher(teacher);
            default:
                return false;
        }
    }

    public static boolean deleteTeacher(DatabaseType dbType, int teacherId) {
        switch (dbType) {
            case MySQL:
                try {
                    mySqlDb.deleteTeacherById(teacherId);
                    return true;
                } catch (TeacherNotFoundException e) {
                    return false;
                }
            case MongoDb:
                try {
                    mongoDb.deleteTeacherById(teacherId);
                    return true;
                } catch (TeacherNotFoundException e) {
                    return false;
                }
            default:
                return false;
        }
    }

    public static List<Teacher> getTeachers(DatabaseType dbType) {
        switch (dbType) {
            case MySQL:
                return mySqlDb.getAllTeachersList();
            case MongoDb:
                return mongoDb.getAllTeachersList();
            default:
                return new ArrayList<>();
        }
    }

    public static Teacher getTeacher(DatabaseType dbType, int teacherId) {
        switch (dbType) {
            case MySQL:
                try {
                    return mySqlDb.getTeacherById(teacherId);
                } catch (TeacherNotFoundException e) {
                    return null;
                }
            case MongoDb:
                try {
                    return mongoDb.getTeacherById(teacherId);
                } catch (TeacherNotFoundException e) {
                    return null;
                }
            default:
                return null;
        }
    }
}
