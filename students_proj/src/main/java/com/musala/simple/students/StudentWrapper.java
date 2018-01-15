package com.musala.simple.students;

/**
* This is a helper class used to parse a JSON array of students.
* The user of this class can pass it as an argument to the 
* {@link com.google.gson.Gson#fromJson(String, Class)} method.
* 
* @author yoan.petrushinov
*
*/
public class StudentWrapper {
	public Student[] students;
}
