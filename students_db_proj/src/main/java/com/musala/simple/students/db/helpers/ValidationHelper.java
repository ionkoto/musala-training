package com.musala.simple.students.db.helpers;

import java.io.File;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.musala.simple.students.db.internal.ErrorMessage;

/**
 * This is a helper class providing methods for validation purposes.
 * 
 * @author yoan.petrushinov
 * 
 */
public class ValidationHelper {
	private ValidationHelper() {

	}

	static final String JSON_FILE_EXTENSION = ".json";

	/**
	 * Validates the command line input arguments.
	 *
	 * @param args
	 *            A String array with command line arguments
	 * @return true if the first argument in the array is a valid path to a file
	 *         with extension ".json".
	 */
	public static boolean isInputValid(String[] args) {

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
	public static boolean isValidJson(String Json) {
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

	public static boolean validRequest(String[] args) {
		if (args.length < 2) {
			return false;
		}
		try {
			int studentId = Integer.parseInt(args[1]);
			if (studentId < 0) {
				System.err.println(ErrorMessage.NEGATIVE_ID);
				return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		} catch (NumberFormatException e) {
			System.err.println(ErrorMessage.INVALID_ID_FORMAT);
			return false;
		}
		return true;
	}
}
