package com.musala.simple.students;

/**
 * The class represents a blueprint for storing data about a student - id, name,
 * age and grade.
 * 
 * @author yoan.petrushinov
 *
 */
public class Student {
	private int id;
	private String name;
	private int age;
	private int grade;

	public Student(int id, String name, int age, int grade) {
		this.setId(id);
		this.setName(name);
		this.setAge(age);
		this.setGrade(grade);
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

	public int getAge() {
		return age;
	}

	private void setAge(int age) {
		this.age = age;
	}

	public int getGrade() {
		return grade;
	}

	private void setGrade(int grade) {
		this.grade = grade;
	}
}
