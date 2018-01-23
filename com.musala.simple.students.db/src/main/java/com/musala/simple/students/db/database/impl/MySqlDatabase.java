package com.musala.simple.students.db.database.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.musala.simple.students.db.database.AbstractDatabase;
import com.musala.simple.students.db.database.DatabaseCommands;
import com.musala.simple.students.db.exception.StudentNotFoundException;
import com.musala.simple.students.db.internal.ErrorMessage;
import com.musala.simple.students.db.internal.InfoMessage;
import com.musala.simple.students.db.student.Student;

/**
 * MongoDb implementation of the {@link DatabaseCommands} interface.
 * 
 * @author yoan.petrushinov
 *
 */
public class MySqlDatabase extends AbstractDatabase {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String AGE = "age";
    private static final String GRADE = "grade";
    private static final String CURRENT_DB = " Database: MySql";

    private static final String DATABASE_URL = "%s%s:%d/%s";
    private static final String GET_STUDENT_STATEMENT = "SELECT * FROM students WHERE id = %d";
    private static final String GET_ALL_STUDENTS_STATEMENT = "SELECT * FROM students";
    private static final String INSERT_STUDENT_STATEMENT = "INSERT INTO students (" + " id," + " name," + " age,"
            + " grade ) VALUES (" + "?, ?, ?, ?)";
    private static final String DELETE_STUDENT_STATEMENT = "DELETE FROM students where id = ?";

    private static final String DUPLICATE_ENTRY_ERROR = "Duplicate entry";
    private static final String STUDEND_ADD_FAIL_DUPLICATE_ID = "Student %s with id %d can not be added to the database. Student with the same id already exists\n";
    private static final String STUDENT_ADD_FAIL = "Problem occured while trying to add %s with id %d to the database.\n";
    private static final String STUDENT_GET_FAIL = "Problem occured while trying to find student with id %d in the database.\n";
    private static final String BASE_URL = "jdbc:mysql://";
    private static final String LOCALHOST_URL = "jdbc:mysql://localhost";
    private static final String CREATE_STUDENTS_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS students "
            + "(id INTEGER not NULL, " + " name VARCHAR(50), " + " age INTEGER, " + " grade INTEGER, "
            + " PRIMARY KEY ( id ))";

    private Connection connection;
    private static MySqlDatabase singleton;

    private MySqlDatabase() {
    }

    public static MySqlDatabase getInstance() {
        if (singleton == null) {
            singleton = new MySqlDatabase();
        }
        return singleton;
    }

    @Override
    public void establishConnection() {
        String databaseURL = String.format(DATABASE_URL, BASE_URL, this.getHost(), this.getPort(), this.getName());
        String username = this.getUsername();
        String password = this.getPassword();

        // SQL command to create a database in MySQL.
        String createDbQuery = "CREATE DATABASE IF NOT EXISTS studentsDb";
        try {
            // Connect to localhost and create Database if it doesn't exist
            this.connection = DriverManager.getConnection(LOCALHOST_URL, username, password);
            PreparedStatement stmt = this.connection.prepareStatement(createDbQuery);
            stmt.execute();

            // Connect to the studentsDb database
            this.connection = DriverManager.getConnection(databaseURL, username, password);

            // create Table students if doesn't exist
            stmt = this.connection.prepareStatement(CREATE_STUDENTS_TABLE_STATEMENT);
            stmt.execute();
            stmt.close();
            getLogger().info(InfoMessage.DATABASE_CONNECTION_SUCCESS + CURRENT_DB);
        } catch (SQLException e) {
            throw new IllegalStateException(ErrorMessage.DATABASE_CONNECTION_FAIL, e);
        }
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#addStudent(Student)} method. Executes an SQL statement
     * to create a new entry in the 'students' table using the attributes of the
     * {@link Student} object provided as a parameter. The method handles attempts for
     * duplicate additions.
     * 
     */
    @Override
    public void addStudent(Student student) {
        try {
            PreparedStatement st = this.connection.prepareStatement(INSERT_STUDENT_STATEMENT);

            st.setInt(1, student.getId());
            st.setString(2, student.getName());
            st.setInt(3, student.getAge());
            st.setInt(4, student.getGrade());

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            if (e.getMessage().contains(DUPLICATE_ENTRY_ERROR)) {
                getLogger().error(
                        String.format(STUDEND_ADD_FAIL_DUPLICATE_ID, student.getName(), student.getId()) + CURRENT_DB);
            } else {
                getLogger().error(String.format(STUDENT_ADD_FAIL, student.getName(), student.getId()) + CURRENT_DB);
                getLogger().error(e.toString());
            }
        }
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#addMultipleStudents(List<Student> students)} method.
     * Iterates over the List<Student> parameter and calls the
     * {@link MySqlDatabase#addStudent(Student)} method for each Student object.
     * 
     * @param students
     *            a List containing multiple {@link Student} objects to be added to
     *            the database
     */
    @Override
    public void addMultipleStudents(List<Student> students) {
        for (Student student : students) {
            this.addStudent(student);
        }
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#addMultipleStudents(Student[] students)} method.
     * Casts the {@link Student} array to a List and calls the
     * {@link MySqlDatabase#addMultipleStudents(List)} method with it.
     * 
     * @param students
     *            an Array containing multiple {@link Student} objects to be added
     *            to the database
     */
    @Override
    public void addMultipleStudents(Student[] students) {
        List<Student> studentsList = Arrays.asList(students);
        this.addMultipleStudents(studentsList);
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#deleteStudentById(int)()} method. Executes a delete
     * sql statement on an entry by given id. Throws an exception if entry with the
     * given id does not exist.
     * 
     * @param studentId
     *            the id to find a student in the database
     */
    @Override
    public void deleteStudentById(int studentId) throws StudentNotFoundException {
        try {
            PreparedStatement st = this.connection.prepareStatement(DELETE_STUDENT_STATEMENT);
            st.setInt(1, studentId);
            st.executeUpdate();
            getLogger().info(String.format(InfoMessage.STUDENT_DELETE_SUCCESS, studentId) + CURRENT_DB);
            st.close();
        } catch (SQLException e) {
            getLogger().error(ErrorMessage.STUDENT_NOT_EXISTS + CURRENT_DB);
            getLogger().error(e.toString());
        }
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#getStudentById()} method. Executes sql SELECT
     * statement with the provided ID and if entry found creates a new {@link Student}
     * object with the entry's data and returns it. Else throws an exception.
     * 
     * @param studentId
     *            the id to find a student in the database
     * @return a Student object with id, name, age and grade
     */
    @Override
    public Student getStudentById(int sudentId) throws StudentNotFoundException {
        String query = String.format(GET_STUDENT_STATEMENT, sudentId);
        Student student = null;
        try (Statement statement = this.connection.createStatement();) {
            ResultSet rs = statement.executeQuery(query);

            if (rs.next()) {
                student = constructStudentObject(rs);
            } else {
                throw new StudentNotFoundException(ErrorMessage.STUDENT_NOT_EXISTS + CURRENT_DB);
            }
        } catch (SQLException e) {
            getLogger().error(String.format(STUDENT_GET_FAIL, sudentId) + CURRENT_DB);
            getLogger().error(e.toString());
        }
        return student;
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#getAllStudentsArr()} method. Executes a SELECT sql
     * statement to retrieve all the entries in the 'students' table. Iterates the
     * entries and constructs {@link Student} objects with the data. Each object gets
     * added to a List<Student> and the list is returned in the end (cast to an array).
     * 
     * @return a Student[] array
     */
    @Override
    public Student[] getAllStudentsArr() {
        List<Student> studentsList = new ArrayList<>();
        try (Statement statement = this.connection.createStatement()) {

            ResultSet rs = statement.executeQuery(GET_ALL_STUDENTS_STATEMENT);

            while (rs.next()) {
                Student student = constructStudentObject(rs);
                studentsList.add(student);
            }
        } catch (SQLException e) {
            getLogger().error(e.toString());
        }

        getLogger().info(InfoMessage.STUDENT_GET_ALL_SUCCESS + CURRENT_DB);
        return studentsList.toArray(new Student[studentsList.size()]);
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#getAllStudentsList()} method. Calls the
     * {@link MySqlDatabase#getAllStudentsArr()} and casts the returned array to a
     * List<Student> and returns it.
     * 
     * @return a List<Student> all students in a List
     */
    @Override
    public List<Student> getAllStudentsList() {
        return Arrays.asList(this.getAllStudentsArr());
    }

    /**
     * Casts all the values of the ResultSet to the corresponding data types and
     * passes them to the {@link Student} constructor to create a new object and
     * then returns that object.
     * 
     * @param rs
     *            The ResultSet containing the Student's information in a database
     *            format.
     * @return the new Student object
     */
    private Student constructStudentObject(ResultSet rs) throws SQLException {
        String name = rs.getString(NAME);
        int id = rs.getInt(ID);
        int age = rs.getInt(AGE);
        int grade = rs.getInt(GRADE);
        return new Student(id, name, age, grade);
    }
}
