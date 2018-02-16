package com.musala.simple.students.spring.web.database;

import java.util.List;

import com.musala.simple.students.spring.web.exception.CourseNotFoundException;
import com.musala.simple.students.spring.web.models.course.Course;
import com.musala.simple.students.spring.web.models.student.Student;
import com.musala.simple.students.spring.web.models.teacher.Teacher;

/**
 * The interface serves as an middle abstraction layer for
 * database communication. It gives the user the basic
 * functionality when working with course entities from a database, providing the
 * opportunity to use different implementation depending on
 * the preferred database type.
 * 
 * @author yoan.petrushinov
 *
 */
public interface DatabaseCourseCommands {

    /**
     * Adds a course to the database.
     *
     * @param A {@link Course} object to add
     *            to the database
     */
    public boolean addCourse(Course course);

    /**
     * Adds multiple courses to the database.
     *
     * @param A List of {@link Course} objects
     *            to add to the database
     */
    public void addMultipleCourses(List<Course> courses);

    /**
     * Adds multiple courses to the database.
     *
     * @param An array of {@link Course} objects
     *            to add to the database
     */
    public void addMultipleCourses(Course[] courses);

    /**
     * Deletes a single course from the database by
     * given id.
     *
     * @param An int id to query the student by.
     * @throws CourseNotFoundException
     */
    public void deleteCourseById(int courseId) throws CourseNotFoundException;

    /**
     * Retrieves a single course from the database by
     * given id. The method throws a {@link CourseNotFoundException}
     * in case a course with the given id does not exist
     * in the database.
     *
     * @param An int id to query the course by.
     */
    public Course getCourseById(int courseId) throws CourseNotFoundException;

    /**
     * Retrieves all courses from the database as
     * an Array of {@link Course} objects.
     *
     * @param An int id to query the course by.
     */
    public Course[] getAllCoursesArr();

    /**
     * Retrieves all courses from the database as
     * a List of {@link Course} objects.
     *
     * @param An int id to query the course by.
     */
    public List<Course> getAllCoursesList();
    
    public Teacher[] getCourseTeachersArr(int courseId);
    
    public List<Teacher> getCourseTeachersList(int courseId);
    
    public Student[] getCourseStudentsArr(int courseId);
    
    public List<Student> getCourseStudentsList(int courseId);
}
