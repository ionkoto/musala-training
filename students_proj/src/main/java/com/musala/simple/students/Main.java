package com.musala.simple.students;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.maven.wagon.ResourceDoesNotExistException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Main {
	private static final String JSON_FILE_EXTENSION = ".json";
	private static final String ERROR_PATH_MISSING = "Path is missing.";
	private static final String ERROR_INVALID_PATH = "Invalid path specified.";
	private static final String ERROR_NOT_A_FILE = "Òhe path specified is not an existing file.";
	private static final String ERROR_FILE_NOT_JSON = "The file specified is not of '.json' format.";
	private static final String ERROR_NO_BACKUP_JSON = "No backup.json exists.";
	private static final String ERROR_NOT_VALID_JSON = "The content of the JSON file is not a valid JSON string.";
	private static final String ERROR_STUDENT_NOT_FOUND = "Requested student does not exist in the current Student group.";
	private static final String BACKUP_JSON_PATH = "src/main/resources/backup.json";
	private static final String INFO_CREATING_BACKUP = "Creating backup..";

	public static void main(String[] args) {

		if (inputIsValid(args)) {

			String studentsJsonInfo = readFile(args[0]);

			if (!isValidJson(studentsJsonInfo)) {
				System.err.println(ERROR_NOT_VALID_JSON);
				try {
					readBackupJson();
				} catch (ResourceDoesNotExistException e) {
					e.printStackTrace();
				}
			} else {
				StudentWrapper studentWrapper = new Gson().fromJson(studentsJsonInfo, StudentWrapper.class);
				StudentGroup studentGroup = new StudentGroup();
				studentGroup.fillStudentGroup(studentWrapper.students);

				Map<Integer, Student> studentsMap = studentGroup.getStudents();

				int requestedStudentId = readStudentId(args);
				boolean studentExists = false;
				if (requestedStudentId != -1) {
					try {
						Student student = studentGroup.getStudentById(requestedStudentId);
						studentExists = true;
						printStudentDetails(student);
					} catch (IllegalArgumentException iae) {
						System.err.println(ERROR_STUDENT_NOT_FOUND);
					}
				}
				if (!studentExists) {
					List<Student> studentsList = studentsMap.values().stream().collect(Collectors.toList()); 
					printStudents(studentsList);
				}

				updateBackupFile(studentsJsonInfo);
			}
		} else {
			try {
				readBackupJson();
			} catch (ResourceDoesNotExistException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Validates the command line input arguments.
	 *
	 * @param args  A String array with command line arguments
	 * @return true if the first argument in the array
	 * is a valid path to a file with extension ".json".
	 */
	private static boolean inputIsValid(String[] args) {

		/* Check if any arguments are provided */
		if (args.length == 0) {
			System.err.println(ERROR_PATH_MISSING);
			return false;
		}

		String argPath = args[0];
		File file = new File(argPath);

		/* Check if path exists */
		if (!file.exists()) {
			System.err.println(ERROR_INVALID_PATH);

			return false;
		}

		/* Check if the valid path is a file */
		if (!file.isFile()) {
			System.err.println(ERROR_NOT_A_FILE);

			return false;
		}

		/* Check the file extension to be JSON. */
		if (!argPath.endsWith(JSON_FILE_EXTENSION)) {
			System.err.println(ERROR_FILE_NOT_JSON);
			return false;
		}

		return true;
	}

	/**
	 * Creates a backup Json file using the students objects
	 * provided as a Json String parameter. If a backup file
	 * already exists, it's being overwritten.
	 *
	 * @param studentsJsonInfo A Json String, containing the students
	 * to be written to the backup file.
	 */
	private static void createBackupJson(String studentsJsonInfo) {
		try (FileWriter fileWriter = new FileWriter(BACKUP_JSON_PATH, false)) {
			fileWriter.write(studentsJsonInfo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads the backup Json file (if exists) and prints the
	 * students information from it.
	 *
	 */
	private static void readBackupJson() throws ResourceDoesNotExistException {
		File backupFile = new File(BACKUP_JSON_PATH);

		if (!backupFile.exists()) {
			throw new ResourceDoesNotExistException(ERROR_NO_BACKUP_JSON);
		}
		String backupStudentsJson = readFile(BACKUP_JSON_PATH);

		StudentWrapper studentWrapper = new Gson().fromJson(backupStudentsJson, StudentWrapper.class);
		printStudents(Arrays.asList(studentWrapper.students));
	}

	/**
	 * Updates the backup file by adding the new students. If the backup
	 * file does not exist the {@link Main#createBackupJson(String)} is called
	 * and the backup is created with the new students.
	 *
	 * @param studentsJsonInfo A Json String, containing the students
	 * to be added to the backup file.
	 */
	public static void updateBackupFile(String studentsJsonInfo) {
		File backupFile = new File(BACKUP_JSON_PATH);

		if (!backupFile.exists()) {
			System.out.println(INFO_CREATING_BACKUP);
			createBackupJson(studentsJsonInfo);
		} else {
			String backupFileInfo = readFile(BACKUP_JSON_PATH);
			Student[] backupStudents = new Gson().fromJson(backupFileInfo, StudentWrapper.class).students;
			Student[] newStudents = new Gson().fromJson(studentsJsonInfo, StudentWrapper.class).students;
			
			StudentGroup sGroup = new StudentGroup();
			sGroup.fillStudentGroup(backupStudents);
			sGroup.fillStudentGroup(newStudents);
		
			StudentWrapper allStudentsWrapper = new StudentWrapper();
			int sGroupSize = sGroup.getStudents().values().size();
			allStudentsWrapper.students = sGroup.getStudents().values().toArray(new Student[sGroupSize]);
			createBackupJson(new Gson().toJson(allStudentsWrapper));
		}
	}

	/**
	 * Tries to parse a String using the
	 * {@link Gson#fromJson(com.google.gson.JsonElement, Class) method, which throws
	 * an Exception if the parameter String is not a valid Json.
	 *
	 * @param Json A String that the user wants to validate as JSON
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
	 * TryParses the second argument of the user input (command line). If the
	 * argument is a valid integer the method returns it. Else the method notifies
	 * the user that the argument is not a number and returns -1 (Invalid user id).
	 * If the args array does not contain a second element the method also returns
	 * -1 (Invalid user id).
	 *
	 * @param args the command line arguments provided by the user
	 * @return an int value specifying student's id
	 */
	private static int readStudentId(String[] args) {
		int studentId = -1;
		if (args.length >= 2) {
			try {
				studentId = Integer.parseInt(args[1]);
			} catch (NumberFormatException nfe) {
				System.err.println("The second argumnet must be a number!");
			}
		}
		return studentId;
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
	private static String readFile(String path) {
		try {
			return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Outputs on the console the details of a given {@link Student} objects in a
	 * specified format containing their Id, Name, Age and Grade properties.
	 *
	 * @param student
	 *            A {@link Student} object, whose details are being printed.
	 */
	private static void printStudentDetails(Student student) {
		StringBuilder outputSb = new StringBuilder();
		outputSb.append(String.format("%-3s %-20s %-5s %-5s", "Id", "Name", "Age", "Grade"))
				.append(System.lineSeparator()).append(String.format("%-3d %-20s %-5d %-5d", student.getId(),
						student.getName(), student.getAge(), student.getGrade()));

		System.out.println(outputSb.toString());
	}

	/**
	 * Outputs on the console a list of {@link Student} objects in a specified
	 * format containing their Id, Name, Age and Grade properties.
	 *
	 * @param students
	 *            A List containing {@link Student} objects
	 */
	private static void printStudents(List<Student> students) {
		StringBuilder outputSb = new StringBuilder();
		outputSb.append("Student group: ").append(System.lineSeparator()).append(System.lineSeparator())
				.append(String.format("%-3s %-20s %-5s %-5s", "Id", "Name", "Age", "Grade"))
				.append(System.lineSeparator());

		for (Student student : students) {
			outputSb.append(String.format("%-3d %-20s %-5d %-5d", student.getId(), student.getName(), student.getAge(),
					student.getGrade()));
			outputSb.append(System.lineSeparator());
		}

		System.out.println(outputSb.toString());
	}
}
