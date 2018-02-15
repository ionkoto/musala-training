package com.musala.simple.students.spring.web.models.course;

public class Course {
    private int id;
    private String name;

    public Course() {

    }

    public Course(int id, String name) {
        this.setId(id);
        this.setName(name);
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
