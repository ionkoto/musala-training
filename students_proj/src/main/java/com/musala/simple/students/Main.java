package com.musala.simple.students;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.maven.wagon.ResourceDoesNotExistException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.musala.simple.students.exception.InvalidStudentIdException;
import com.musala.simple.students.internal.ErrorMessage;

public class Main {
	private static final String JSON_FILE_EXTENSION = ".json";

	public static void main(String[] args) {

		if (inputIsValid(args)) {

			String studentsJsonInfo = readFile(args[0]);

			if (!isValidJson(studentsJsonInfo)) {
				System.err.println(ErrorMessage.NOT_VALID_JSON);
				try {
					StudentWrapper backupStudentsData = BackupJsonHelper.readBackupJson();
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

				BackupJsonHelper.updateBackupJson(studentsJsonInfo);
			}
		} else {
			try {
				StudentWrapper backupStudentsData = BackupJsonHelper.readBackupJson();
				StudentDataPrinter.printStudents(backupStudentsData.students);
			} catch (ResourceDoesNotExistException e) {
				System.err.println(ErrorMessage.NO_BACKUP_FAIL);
			}
		}
	}

	/**
	 * Validates the command line input arguments.
	 *
	 * @param args
	 *            A String array with command line arguments
	 * @return true if the first argument in the array is a valid path to a file
	 *         with extension ".json".
	 */
	private static boolean inputIsValid(String[] args) {

		/* Check if any arguments are provided */
		if (args.length == 0) {
			System.err.println(ErrorMessage.PATH_MISSING);
			return false;
		}

		String argPath = args[0];
		File file = new File(argPath);

		/* Check if path exists */
		if (!file.exists()) {
			System.err.println(ErrorMessage.INVALID_PATH);

			return false;
		}

		/* Check if the valid path is a file */
		if (!file.isFile()) {
			System.err.println(ErrorMessage.NOT_A_FILE);

			return false;
		}

		/* Check the file extension to be JSON. */
		if (!argPath.endsWith(JSON_FILE_EXTENSION)) {
			System.err.println(ErrorMessage.FILE_NOT_JSON);
			return false;
		}

		return true;
	}

	/**
	 * Tries to parse a String using the
	 * {@link Gson#fromJson(com.google.gson.JsonElement, Class) method, which throws
	 * an Exception if the parameter String is not a valid Json.
	 *
	 * @param Json
	 *            A String that the user wants to validate as JSON
	 * @return validation of weather the given String is a valid JSON
	 */
	private static boolean isValidJson(String Json) {
		if (Json.equals(null)) {
			return false;
		}
		try {
			new Gson().fromJson(Json, Object.class);
			return true;
		} catch (JsonSyntaxException ex) {
			return false;
		}
	}

	/**
	 * Reads a file by a given path and returns the file's content as a String
	 * object. Uses the {@link Files#readAllBytes(java.nio.file.Path)} method, which
	 * throws an {@link IOException}. The method handles that exception.
	 *
	 * @param path
	 *            a valid path to a file's location
	 * @return the content of the file that has been read
	 */
	static String readFile(String path) {
		try {
			return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.printf("Caught an exception while reading file from path %s:\n", path);
			e.printStackTrace();
		}
		return null;
	}
}
