package com.musala.simple.students.db.helper;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static Logger logger = LoggerFactory.getLogger(FileHelper.class);
	
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
			logger.error(ErrorMessage.PATH_MISSING);
			return false;
		}

		String argPath = args[0];
		File file = new File(argPath);

		/* Check if path exists */
		if (!file.exists()) {
			logger.error(ErrorMessage.INVALID_PATH);

			return false;
		}

		/* Check if the valid path is a file */
		if (!file.isFile()) {
			logger.error(ErrorMessage.NOT_A_FILE);

			return false;
		}

		/* Check the file extension to be JSON. */
		if (!argPath.endsWith(JSON_FILE_EXTENSION)) {
			logger.error(ErrorMessage.FILE_NOT_JSON);

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

	/**
	 * Validates a request for a specific user's details. If no second
	 * argument is provided to the command line it returns false - no request 
	 * was made at all. If a second argument was provided, but it can't be cast
	 * to a valid integer the method throws an exception. If the id is a valid 
	 * integer but negative, the method throws an exception. If all validations
	 * are passed the method returns true, else it returns false.
	 *
	 * @param args String array of command line passed arguments
	 * @return validRequest a boolean value to validate the request (true = valid,
	 * false = invalid).
	 */
	public static boolean validRequest(String[] args) {
		if (args.length < 2) {
			return false;
		}
		try {
			int studentId = Integer.parseInt(args[1]);
			if (studentId < 0) {
				logger.warn(ErrorMessage.NEGATIVE_ID);
				return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		} catch (NumberFormatException e) {
			logger.warn(ErrorMessage.INVALID_ID_FORMAT);
			return false;
		}
		return true;
	}
}
