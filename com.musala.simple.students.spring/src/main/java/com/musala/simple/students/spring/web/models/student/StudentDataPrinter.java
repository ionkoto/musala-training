package com.musala.simple.students.spring.web.models.student;

import java.util.Arrays;
import java.util.List;

/**
 * This is a helper class providing methods for printing (output) data for
 * {@link Student} objects - for single and multiple objects.
 * 
 * @author yoan.petrushinov
 *
 */
public class StudentDataPrinter {
    private static final String STUDENT_PRINT_HEADING = String.format("%-3s %-20s %-5s %-5s", "Id", "Name", "Age", "Grade");
    private static final String STUDENT_GROUP_PRINT_HEADING = "Student group: ";
    
    private StudentDataPrinter() {

    }

    /**
     * Outputs on the console the details of a given {@link Student} objects in a
     * specified format containing their Id, Name, Age and Grade properties.
     *
     * @param student
     *            A {@link Student} object, whose details are being printed.
     */
    public static void printStudentDetails(Student student) {
        StringBuilder outputSb = new StringBuilder();
        outputSb.append(STUDENT_PRINT_HEADING)
                .append(System.lineSeparator())
                .append(student.toString());

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
        outputSb.append(STUDENT_GROUP_PRINT_HEADING)
                .append(System.lineSeparator())
                .append(STUDENT_PRINT_HEADING)
                .append(System.lineSeparator());

        for (Student student : studentsList) {
            outputSb.append(student.toString())
                    .append(System.lineSeparator());
        }

        System.out.println(outputSb.toString());
    }

    /**
     * An overload of the {@link StudentDataPrinter#printStudents(List)}
     *
     * @param studentsArr
     *            An Array containing {@link Student} objects
     */
    public static void printStudents(Student[] studentsArr) {
        printStudents(Arrays.asList(studentsArr));
    }
}
