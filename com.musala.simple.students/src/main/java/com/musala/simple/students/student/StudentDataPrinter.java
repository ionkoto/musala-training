package com.musala.simple.students.student;

import java.util.Arrays;
import java.util.List;

/**
 * This is a helper class providing methods
 * for printing (output) data for {@link Student} 
 * objects - for single and multiple objects.
 * 
 * @author yoan.petrushinov
 *
 */
public class StudentDataPrinter {
	/**
	 * Outputs on the console the details of a given {@link Student} objects in a
	 * specified format containing their Id, Name, Age and Grade properties.
	 *
	 * @param student
	 *            A {@link Student} object, whose details are being printed.
	 */
	public static void printStudentDetails(Student student) {
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
	public static void printStudents(List<Student> studentsList) {
		StringBuilder outputSb = new StringBuilder();
		outputSb.append("Student group: ").append(System.lineSeparator()).append(System.lineSeparator())
				.append(String.format("%-3s %-20s %-5s %-5s", "Id", "Name", "Age", "Grade"))
				.append(System.lineSeparator());

		for (Student student : studentsList) {
			outputSb.append(String.format("%-3d %-20s %-5d %-5d", student.getId(), student.getName(), student.getAge(),
					student.getGrade()));
			outputSb.append(System.lineSeparator());
		}

		System.out.println(outputSb.toString());
	}
	
	/**
	 * An overload of the {@link StudentDataPrinter#printStudents(List)}
	 *
	 * @param studentsArr An Array containing {@link Student} objects
	 */
	public static void printStudents(Student[] studentsArr) {
		printStudents(Arrays.asList(studentsArr));
	}
}
