package com.musala.simple.students.spring.web.database;

import java.util.List;

import com.musala.simple.students.spring.web.exception.TeacherNotFoundException;
import com.musala.simple.students.spring.web.models.teacher.Teacher;

/**
 * The interface serves as an middle abstraction layer for
 * database communication. It gives the user the basic
 * functionality when working with teacher entities from a database, providing the
 * opportunity to use different implementation depending on
 * the preferred database type.
 * 
 * @author yoan.petrushinov
 *
 */
public interface DatabaseTeacherCommands {

    /**
     * Adds a teacher to the database.
     *
     * @param A {@link Teacher} object to add
     *            to the database
     */
    public boolean addTeacher(Teacher teacher);

    /**
     * Adds multiple teachers to the database.
     *
     * @param A List of {@link Teacher} objects
     *            to add to the database
     */
    public void addMultipleTeachers(List<Teacher> teachers);

    /**
     * Adds multiple teachers to the database.
     *
     * @param An array of {@link Teacher} objects
     *            to add to the database
     */
    public void addMultipleTeachers(Teacher[] teachers);

    /**
     * Deletes a single teacher from the database by
     * given id.
     *
     * @param An int id to query the student by.
     * @throws TeacherNotFoundException
     */
    public void deleteTeacherById(int teacherId) throws TeacherNotFoundException;

    /**
     * Retrieves a single teacher from the database by
     * given id. The method throws a {@link TeacherNotFoundException}
     * in case a teacher with the given id does not exist
     * in the database.
     *
     * @param An int id to query the teacher by.
     */
    public Teacher getTeacherById(int teacherId) throws TeacherNotFoundException;

    /**
     * Retrieves all teachers from the database as
     * an Array of {@link Teacher} objects.
     *
     * @param An int id to query the teacher by.
     */
    public Teacher[] getAllTeachersArr();

    /**
     * Retrieves all teachers from the database as
     * a List of {@link Teacher} objects.
     *
     * @param An int id to query the teacher by.
     */
    public List<Teacher> getAllTeachersList();
}
