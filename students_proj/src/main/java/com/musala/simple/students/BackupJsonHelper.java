package com.musala.simple.students;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.maven.wagon.ResourceDoesNotExistException;

import com.google.gson.Gson;
import com.musala.simple.students.internal.ErrorMessage;
import com.musala.simple.students.internal.InfoMessage;
import com.musala.simple.students.internal.PathConstants;

/**
* This is a helper class providing methods for creating, 
* reading and updating a backup json file (local database).
* 
* @author yoan.petrushinov
*
*/
class BackupJsonHelper {

	/**
	 * Creates a backup Json file using the students objects
	 * provided as a Json String parameter. If a backup file
	 * already exists, it's being overwritten.
	 *
	 * @param studentsJsonInfo A Json String, containing the students
	 * to be written to the backup file.
	 */
	static void createBackupJson(String studentsJsonInfo) {
		try (FileWriter fileWriter = new FileWriter(PathConstants.BACKUP_JSON)) {
			fileWriter.write(studentsJsonInfo);
		} catch (IOException e) {
			System.err.println(ErrorMessage.BACKUP_JSON_CREATION_FAIL);
		}
	}

	/**
	 * Reads the backup Json file (if exists) and prints the
	 * students information from it.
	 */
	static StudentWrapper readBackupJson() throws ResourceDoesNotExistException {
		File backupFile = new File(PathConstants.BACKUP_JSON);

		if (!backupFile.exists()) {
			throw new ResourceDoesNotExistException(ErrorMessage.NO_BACKUP_JSON);
		}
		String backupStudentsJson = Main.readFile(PathConstants.BACKUP_JSON);

		return new Gson().fromJson(backupStudentsJson, StudentWrapper.class);
	}

	/**
	 * Updates the backup file by adding the new students. If the backup
	 * file does not exist the {@link Main#createBackupJson(String)} is called
	 * and the backup is created with the new students.
	 *
	 * @param studentsJsonInfo A Json String, containing the students
	 * to be added to the backup file.
	 */
	public static void updateBackupJson(String studentsJsonInfo) {
		File backupFile = new File(PathConstants.BACKUP_JSON);

		if (!backupFile.exists()) {
			System.out.println(InfoMessage.CREATING_BACKUP);
			createBackupJson(studentsJsonInfo);
		} else {
			String backupFileInfo = Main.readFile(PathConstants.BACKUP_JSON);
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
}
