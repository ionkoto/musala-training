package com.musala.simple.students.spring.web.database.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.musala.simple.students.spring.web.database.AbstractDatabase;
import com.musala.simple.students.spring.web.database.DatabaseStudentCommands;
import com.musala.simple.students.spring.web.dbevents.DbEventsCourses;
import com.musala.simple.students.spring.web.dbevents.DbEventsStudents;
import com.musala.simple.students.spring.web.dbevents.DbEventsTeachers;
import com.musala.simple.students.spring.web.dbevents.Event;
import com.musala.simple.students.spring.web.exception.CourseNotFoundException;
import com.musala.simple.students.spring.web.exception.StudentNotFoundException;
import com.musala.simple.students.spring.web.exception.TeacherNotFoundException;
import com.musala.simple.students.spring.web.internal.ErrorMessage;
import com.musala.simple.students.spring.web.internal.InfoMessage;
import com.musala.simple.students.spring.web.models.course.Course;
import com.musala.simple.students.spring.web.models.student.Student;
import com.musala.simple.students.spring.web.models.teacher.Teacher;

/**
 * MongoDb implementation of the {@link DatabaseStudentCommands} interface.
 * 
 * @author yoan.petrushinov
 *
 */
public class MySqlDatabase extends AbstractDatabase {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String AGE = "age";
    private static final String GRADE = "grade";
    private static final String EMAIL = "email";
    private static final String CURRENT_DB = " Database: MySql";

    private static final String DATABASE_URL = "%s%s:%d/%s";
    private static final String GET_STUDENT_STATEMENT = "SELECT * FROM students WHERE id = %d";
    private static final String GET_TEACHER_STATEMENT = "SELECT * FROM teachers WHERE id = %d";
    private static final String GET_COURSE_STATEMENT = "SELECT * FROM courses WHERE id = %d";
    private static final String GET_ALL_STUDENTS_STATEMENT = "SELECT * FROM students";
    private static final String GET_ALL_TEACHERS_STATEMENT = "SELECT * FROM teachers";
    private static final String GET_ALL_COURSES_STATEMENT = "SELECT * FROM courses";
    private static final String INSERT_STUDENT_STATEMENT = 
            "INSERT INTO students (" + " id," + " name," + " age, grade ) VALUES (" + "?, ?, ?, ?)";
    private static final String INSERT_TEACHER_STATEMENT = 
            "INSERT INTO teachers (" + " id," + " name," + " email ) VALUES (" + "?, ?, ?)";
    private static final String INSERT_COURSE_STATEMENT = 
            "INSERT INTO courses (" + " id," + " name ) VALUES (" + "?, ?)";
    private static final String GET_COURSE_STUDENTS_STATEMENT = "SELECT s.id, s.name, s.age, s.grade " + 
            "FROM students s, students_courses " + 
            "WHERE students_courses.courseId_FK = %d " + 
            "AND students_courses.sudentId_FK = s.id";
    private static final String DELETE_STUDENT_STATEMENT = "DELETE FROM students where id = ?";
    private static final String DELETE_TEACHER_STATEMENT = "DELETE FROM teachers where id = ?";
    private static final String DELETE_COURSE_STATEMENT = "DELETE FROM courses where id = ?";
    private static final String DUPLICATE_ENTRY_ERROR = "Duplicate entry";
    private static final String STUDEND_ADD_FAIL_DUPLICATE_ID = "Student %s with id %d can not be added to the database. Student with the same id already exists\n";
    private static final String TEACHER_ADD_FAIL_DUPLICATE_ID = "Teacher %s with id %d can not be added to the database. Teacher with the same id already exists\n";
    private static final String COURSE_ADD_FAIL_DUPLICATE_ID = "Course %s with id %d can not be added to the database. Course with the same id already exists\n";
    private static final String STUDENT_ADD_FAIL = "Problem occured while trying to add student %s with id %d to the database.\n";
    private static final String TEACHER_ADD_FAIL = "Problem occured while trying to add teacher %s with id %d to the database.\n";
    private static final String COURSE_ADD_FAIL = "Problem occured while trying to add course %s with id %d to the database.\n";
    private static final String STUDENT_GET_FAIL = "Problem occured while trying to find student with id %d in the database.\n";
    private static final String TEACHER_GET_FAIL = "Problem occured while trying to find teacher with id %d in the database.\n";
    private static final String COURSE_GET_FAIL = "Problem occured while trying to find course with id %d in the database.\n";
    private static final String BASE_URL = "jdbc:mysql://";
    private static final String LOCALHOST_URL = "jdbc:mysql://localhost";
    private static final String CREATE_STUDENTS_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS students "
            + "(id INTEGER not NULL, " + " name VARCHAR(50), " + " age INTEGER, " + " grade INTEGER, "
            + " PRIMARY KEY ( id ))";
    private static final String CREATE_TEACHERS_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS teachers "
            + "(id INTEGER not NULL, " + " name VARCHAR(50), " + " email VARCHAR(50), " + " PRIMARY KEY ( id ))";
    private static final String CREATE_COURSES_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS coarses "
            + "(id INTEGER not NULL, " + " name VARCHAR(50), " + " PRIMARY KEY ( id ))";
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
            
         // create Table teachers if doesn't exist
            stmt = this.connection.prepareStatement(CREATE_TEACHERS_TABLE_STATEMENT);
            stmt.execute();
            
         // create Table courses if doesn't exist
            stmt = this.connection.prepareStatement(CREATE_COURSES_TABLE_STATEMENT);
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
    public boolean addStudent(Student student) {
        try {
            PreparedStatement st = this.connection.prepareStatement(INSERT_STUDENT_STATEMENT);

            st.setInt(1, student.getId());
            st.setString(2, student.getName());
            st.setInt(3, student.getAge());
            st.setInt(4, student.getGrade());

            st.executeUpdate();
            st.close();
            this.registerEvent(new Event(DbEventsStudents.AddStudentSuccessMySql.getMessage(), DbEventsStudents.AddStudentSuccessMySql.getCode(),
                    System.currentTimeMillis()));
            return true;
        } catch (SQLException e) {

            if (e.getMessage().contains(DUPLICATE_ENTRY_ERROR)) {
                this.registerEvent(new Event(DbEventsStudents.AddDuplicateStudentFail.getMessage(),
                        DbEventsStudents.AddDuplicateStudentFail.getCode(), System.currentTimeMillis()));
                getLogger().error(
                        String.format(STUDEND_ADD_FAIL_DUPLICATE_ID, student.getName(), student.getId()) + CURRENT_DB);
            } else {
                this.registerEvent(new Event(DbEventsStudents.AddStudentFail.getMessage(), DbEventsStudents.AddStudentFail.getCode(),
                        System.currentTimeMillis()));
                getLogger().error(String.format(STUDENT_ADD_FAIL, student.getName(), student.getId()) + CURRENT_DB);
                getLogger().error(e.toString());
            }
        }
        return false;
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
        this.registerEvent(new Event(DbEventsStudents.AddMultipleSuccess.getMessage(), DbEventsStudents.AddMultipleSuccess.getCode(),
                System.currentTimeMillis()));
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
            this.registerEvent(new Event(DbEventsStudents.AddStudentSuccessMySql.getMessage(),
                    DbEventsStudents.AddStudentSuccessMySql.getCode(), System.currentTimeMillis()));
        } catch (SQLException e) {
            this.registerEvent(new Event(DbEventsStudents.DeleteStudentFail.getMessage(), DbEventsStudents.DeleteStudentFail.getCode(),
                    System.currentTimeMillis()));
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
            this.registerEvent(new Event(DbEventsStudents.GetStudentFail.getMessage(), DbEventsStudents.GetStudentFail.getCode(),
                    System.currentTimeMillis()));
            getLogger().error(String.format(STUDENT_GET_FAIL, sudentId) + CURRENT_DB);
            getLogger().error(e.toString());
        }
        this.registerEvent(new Event(DbEventsStudents.GetStudentSuccess.getMessage(), DbEventsStudents.GetStudentsSuccess.getCode(),
                System.currentTimeMillis()));
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
            this.registerEvent(new Event(DbEventsStudents.GetStudentsFail.getMessage(), DbEventsStudents.GetStudentsFail.getCode(),
                    System.currentTimeMillis()));
            getLogger().error(e.toString());
        }

        getLogger().info(InfoMessage.STUDENT_GET_ALL_SUCCESS + CURRENT_DB);
        this.registerEvent(new Event(DbEventsStudents.GetStudentsSuccess.getMessage(), DbEventsStudents.GetStudentsSuccess.getCode(),
                System.currentTimeMillis()));
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

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#addTeacher(Teacher)} method. Executes an SQL statement
     * to create a new entry in the 'teachers' table using the attributes of the
     * {@link Teacher} object provided as a parameter. The method handles attempts for
     * duplicate additions.
     * 
     */
    @Override
    public boolean addTeacher(Teacher teacher) {
        try {
            PreparedStatement st = this.connection.prepareStatement(INSERT_TEACHER_STATEMENT);

            st.setInt(1, teacher.getId());
            st.setString(2, teacher.getName());
            st.setString(3, teacher.getEmail());

            st.executeUpdate();
            st.close();
            this.registerEvent(new Event(DbEventsTeachers.AddTeacherSuccessMySql.getMessage(), DbEventsTeachers.AddTeacherSuccessMySql.getCode(),
                    System.currentTimeMillis()));
            return true;
        } catch (SQLException e) {

            if (e.getMessage().contains(DUPLICATE_ENTRY_ERROR)) {
                this.registerEvent(new Event(DbEventsTeachers.AddDuplicateTeacherFail.getMessage(),
                        DbEventsTeachers.AddDuplicateTeacherFail.getCode(), System.currentTimeMillis()));
                getLogger().error(
                        String.format(TEACHER_ADD_FAIL_DUPLICATE_ID, teacher.getName(), teacher.getId()) + CURRENT_DB);
            } else {
                this.registerEvent(new Event(DbEventsTeachers.AddTeacherFail.getMessage(), DbEventsTeachers.AddTeacherFail.getCode(),
                        System.currentTimeMillis()));
                getLogger().error(String.format(TEACHER_ADD_FAIL, teacher.getName(), teacher.getId()) + CURRENT_DB);
                getLogger().error(e.toString());
            }
        }
        return false;
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#addMultipleTeachers(List<Teacher> teachers)} method.
     * Iterates over the List<Teacher> parameter and calls the
     * {@link MySqlDatabase#addTeacher(Teacher)} method for each Teacher object.
     * 
     * @param teachers
     *            a List containing multiple {@link Teacher} objects to be added to
     *            the database
     */
    @Override
    public void addMultipleTeachers(List<Teacher> teachers) {
        for (Teacher teacher : teachers) {
            this.addTeacher(teacher);
        }
        this.registerEvent(new Event(DbEventsTeachers.AddMultipleSuccess.getMessage(), DbEventsTeachers.AddMultipleSuccess.getCode(),
                System.currentTimeMillis()));
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#addMultipleTeachers(Teacher[] teachers)} method.
     * Casts the {@link Teacher} array to a List and calls the
     * {@link MySqlDatabase#addMultipleTeachers(List)} method with it.
     * 
     * @param teachers
     *            an Array containing multiple {@link Teacher} objects to be added
     *            to the database
     */
    @Override
    public void addMultipleTeachers(Teacher[] teachers) {
        List<Teacher> teachersList = Arrays.asList(teachers);
        this.addMultipleTeachers(teachersList);
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#deleteTeacherById(int)()} method. Executes a delete
     * sql statement on an entry by given id. Throws an exception if entry with the
     * given id does not exist.
     * 
     * @param teacherId
     *            the id to find a teacher in the database
     */
    @Override
    public void deleteTeacherById(int teacherId) throws TeacherNotFoundException {
        try {
            PreparedStatement st = this.connection.prepareStatement(DELETE_TEACHER_STATEMENT);
            st.setInt(1, teacherId);
            st.executeUpdate();
            getLogger().info(String.format(InfoMessage.TEACHER_DELETE_SUCCESS, teacherId) + CURRENT_DB);
            st.close();
            this.registerEvent(new Event(DbEventsTeachers.AddTeacherSuccessMySql.getMessage(),
                    DbEventsTeachers.AddTeacherSuccessMySql.getCode(), System.currentTimeMillis()));
        } catch (SQLException e) {
            this.registerEvent(new Event(DbEventsTeachers.DeleteTeacherFail.getMessage(), DbEventsTeachers.DeleteTeacherFail.getCode(),
                    System.currentTimeMillis()));
            getLogger().error(ErrorMessage.TEACHER_NOT_EXISTS + CURRENT_DB);
            getLogger().error(e.toString());
        }
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#getTeacherById()} method. Executes sql SELECT
     * statement with the provided ID and if entry found creates a new {@link Teacher}
     * object with the entry's data and returns it. Else throws an exception.
     * 
     * @param teacherId
     *            the id to find a teacher in the database
     * @return a Teacher object with id, name and email
     */
    @Override
    public Teacher getTeacherById(int teacherId) throws TeacherNotFoundException {
        String query = String.format(GET_TEACHER_STATEMENT, teacherId);
        Teacher teacher = null;
        try (Statement statement = this.connection.createStatement();) {
            ResultSet rs = statement.executeQuery(query);

            if (rs.next()) {
                teacher = constructTeacherObject(rs);
            } else {
                throw new TeacherNotFoundException(ErrorMessage.TEACHER_NOT_EXISTS + CURRENT_DB);
            }
        } catch (SQLException e) {
            this.registerEvent(new Event(DbEventsTeachers.GetTeacherFail.getMessage(), DbEventsTeachers.GetTeacherFail.getCode(),
                    System.currentTimeMillis()));
            getLogger().error(String.format(TEACHER_GET_FAIL, teacherId) + CURRENT_DB);
            getLogger().error(e.toString());
        }
        this.registerEvent(new Event(DbEventsTeachers.GetTeacherSuccess.getMessage(), DbEventsTeachers.GetTeachersSuccess.getCode(),
                System.currentTimeMillis()));
        return teacher;
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#getAllTeachersArr()} method. Executes a SELECT sql
     * statement to retrieve all the entries in the 'teachers' table. Iterates the
     * entries and constructs {@link Teacher} objects with the data. Each object gets
     * added to a List<Teacher> and the list is returned in the end (cast to an array).
     * 
     * @return a Teacher[] array
     */
    @Override
    public Teacher[] getAllTeachersArr() {
        List<Teacher> teachersList = new ArrayList<>();
        try (Statement statement = this.connection.createStatement()) {

            ResultSet rs = statement.executeQuery(GET_ALL_TEACHERS_STATEMENT);

            while (rs.next()) {
                Teacher teacher = constructTeacherObject(rs);
                teachersList.add(teacher);
            }
        } catch (SQLException e) {
            this.registerEvent(new Event(DbEventsTeachers.GetTeachersFail.getMessage(), DbEventsTeachers.GetTeachersFail.getCode(),
                    System.currentTimeMillis()));
            getLogger().error(e.toString());
        }

        getLogger().info(InfoMessage.TEACHER_GET_ALL_SUCCESS + CURRENT_DB);
        this.registerEvent(new Event(DbEventsTeachers.GetTeachersSuccess.getMessage(), DbEventsTeachers.GetTeachersSuccess.getCode(),
                System.currentTimeMillis()));
        return teachersList.toArray(new Teacher[teachersList.size()]);
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#getAllTeachersList()} method. Calls the
     * {@link MySqlDatabase#getAllTeachersArr()} and casts the returned array to a
     * List<Teacher> and returns it.
     * 
     * @return a List<Teacher> all teachers in a List
     */
    @Override
    public List<Teacher> getAllTeachersList() {
        return Arrays.asList(this.getAllTeachersArr());
    }
    
    /**
     * Casts all the values of the ResultSet to the corresponding data types and
     * passes them to the {@link Teacher} constructor to create a new object and
     * then returns that object.
     * 
     * @param rs
     *            The ResultSet containing the Teacher's information in a database
     *            format.
     * @return the new Teacher object
     */
    private Teacher constructTeacherObject(ResultSet rs) throws SQLException {
        String name = rs.getString(NAME);
        int id = rs.getInt(ID);
        String email = rs.getString(EMAIL);
        return new Teacher(id, name, email);
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#addCourse(Course)} method. Executes an SQL statement
     * to create a new entry in the 'courses' table using the attributes of the
     * {@link Course} object provided as a parameter. The method handles attempts for
     * duplicate additions.
     * 
     */
    @Override
    public boolean addCourse(Course course) {
        try {
            PreparedStatement st = this.connection.prepareStatement(INSERT_COURSE_STATEMENT);

            st.setInt(1, course.getId());
            st.setString(2, course.getName());

            st.executeUpdate();
            st.close();
            this.registerEvent(new Event(DbEventsCourses.AddCourseSuccessMySql.getMessage(), DbEventsCourses.AddCourseSuccessMySql.getCode(),
                    System.currentTimeMillis()));
            return true;
        } catch (SQLException e) {

            if (e.getMessage().contains(DUPLICATE_ENTRY_ERROR)) {
                this.registerEvent(new Event(DbEventsCourses.AddDuplicateCourseFail.getMessage(),
                        DbEventsCourses.AddDuplicateCourseFail.getCode(), System.currentTimeMillis()));
                getLogger().error(
                        String.format(COURSE_ADD_FAIL_DUPLICATE_ID, course.getName(), course.getId()) + CURRENT_DB);
            } else {
                this.registerEvent(new Event(DbEventsCourses.AddCourseFail.getMessage(), DbEventsCourses.AddCourseFail.getCode(),
                        System.currentTimeMillis()));
                getLogger().error(String.format(COURSE_ADD_FAIL, course.getName(), course.getId()) + CURRENT_DB);
                getLogger().error(e.toString());
            }
        }
        return false;
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#addMultipleCourses(List<Course> courses)} method.
     * Iterates over the List<Course> parameter and calls the
     * {@link MySqlDatabase#addCourse(Course)} method for each Course object.
     * 
     * @param courses
     *            a List containing multiple {@link Course} objects to be added to
     *            the database
     */
    @Override
    public void addMultipleCourses(List<Course> courses) {
        for (Course course : courses) {
            this.addCourse(course);
        }
        this.registerEvent(new Event(DbEventsCourses.AddMultipleSuccess.getMessage(), DbEventsCourses.AddMultipleSuccess.getCode(),
                System.currentTimeMillis()));
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#addMultipleCourses(Course[] courses)} method.
     * Casts the {@link Course} array to a List and calls the
     * {@link MySqlDatabase#addMultipleCourses(List)} method with it.
     * 
     * @param courses
     *            an Array containing multiple {@link Course} objects to be added
     *            to the database
     */
    @Override
    public void addMultipleCourses(Course[] courses) {
        List<Course> coursesList = Arrays.asList(courses);
        this.addMultipleCourses(coursesList);
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#deleteCourseById(int)()} method. Executes a delete
     * sql statement on an entry by given id. Throws an exception if entry with the
     * given id does not exist.
     * 
     * @param courseId
     *            the id to find a course in the database
     */
    @Override
    public void deleteCourseById(int courseId) throws CourseNotFoundException {
        try {
            PreparedStatement st = this.connection.prepareStatement(DELETE_COURSE_STATEMENT);
            st.setInt(1, courseId);
            st.executeUpdate();
            getLogger().info(String.format(InfoMessage.COURSE_DELETE_SUCCESS, courseId) + CURRENT_DB);
            st.close();
            this.registerEvent(new Event(DbEventsCourses.AddCourseSuccessMySql.getMessage(),
                    DbEventsCourses.AddCourseSuccessMySql.getCode(), System.currentTimeMillis()));
        } catch (SQLException e) {
            this.registerEvent(new Event(DbEventsCourses.DeleteCourseFail.getMessage(), DbEventsCourses.DeleteCourseFail.getCode(),
                    System.currentTimeMillis()));
            getLogger().error(ErrorMessage.COURSE_NOT_EXISTS + CURRENT_DB);
            getLogger().error(e.toString());
        }
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#getCourseById()} method. Executes sql SELECT
     * statement with the provided ID and if entry found creates a new {@link Course}
     * object with the entry's data and returns it. Else throws an exception.
     * 
     * @param courseId
     *            the id to find a course in the database
     * @return a Course object with id, name and email
     */
    @Override
    public Course getCourseById(int courseId) throws CourseNotFoundException {
        String query = String.format(GET_COURSE_STATEMENT, courseId);
        Course course = null;
        try (Statement statement = this.connection.createStatement();) {
            ResultSet rs = statement.executeQuery(query);

            if (rs.next()) {
                course = constructCourseObject(rs);
            } else {
                throw new CourseNotFoundException(ErrorMessage.COURSE_NOT_EXISTS + CURRENT_DB);
            }
        } catch (SQLException e) {
            this.registerEvent(new Event(DbEventsCourses.GetCourseFail.getMessage(), DbEventsCourses.GetCourseFail.getCode(),
                    System.currentTimeMillis()));
            getLogger().error(String.format(COURSE_GET_FAIL, courseId) + CURRENT_DB);
            getLogger().error(e.toString());
        }
        this.registerEvent(new Event(DbEventsCourses.GetCourseSuccess.getMessage(), DbEventsCourses.GetCoursesSuccess.getCode(),
                System.currentTimeMillis()));
        return course;
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#getAllCoursesArr()} method. Executes a SELECT sql
     * statement to retrieve all the entries in the 'courses' table. Iterates the
     * entries and constructs {@link Course} objects with the data. Each object gets
     * added to a List<Course> and the list is returned in the end (cast to an array).
     * 
     * @return a Course[] array
     */
    @Override
    public Course[] getAllCoursesArr() {
        List<Course> coursesList = new ArrayList<>();
        try (Statement statement = this.connection.createStatement()) {

            ResultSet rs = statement.executeQuery(GET_ALL_COURSES_STATEMENT);

            while (rs.next()) {
                Course course = constructCourseObject(rs);
                coursesList.add(course);
            }
        } catch (SQLException e) {
            this.registerEvent(new Event(DbEventsCourses.GetCoursesFail.getMessage(), DbEventsCourses.GetCoursesFail.getCode(),
                    System.currentTimeMillis()));
            getLogger().error(e.toString());
        }

        getLogger().info(InfoMessage.COURSE_GET_ALL_SUCCESS + CURRENT_DB);
        this.registerEvent(new Event(DbEventsCourses.GetCoursesSuccess.getMessage(), DbEventsCourses.GetCoursesSuccess.getCode(),
                System.currentTimeMillis()));
        return coursesList.toArray(new Course[coursesList.size()]);
    }

    /**
     * A MySql-specific implementation of the
     * {@link AbstractDatabase#getAllCoursesList()} method. Calls the
     * {@link MySqlDatabase#getAllCoursesArr()} and casts the returned array to a
     * List<Course> and returns it.
     * 
     * @return a List<Course> all courses in a List
     */
    @Override
    public List<Course> getAllCoursesList() {
        return Arrays.asList(this.getAllCoursesArr());
    }
    
    /**
     * Casts all the values of the ResultSet to the corresponding data types and
     * passes them to the {@link Course} constructor to create a new object and
     * then returns that object.
     * 
     * @param rs
     *            The ResultSet containing the Course's information in a database
     *            format.
     * @return the new Course object
     */
    private Course constructCourseObject(ResultSet rs) throws SQLException {
        String name = rs.getString(NAME);
        int id = rs.getInt(ID);
        return new Course(id, name);
    }

    @Override
    public List<Teacher> getCourseTeachers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Student[] getCourseStudentsArr(int courseId) {
        List<Student> studentsList = new ArrayList<>();
        try (Statement statement = this.connection.createStatement()) {

            ResultSet rs = statement.executeQuery(String.format(GET_COURSE_STUDENTS_STATEMENT, courseId));
            while (rs.next()) {
                Student student = constructStudentObject(rs);
                studentsList.add(student);
            }
        } catch (SQLException e) {
            this.registerEvent(new Event(DbEventsCourses.GetCourseStudentsFail.getMessage(), DbEventsCourses.GetCourseStudentsFail.getCode(),
                    System.currentTimeMillis()));
            getLogger().error(e.toString());
        }

        getLogger().info(InfoMessage.COURSE_GET_STUDENTS_SUCCESS + CURRENT_DB);
        this.registerEvent(new Event(DbEventsCourses.GetCourseStudentsSuccess.getMessage(), DbEventsCourses.GetCourseStudentsSuccess.getCode(),
                System.currentTimeMillis()));
        return studentsList.toArray(new Student[studentsList.size()]);
    }

    @Override
    public List<Student> getCourseStudentsList(int courseId) {
        return Arrays.asList(this.getCourseStudentsArr(courseId));
    }
}