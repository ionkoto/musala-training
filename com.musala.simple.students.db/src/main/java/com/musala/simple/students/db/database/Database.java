package com.musala.simple.students.db.database;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.musala.simple.students.db.exception.StudentNotFoundException;
import com.musala.simple.students.db.student.Student;

public abstract class Database implements DatabaseCommands, DatabaseProperties {
	private String name;
	private String host;
	private int port;
	private String username;
	private String password;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public String getHost() {
		return this.host;
	}

	@Override
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public int getPort() {
		return this.port;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	protected Logger getLogger() {
		return this.logger;
	}
	
	public abstract void establishConnection();
	
	@Override
	public abstract void addStudent(Student student);

	@Override
	public abstract void addMultipleStudents(List<Student> students);

	@Override
	public abstract void addMultipleStudents(Student[] students);

	@Override
	public abstract void deleteStudentById(int studentId) throws StudentNotFoundException;

	@Override
	public abstract Student getStudentById(int sudentId) throws StudentNotFoundException;

	@Override
	public abstract Student[] getAllStudentsArr();

	@Override
	public abstract List<Student> getAllStudentsList();
}
