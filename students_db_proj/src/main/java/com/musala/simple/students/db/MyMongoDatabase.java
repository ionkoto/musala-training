package com.musala.simple.students.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;

import java.lang.reflect.Field;

import com.musala.simple.students.db.exception.StudentNotFoundException;
import com.musala.simple.students.db.internal.ErrorMessage;
import com.musala.simple.students.db.internal.InfoMessage;
import com.musala.simple.students.db.student.Student;

public class MyMongoDatabase implements Database {
	private MongoClient mongo;
	private MongoDatabase database;
	private MongoCollection<Document> studentsCollection;
	private String dbName;
	private Logger logger = LoggerFactory.getLogger(Main.class);

	public MyMongoDatabase(String dbName, String host, int port) {
		this.setDbName(dbName);
		this.setMongoClient(host, port);

		// Accessing the database
		this.setDatabase();
		this.setStudentsCollection();
	}

	private void setMongoClient(String host, int port) {
		this.mongo = new MongoClient(host, port);
		logger.info(InfoMessage.DATABASE_CONNECTION_SUCCESS);
	}

	private MongoClient getMongoClient() {
		return this.mongo;
	}

	private String getDbName() {
		return dbName;
	}

	private void setDbName(String dbName) {
		this.dbName = dbName;
	}

	private MongoDatabase getDatabase() {
		return database;
	}

	private void setDatabase() {
		this.database = this.getMongoClient().getDatabase(this.getDbName());
	}

	private void setStudentsCollection() {
		Document index = new Document("id", 1);
		this.studentsCollection = this.getDatabase().getCollection("students");
		this.studentsCollection.createIndex(index, new IndexOptions().unique(true));
	}

	@Override
	public void addStudent(Student student) {
		Field[] fields = Student.class.getDeclaredFields();
		Document newStudent = new Document();
		for (Field field : fields) {
			String fieldName = field.getName();
			field.setAccessible(true);
			try {
				newStudent.append(fieldName, field.get(student));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error(String.format("An error occured while trying to add student with name %s to the database.",
						student.getName()));
				logger.info("Stacktrace:", e);
			}
		}
		try {
			this.studentsCollection.insertOne(newStudent);
		} catch (MongoWriteException e) {
			if (e.getMessage().startsWith("E11000")) {
				logger.error(String.format(
						"Student %s with id %d can not be added to the Database. Student with the same id already exists\n",
						student.getName(), student.getId()));
			} else {
				logger.error(String.format("Problem occured while trying to add %s with id %d to the database.\n",
						student.getName(), student.getId()));
				logger.error("Stacktrace:", e);
			}
		}
	}

	@Override
	public void deleteStudentById(int studentId) {

	}

	@Override
	public Student getStudentById(int studentId) throws StudentNotFoundException {
		Document query = new Document("id", studentId);
		query = this.studentsCollection.find(query).first();

		if (query == null) {
			throw new StudentNotFoundException(ErrorMessage.STUDENT_NOT_EXISTS);
		}

		List list = new ArrayList(query.values());
		int id = (int) list.get(1);
		String name = (String) list.get(2);
		int age = (int) list.get(3);
		int grade = (int) list.get(4);

		return new Student(id, name, age, grade);
	}

	@Override
	public Student[] getAllStudentsArr() {
		List<Student> studentsList = new ArrayList<>();
		try (MongoCursor<Document> cur = this.studentsCollection.find().iterator()) {
			while (cur.hasNext()) {

				Document doc = cur.next();

				List list = new ArrayList(doc.values());
				int studentId = (int) list.get(1);
				String studentName = (String) list.get(2);
				int studentAge = (int) list.get(3);
				int studentGrade = (int) list.get(4);

				Student student = new Student(studentId, studentName, studentAge, studentGrade);
				studentsList.add(student);
			}
		}
		return studentsList.toArray(new Student[studentsList.size()]);
	}

	@Override
	public List<Student> getAllStudentsList() {
		return Arrays.asList(this.getAllStudentsArr());
	}

	@Override
	public void addMultipleStudents(List<Student> students) {
		for (Student student : students) {
			this.addStudent(student);
		}
	}

	@Override
	public void addMultipleStudents(Student[] students) {
		List<Student> studentsList = Arrays.asList(students);
		this.addMultipleStudents(studentsList);
	}
}
