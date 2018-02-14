package com.musala.simple.students.spring.web.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.musala.simple.students.spring.web.database.AbstractDatabase;
import com.musala.simple.students.spring.web.database.DatabaseFactory;
import com.musala.simple.students.spring.web.database.DatabaseType;
import com.musala.simple.students.spring.web.exception.StudentNotFoundException;
import com.musala.simple.students.spring.web.internal.ErrorMessage;
import com.musala.simple.students.spring.web.models.student.Student;
import com.musala.simple.students.spring.web.models.student.StudentDataPrinter;
import com.musala.simple.students.spring.web.models.student.StudentGroup;
import com.musala.simple.students.spring.web.models.student.StudentWrapper;

/**
 * This is a helper class for performing predefined actions on a database and
 * output information on the console for the benefit of the user.
 * 
 * @author yoan.petrushinov
 * 
 */
public class DbHelper {
    private static final String DEFAULT_DB_NAME = "studentsDb";
    private static final String DEFAULT_DB_PORT = "27017";
    private static final String DEFAULT_DB_HOST = "localhost";
    private static final String DEFAULT_DB_USERNAME = "admin";
    private static final String DEFAULT_DB_PASSWORD = "admin";
    private static final String DB_NAME = "name";
    private static final String DB_PORT = "port";
    private static final String DB_HOST = "host";
    private static final String DB_USER = "username";
    private static final String DB_PASSWORD = "password";
    private static Logger logger = LoggerFactory.getLogger(DbHelper.class);

    private DbHelper() {

    }

    /**
     * Performs predefined actions using a .json file provided as user input
     * and returning different outputs to the console for the benefit of the user.
     *
     * @param args
     *            arguments array provided by the user as input on the command line
     *            database
     *            an implementation of the AbstractDatabase class
     */
    public static void performDatabaseActions(String[] args, AbstractDatabase database) {
        logger.info("\n\nCurrently working with " + database.getClass().getSimpleName() + "\n\n");
        if (ValidationHelper.isInputValid(args)) {

            String studentsJsonInfo = FileHelper.readFile(args[0]);

            if (!ValidationHelper.isValidJson(studentsJsonInfo)) {
                logger.warn(ErrorMessage.NOT_VALID_JSON);
                Student[] students = database.getAllStudentsArr();
                StudentDataPrinter.printStudents(students);
            } else {
                StudentWrapper studentWrapper = new Gson().fromJson(studentsJsonInfo, StudentWrapper.class);
                StudentGroup studentGroup = new StudentGroup();
                studentGroup.fillStudentGroup(studentWrapper.students);
                Map<Integer, Student> studentsMap = studentGroup.getStudents();
                List<Student> studentsList = new ArrayList<Student>(studentsMap.values());
                database.addMultipleStudents(studentsList);

                if (ValidationHelper.validUserDetailsRequest(args)) {
                    int studentId = Integer.parseInt(args[1]);
                    try {
                        // Look for the student in the Student Group
                        Student student = studentGroup.getStudentById(studentId);
                        StudentDataPrinter.printStudentDetails(student);
                    } catch (StudentNotFoundException snfe) {
                        logger.warn(ErrorMessage.STUDENT_NOT_FOUND);
                        try {
                            Student student = database.getStudentById(studentId);
                            StudentDataPrinter.printStudentDetails(student);
                        } catch (StudentNotFoundException e) {
                            logger.warn(e.getMessage());
                            StudentDataPrinter.printStudents(studentsList);
                        }
                    }
                } else {
                    StudentDataPrinter.printStudents(studentsList);
                }
            }
        } else {
            Student[] students = database.getAllStudentsArr();
            if (students.length == 0) {
                logger.info(ErrorMessage.DATABASE_EMPTY);
            } else {
                StudentDataPrinter.printStudents(students);
            }
        }
    }

    /**
     * Creates a new instance of an implementation of the {@link AbstractDatabase} class.
     *
     * @param dbType
     *            The type of database implementation to be initialized (e.g. Mongo, MySql)
     * @return database The new instance of the database initialized.
     */
    public static AbstractDatabase initializeDatabase(DatabaseType dbType) {
        Properties dbProperties = FileHelper.readDbPropertiesFile(dbType);
        AbstractDatabase database = DatabaseFactory.createDatabase(dbType)
                .withName(dbProperties.getProperty(DB_NAME, DEFAULT_DB_NAME))
                .withHost(dbProperties.getProperty(DB_HOST, DEFAULT_DB_HOST))
                .withPort(dbProperties.getProperty(DB_PORT, DEFAULT_DB_PORT))
                .withUsername(dbProperties.getProperty(DB_USER, DEFAULT_DB_USERNAME))
                .withPassword(dbProperties.getProperty(DB_PASSWORD, DEFAULT_DB_PASSWORD)).build();
        database.establishConnection();
        return database;
    }
}
