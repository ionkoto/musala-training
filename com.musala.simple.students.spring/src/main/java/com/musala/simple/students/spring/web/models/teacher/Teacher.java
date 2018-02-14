package com.musala.simple.students.spring.web.models.teacher;

public class Teacher {
    private int id;
    private String name;
    private String email;

    public Teacher() {

    }

    public Teacher(int id, String name, String email) {
        this.setId(id);
        this.setName(name);
        this.setEmail(email);
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

    public String getEmail() {
        return this.email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("%-3d %-20s %-20s", this.getId(), this.getName(), this.getEmail());
    }
}
