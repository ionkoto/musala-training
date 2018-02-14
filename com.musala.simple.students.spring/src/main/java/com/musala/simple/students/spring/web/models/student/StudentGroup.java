package com.musala.simple.students.spring.web.models.student;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.musala.simple.students.spring.web.exception.StudentNotFoundException;
import com.musala.simple.students.spring.web.internal.ErrorMessage;

/**
 * The class {@code StudentGroup} groups together a certain amount of
 * {@link Student} objects and provides the user with methods for filling a
 * group with students, getting a List of all students from a group and getting
 * a specific student by id. The {@link Student} objects are stored in a Map. It
 * allows the user to add entries in a way, that each student's Id matches the
 * object's index in the Map.
 * 
 * @author yoan.petrushinov
 *
 */
public class StudentGroup {
    private Map<Integer, Student> students;

    public StudentGroup() {
        this.students = new HashMap<Integer, Student>();
    }

    /**
     * Populates the {@link StudentGroup}'s students property with {@link Student}
     * objects. An attempt to add a student with existing ID returns a message to
     * the user and the operation is omitted.
     *
     * @param studentsArr
     *            Array of Student objects
     */
    public void fillStudentGroup(Student[] studentsArr) {
        for (Student student : studentsArr) {
            if (this.students.containsKey(student.getId())) {
                System.err.printf(
                        "The student %s with id %d can not be added. A student with the same Id already exists.\n",
                        student.getName(), student.getId());
            } else {
                this.students.put(student.getId(), student);
            }
        }
    }

    /**
     * Casts the {@link StudentGroup#students} field to an unmodifiable Map of
     * Student objects and returns it to the user.
     *
     * @return An unmodifiable List of Student objects
     */
    public Map<Integer, Student> getStudents() {
        return Collections.unmodifiableMap(this.students);
    }

    /**
     * Validates the id provided as a parameter and returns the requested Student
     * object. If the Students map does not contain the provided id as a key the
     * method throws an Exception.
     *
     * @param id
     *            Id of the user being requested
     * @return student A Student object from the {@link StudentGroup#students}
     *         corresponding to the provided id
     * @throws StudentNotFoundException
     */
    public Student getStudentById(int id) throws StudentNotFoundException {
        if (!this.students.containsKey(id)) {
            throw new StudentNotFoundException(ErrorMessage.STUDENT_NOT_EXISTS);
        }

        return this.students.get(id);
    }
}
