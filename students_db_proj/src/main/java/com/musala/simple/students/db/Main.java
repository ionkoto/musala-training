package com.musala.simple.students.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.musala.simple.students.db.exception.StudentNotFoundException;
import com.musala.simple.students.db.helpers.FileHelper;
import com.musala.simple.students.db.helpers.ValidationHelper;
import com.musala.simple.students.db.internal.ErrorMessage;
import com.musala.simple.students.db.student.Student;
import com.musala.simple.students.db.student.StudentDataPrinter;
import com.musala.simple.students.db.student.StudentGroup;
import com.musala.simple.students.db.student.StudentWrapper;

public class Main {
	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(Main.class);
		BasicConfigurator.configure();

		// Pass the path to the config file of the preferred Database
		Properties dbProperties = FileHelper.readDbPropertiesFile(DatabaseTypes.MongoDb);
		Database database = new DatabaseFactory(dbProperties, DatabaseTypes.MongoDb).getDatabase();

		if (ValidationHelper.isInputValid(args)) {

			String studentsJsonInfo = FileHelper.readFile(args[0]);

			if (!ValidationHelper.isValidJson(studentsJsonInfo)) {
				logger.error(ErrorMessage.NOT_VALID_JSON);
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
						logger.debug(ErrorMessage.STUDENT_NOT_FOUND);
						try {
							Student student = database.getStudentById(studentId);
							StudentDataPrinter.printStudentDetails(student);
						} catch (StudentNotFoundException e) {
							logger.error(String.format(
									"An error occured while trying to retrieve the student with id: %d\n", studentId));
							logger.error(ErrorMessage.STUDENT_NOT_EXISTS, e);
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
