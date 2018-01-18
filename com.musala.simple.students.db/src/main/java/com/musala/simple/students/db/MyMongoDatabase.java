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

/**
 * MongoDb implementation of the {@link Database} interface.
 * 
 * @author yoan.petrushinov
 *
 */
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

	/**
	 * 
	 * @param host
	 *            the hostname for establishing databse connection port the port
	 *            number for the db connection
	 */
	private void setMongoClient(String host, int port) {
		this.mongo = new MongoClient(host, port);
		logger.info(InfoMessage.DATABASE_CONNECTION_SUCCESS);
	}

	private MongoClient getMongoClient() {
		return this.mongo;
	}

	/**
	 * @return dbName the name of the current database being initialized
	 */
	private String getDbName() {
		return dbName;
	}

	/**
	 * 
	 * @param dbName
	 *            name of the database being initialized
	 */
	private void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * @return the {@link MongoDatabase} property
	 */
	private MongoDatabase getDatabase() {
		return database;
	}

	/**
	 * sets the {@link MyMongoDatabase#database} parameter
	 */
	private void setDatabase() {
		this.database = this.getMongoClient().getDatabase(this.getDbName());
	}

	/**
	 * initializes a collection of student {@link Document}s and sets the id
	 * property as a unique property. This prevents from creating multiple documents
	 * in the collection with the same student id. Notice that the unique index is
	 * set for the id property of the student object and not the auto-generated
	 * "_id" property of the {@link Document}. If the collection does not exist it's
	 * being automatically created.
	 */
	private void setStudentsCollection() {
		Document index = new Document("id", 1);
		this.studentsCollection = this.getDatabase().getCollection("students");
		this.studentsCollection.createIndex(index, new IndexOptions().unique(true));
	}

	/**
	 * A MongoDb-specific implementation of the {@link Database#addStudent(Student)}
	 * method. Extracts the properties of the {@link Student} object as an array of
	 * Fields and then initializes a {@link Document} object to be added to the
	 * collection (the MongoDb collection only accepts {@link Document} objects. The
	 * method then iterates over the Fields array and assigns the Student's
	 * properties to the Document object. The document object is then inserted to
	 * the collection using a try-catch block in case a document containing the same
	 * student id is already in the database. The
	 * {@link MyMongoDatabase#setStudentsCollection} method specifies that the id
	 * property must be unique.
	 * 
	 */
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

	/**
	 * A MongoDb-specific implementation of the {@link Database#getStudentById()}
	 * method. Searches the database for a document with an id property value equal
	 * to the studentId param. If not found the method throws an exception. Else it
	 * creates a student object with the required properties and returns it.
	 * 
	 * @param studentId
	 *            the id to find a student in the database
	 * @return a Student object with id, name, age and grade
	 */
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

	/**
	 * A MongoDb-specific implementation of the {@link Database#getAllStudentsArr()}
	 * method. Retrieves the collection of {@link Document}s from the database, adds
	 * the values of each document to a list and then assigns each value to the
	 * corresponding property of a newly created Student objects. It then adds that
	 * object to a List. After adding all the students to the list the method
	 * returns an array from that list.
	 * 
	 * @return a Student[] array
	 */
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

	/**
	 * A MongoDb-specific implementation of the
	 * {@link Database#getAllStudentsList()} method. Calls the
	 * {@link MyMongoDatabase#getAllStudentsArr()} and casts the returned array to a
	 * List<Student> and returns it.
	 * 
	 * @return a List<Student> all students in a List
	 */
	@Override
	public List<Student> getAllStudentsList() {
		return Arrays.asList(this.getAllStudentsArr());
	}

	/**
	 * A MongoDb-specific implementation of the
	 * {@link Database#addMultipleStudents(List<Student> students)} method. Iterates
	 * over the List<Student> parameter and calls the
	 * {@link Database#addStudent(Student)} method for each Student object.
	 * 
	 * @param students
	 *            a List containing multiple {@link Student} objects to be added to
	 *            the database
	 */
	@Override
	public void addMultipleStudents(List<Student> students) {
		for (Student student : students) {
			this.addStudent(student);
		}
	}

	/**
	 * A MongoDb-specific implementation of the
	 * {@link Database#addMultipleStudents(Student[] students)} method. Iterates
	 * over the Student[] parameter and calls the
	 * {@link Database#addStudent(Student)} method for each Student object.
	 * 
	 * @param students
	 *            an Array containing multiple {@link Student} objects to be added
	 *            to the database
	 */
	@Override
	public void addMultipleStudents(Student[] students) {
		List<Student> studentsList = Arrays.asList(students);
		this.addMultipleStudents(studentsList);
	}
}
