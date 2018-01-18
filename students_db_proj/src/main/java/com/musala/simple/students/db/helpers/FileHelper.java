package com.musala.simple.students.db.helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import com.musala.simple.students.db.DatabaseTypes;
import com.musala.simple.students.db.Main;
import com.musala.simple.students.db.internal.PathConstants;

/**
 * This is a helper class providing methods for creating, reading and updating
 * files.
 * 
 * @author yoan.petrushinov
 * 
 */
public class FileHelper {
	private static Logger logger = LoggerFactory.getLogger(Main.class);

	private FileHelper() {

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
	public static String readFile(String path) {
		try {
			return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			logger.error(String.format("Caught an exception while reading file from path %s:\n", path));
			logger.error("Stacktrace:", e);
		}
		return null;
	}

	/**
	 * Reads a file containing the predefined properties, required for successful
	 * establishment of the connection to the database. Base on the database type,
	 * passed as an argument the corresponding config file is retrieved.
	 *
	 * @param dbType
	 *            - enum type specifying the type of Database the user chooses to
	 *            user.
	 * @return an object of type {@link Properties}, containing properties in
	 *         key-value pair format.
	 */
	public static Properties readDbPropertiesFile(DatabaseTypes dbType) {
		String configPath = null;

		switch (dbType) {
		case MongoDb:
			configPath = PathConstants.MONGODB_PROPS_PATH;
			break;
		case MySQL:
			// configPath = PathConstants.MYSQL_PROPS_PATH;
		case PostgreSQL:
			// configPath = PathConstants.PostgreSQL_PROPS_PATH;
		default:
			break;
		}

		Properties prop = new Properties();
		try (FileInputStream fis = new FileInputStream(configPath)) {
			prop.load(fis);
		} catch (IOException e) {
			logger.error(
					String.format("An error occured while trying to read the database config file: %s", configPath));
			logger.info("Stacktrace:", e);
		}

		return prop;
	}
}
