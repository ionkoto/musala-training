package com.musala.simple.students.db.internal;

/**
 * This is an internal class, holding constant variables, 
 * used for Info-messaging.
 * 
 * @author yoan.petrushinov
 *
 */
public class InfoMessage {
	public static final String DATABASE_CONNECTION_SUCCESS = "Database connection established successfully!";
	public static final String STUDENT_DELETE_SUCCESS = "Student with id %d successfulyl deleted!";
	public static final String STUDENT_GET_ALL_SUCCESS = "Successfully retrieved all the students from the database.";
	private InfoMessage() {
		
	}
}
