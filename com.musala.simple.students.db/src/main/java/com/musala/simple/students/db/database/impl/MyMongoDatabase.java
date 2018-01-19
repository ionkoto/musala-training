package com.musala.simple.students.db.database.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.musala.simple.students.db.database.Database;
import com.musala.simple.students.db.database.DatabaseCommands;
import com.musala.simple.students.db.database.DatabaseProperties;
import com.musala.simple.students.db.exception.StudentNotFoundException;
import com.musala.simple.students.db.internal.ErrorMessage;
import com.musala.simple.students.db.internal.InfoMessage;
import com.musala.simple.students.db.student.Student;

/**
 * MongoDb implementation of the {@link DatabaseCommands} interface.
 * 
 * @author yoan.petrushinov
 *
 */
public class MyMongoDatabase extends Database implements DatabaseCommands, DatabaseProperties {
	private MongoClient mongo;
	private MongoDatabase database;
	private MongoCollection<Document> studentsCollection;

	@Override
	public void establishConnection() {
		this.setMongoClient();
		this.setDatabase();
		this.setStudentsCollection();
		getLogger().info(InfoMessage.DATABASE_CONNECTION_SUCCESS);
	}
	
	/**
	 * 
	 * @param host
	 *            the hostname for establishing database connection port the port
	 *            number for the database connection
	 */
	private void setMongoClient() {
		this.mongo = new MongoClient(this.getHost(), this.getPort());
	}

	private MongoClient getMongoClient() {
		return this.mongo;
	}

	/**
	 * @return the {@link MongoDatabase} property
	 */
	private MongoDatabase getDatabase() {
		return this.database;
	}

	/**
	 * sets the {@link MyMongoDatabase#database} parameter
	 */
	private void setDatabase() {
		this.database = this.getMongoClient().getDatabase(this.getName());
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
	 * A MongoDb-specific implementation of the {@link DatabaseCommands#addStudent(Student)}
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
				getLogger().error(String.format("An error occured while trying to add student with name %s to the database.",
						student.getName()));
				getLogger().info("Stacktrace:", e);
			}
		}
		try {
			this.studentsCollection.insertOne(newStudent);
		} catch (MongoWriteException e) {
			if (e.getMessage().startsWith("E11000")) {
				getLogger().error(String.format(
						"Student %s with id %d can not be added to the DatabaseCommands. Student with the same id already exists\n",
						student.getName(), student.getId()));
			} else {
				getLogger().error(String.format("Problem occured while trying to add %s with id %d to the database.\n",
						student.getName(), student.getId()));
				getLogger().error("Stacktrace:", e);
			}
		}
	}

	@Override
	public void deleteStudentById(int studentId) throws StudentNotFoundException {
		Document query = new Document("id", studentId);
		query = this.studentsCollection.find(query).first();

		if (query == null) {
			throw new StudentNotFoundException(ErrorMessage.STUDENT_NOT_EXISTS);
		}

		this.studentsCollection.deleteOne(query);
		getLogger().info(String.format(InfoMessage.STUDENT_DELETE_SUCCESS, studentId));
	}

	/**
	 * A MongoDb-specific implementation of the {@link DatabaseCommands#getStudentById()}
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

		return constructStudentObject(query);
	}

	/**
	 * Creates a list of the Document's values and constructs
	 * a student object with the extracted from the document
	 * properties.
	 * @param query The Document containing the Student's information in a
	 * 		  database format.
	 * @return the new Student object
	 */
	private Student constructStudentObject(Document query) {
		List list = new ArrayList(query.values());
		int id = (int) list.get(1);
		String name = Objects.toString(list.get(2));
		int age = (int) list.get(3);
		int grade = (int) list.get(4);

		return new Student(id, name, age, grade);
	}

	/**
	 * A MongoDb-specific implementation of the {@link DatabaseCommands#getAllStudentsArr()}
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
				Student student = constructStudentObject(doc);
				studentsList.add(student);
			}
		}
		getLogger().info(InfoMessage.STUDENT_GET_ALL_SUCCESS);
		return studentsList.toArray(new Student[studentsList.size()]);
	}

	/**
	 * A MongoDb-specific implementation of the
	 * {@link DatabaseCommands#getAllStudentsList()} method. Calls the
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
	 * {@link DatabaseCommands#addMultipleStudents(List<Student> students)} method. Iterates
	 * over the List<Student> parameter and calls the
	 * {@link DatabaseCommands#addStudent(Student)} method for each Student object.
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
	 * {@link DatabaseCommands#addMultipleStudents(Student[] students)} method. Iterates
	 * over the Student[] parameter and calls the
	 * {@link DatabaseCommands#addStudent(Student)} method for each Student object.
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
