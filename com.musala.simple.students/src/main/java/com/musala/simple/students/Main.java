package com.musala.simple.students;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.maven.wagon.ResourceDoesNotExistException;

import com.google.gson.Gson;
import com.musala.simple.students.exception.InvalidStudentIdException;
import com.musala.simple.students.helpers.FileHelper;
import com.musala.simple.students.helpers.ValidationHelper;
import com.musala.simple.students.internal.ErrorMessage;
import com.musala.simple.students.student.Student;
import com.musala.simple.students.student.StudentDataPrinter;
import com.musala.simple.students.student.StudentGroup;
import com.musala.simple.students.student.StudentWrapper;

public class Main {
	public static void main(String[] args) {

		if (ValidationHelper.isInputValid(args)) {

			String studentsJsonInfo = FileHelper.readFile(args[0]);

			if (!ValidationHelper.isValidJson(studentsJsonInfo)) {
				System.err.println(ErrorMessage.NOT_VALID_JSON);
				try {
					StudentWrapper backupStudentsData = FileHelper.readBackupJson();
					StudentDataPrinter.printStudents(backupStudentsData.students);
				} catch (ResourceDoesNotExistException e) {
					System.err.println(ErrorMessage.NO_BACKUP_FAIL);
				}
			} else {
				StudentWrapper studentWrapper = new Gson().fromJson(studentsJsonInfo, StudentWrapper.class);
				StudentGroup studentGroup = new StudentGroup();
				studentGroup.fillStudentGroup(studentWrapper.students);

				Map<Integer, Student> studentsMap = studentGroup.getStudents();

				if (args.length >= 2) {
					try {
						Student student = studentGroup.getStudentById(args[1]);
						StudentDataPrinter.printStudentDetails(student);
					} catch (ArrayIndexOutOfBoundsException e) {
						/* Do nothing. Student Id is not a required input parameter. */
					} catch (InvalidStudentIdException ise) {
						System.err.printf("An error occured while trying to retrieve the student with id: %s\n",
								args[1]);
						ise.printStackTrace();
					}
				} else {
					List<Student> studentsList = new ArrayList<Student>(studentsMap.values());
					StudentDataPrinter.printStudents(studentsList);
				}
				FileHelper.updateBackupJson(studentsJsonInfo);
			}
		} else {
			try {
				StudentWrapper backupStudentsData = FileHelper.readBackupJson();
				StudentDataPrinter.printStudents(backupStudentsData.students);
			} catch (ResourceDoesNotExistException e) {
				System.err.println(ErrorMessage.NO_BACKUP_FAIL);
			}
		}
	}
}
