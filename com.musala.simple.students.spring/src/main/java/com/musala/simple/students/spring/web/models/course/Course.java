package com.musala.simple.students.spring.web.models.course;

import java.util.List;

import com.musala.simple.students.spring.web.models.student.Student;
import com.musala.simple.students.spring.web.models.teacher.Teacher;

public class Course {
    private int id;
    private String name;
    private List<Student> studentsEnrolled;
    private List<Teacher> courseTeachers;

    public Course() {

    }

    public Course(int id, String name) {
        this.setId(id);
        this.setName(name);
        this.studentsEnrolled = null;
        this.courseTeachers = null;
    }

    public Course(int id, String name, List<Student> studentsEnrolled, List<Teacher> courseTeachers) {
        this.setId(id);
        this.setName(name);
        this.setStudentsEnrolled(studentsEnrolled);
        this.setCourseTeachers(courseTeachers);
    }

    private void setCourseTeachers(List<Teacher> courseTeachers) {
        this.courseTeachers = courseTeachers;
    }

    private void setStudentsEnrolled(List<Student> studentsEnrolled) {
        this.studentsEnrolled = studentsEnrolled;
    }

    public List<Teacher> getCourseTeachers() {
        return this.courseTeachers;
    }

    public List<Student> getStudentsEnrolled() {
        return this.studentsEnrolled;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%-3d %-20s", this.getId(), this.getName());
    }
}
