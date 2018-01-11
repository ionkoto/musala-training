package com.musala.simple.students;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Main {
	private static final String JSON_FILE_EXTENSION = ".json";
	
	public static void main(String[] args) {

		/* Check if any arguments are provided */
		if (args.length == 0) {
			throw new IllegalArgumentException("Path is missing.");
		}
		
		String path = args[0];
    	File file = new File(path);

    	/* Check if path exists */
    	if (!file.exists()) {
    		throw new IllegalArgumentException("Invalid path specified.");
    	}
    		
    	/* Check if the valid path is a file */
	    if (!file.isFile()) {
	    	throw new IllegalArgumentException("The path specified is not an existing file.");
	    }	
	    
        /* Check the file extension to be JSON. */
        if (!path.endsWith(JSON_FILE_EXTENSION)) {
        	throw new IllegalArgumentException("The file specified is not of '.json' format.");
        } 
      
        String fileContent = readFile(path);
        int requestedStudentId = readStudentId(args);
        
        /* validate JSON */
        if (!isValidJson(fileContent)) {
        	throw new IllegalArgumentException("The file given does not contain a valid JSON");
        }
        
        StudentWrapper studentWrapper = new Gson().fromJson(fileContent, StudentWrapper.class);
    	StudentGroup studentGroup = new StudentGroup();
    	studentGroup.fillStudentGroup(studentWrapper.students);

    	List<Student> students = studentGroup.getStudents();
    	
    	boolean studentExists = false;
    	if (requestedStudentId != -1) {
    		try {
    			Student student = studentGroup.getStudentById(requestedStudentId);
    			studentExists = true;
    			printStudentDetails(student);
    		} catch (IllegalArgumentException iae) {
    			System.err.println("Requested student does not exist in the data.");
    		} 
    	} 
    	if (!studentExists) {
    		printStudents(students);
    	}
	}
	
	/**
	 * Tries to parse a String using the {@link Gson#fromJson(com.google.gson.JsonElement, Class)
	 * method, which throws an Exception if the parameter String is
	 * not a valid Json.
	 *
	 * @param  Json  A String that the user wants to validate as JSON
	 * @return validation of weather the given String is a valid JSON
	 */
	private static boolean isValidJson(String Json) {
        try {
        	new Gson().fromJson(Json, Object.class);
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    }
	
	/**
	 * TryParses the second argument of the user input (command line).
	 * If the argument is a valid integer the method returns it. Else
	 * the method notifies the user that the argument is not a number
	 * and returns -1 (Invalid user id). If the args array does
	 * not contain a second element the method also returns -1 (Invalid
	 * user id).
	 *
	 * @param  args  the command line arguments provided by the user
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
	 * Reads a file by a given path and returns the file's content as 
	 * a String object. Uses the {@link Files#readAllBytes(java.nio.file.Path)}
	 * method, which throws an {@link IOException}. The method handles that exception.
	 *
	 * @param  path  a valid path to a file's location
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
	 * @param  student A {@link Student} object, whose details are being printed.
	 */
	private static void printStudentDetails(Student student) {
		StringBuilder outputSb = new StringBuilder();
		outputSb.append(String.format("%-3s %-20s %-5s %-5s", "Id", "Name", "Age", "Grade"))
				.append(System.lineSeparator())
				.append(String.format("%-3d %-20s %-5d %-5d", 
						student.getId(), 
						student.getName(), 
						student.getAge(), 
						student.getGrade()));
		
		System.out.println(outputSb.toString());
	}
	
	/**
	 * Outputs on the console a list of {@link Student} objects in a 
	 * specified format containing their Id, Name, Age and Grade properties.
	 *
	 * @param  students A List containing {@link Student} objects
	 */
	private static void printStudents(List<Student> students) {
		StringBuilder outputSb = new StringBuilder();
		outputSb.append("Student group: ")
				.append(System.lineSeparator())
				.append(System.lineSeparator())
				.append(String.format("%-3s %-20s %-5s %-5s", "Id", "Name", "Age", "Grade"))
				.append(System.lineSeparator());
		
		for (Student student : students) {
			outputSb.append(String.format("%-3d %-20s %-5d %-5d", 
					student.getId(), 
					student.getName(), 
					student.getAge(), 
					student.getGrade()));
			outputSb.append(System.lineSeparator());
		}
		
		System.out.println(outputSb.toString());	
	}
}
