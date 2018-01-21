package com.musala.simple.students.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.musala.simple.students.db.database.AbstractDatabase;
import com.musala.simple.students.db.database.DatabaseFactory;
import com.musala.simple.students.db.database.DatabaseType;
import com.musala.simple.students.db.exception.StudentNotFoundException;
import com.musala.simple.students.db.helper.FileHelper;
import com.musala.simple.students.db.helper.ValidationHelper;
import com.musala.simple.students.db.internal.ErrorMessage;
import com.musala.simple.students.db.student.Student;
import com.musala.simple.students.db.student.StudentDataPrinter;
import com.musala.simple.students.db.student.StudentGroup;
import com.musala.simple.students.db.student.StudentWrapper;

public class Main {
	private static final String DEFAULT_DB_NAME = "studentsDb";
	private static final String DEFAULT_DB_PORT = "27017";
	private static final String DEFAULT_DB_HOST = "localhost";
	private static final String DEFAULT_DB_USERNAME = "admin";
	private static final String DEFAULT_DB_PASSWORD = "admin";
	private static final String DB_NAME = "name";
	private static final String DB_PORT = "port";
	private static final String DB_HOST = "host";
	private static final String DB_USER = "username";
	private static final String DB_PASSWORD = "password";
	 
	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(Main.class);
		
		/* Sets up a default configuration for the log4j logger. This is suitable for printing 
		 * out logging statements to the standard console. For writing logs to a file you need
		 * to create a configuration file with more specific settings. Please refer to:
		 * http://www.codejava.net/coding/how-to-configure-log4j-as-logging-mechanism-in-java */
		BasicConfigurator.configure();

		Properties dbProperties = FileHelper.readDbPropertiesFile(DatabaseType.MongoDb);
		AbstractDatabase database = DatabaseFactory.createDatabase(DatabaseType.MongoDb)
				.withName(dbProperties.getProperty(DB_NAME, DEFAULT_DB_NAME))
				.withHost(dbProperties.getProperty(DB_HOST, DEFAULT_DB_HOST))
				.withPort(dbProperties.getProperty(DB_PORT, DEFAULT_DB_PORT))
				.withUsername(dbProperties.getProperty(DB_USER, DEFAULT_DB_USERNAME))
				.withPassword(dbProperties.getProperty(DB_PASSWORD, DEFAULT_DB_PASSWORD))
				.build();
		database.establishConnection();

		if (ValidationHelper.isInputValid(args)) {

			String studentsJsonInfo = FileHelper.readFile(args[0]);

			if (!ValidationHelper.isValidJson(studentsJsonInfo)) {
				logger.warn(ErrorMessage.NOT_VALID_JSON);
				Student[] students = database.getAllStudentsArr();
				StudentDataPrinter.printStudents(students);
			} else {
				StudentWrapper studentWrapper = new Gson().fromJson(studentsJsonInfo, StudentWrapper.class);
				StudentGroup studentGroup = new StudentGroup();
				studentGroup.fillStudentGroup(studentWrapper.students);
				Map<Integer, Student> studentsMap = studentGroup.getStudents();
				List<Student> studentsList = new ArrayList<Student>(studentsMap.values());
				database.addMultipleStudents(studentsList);
				
				if (ValidationHelper.validRequest(args)) {
					int studentId = Integer.parseInt(args[1]);
					try {
						// Look for the student in the Student Group
						Student student = studentGroup.getStudentById(studentId);
						StudentDataPrinter.printStudentDetails(student);
					} catch (StudentNotFoundException snfe) {
						logger.warn(ErrorMessage.STUDENT_NOT_FOUND);
						try {
							Student student = database.getStudentById(studentId);
							StudentDataPrinter.printStudentDetails(student);
						} catch (StudentNotFoundException e) {
							logger.warn(String.format(
									"Could not retrieve student with id: %d\n", studentId));
							logger.warn(ErrorMessage.STUDENT_NOT_EXISTS);
							StudentDataPrinter.printStudents(studentsList);
						}
					}
				} else {
					StudentDataPrinter.printStudents(studentsList);
				}
			}
		} else {
			Student[] students = database.getAllStudentsArr();
			if (students.length == 0) {
				logger.info(ErrorMessage.DATABASE_EMPTY);
			} else {
				StudentDataPrinter.printStudents(students);
			}
		}
	}
}
