package com.musala.simple.students.spring.web.database.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.musala.simple.students.spring.web.database.AbstractDatabase;
import com.musala.simple.students.spring.web.database.DatabaseCommands;
import com.musala.simple.students.spring.web.dbevents.DbEvents;
import com.musala.simple.students.spring.web.dbevents.Event;
import com.musala.simple.students.spring.web.dbevents.EventLogger;
import com.musala.simple.students.spring.web.exception.StudentNotFoundException;
import com.musala.simple.students.spring.web.internal.ErrorMessage;
import com.musala.simple.students.spring.web.internal.InfoMessage;
import com.musala.simple.students.spring.web.student.Student;

/**
 * MongoDb implementation of the {@link DatabaseCommands} interface.
 * 
 * @author yoan.petrushinov
 *
 */
public class MyMongoDatabase extends AbstractDatabase {
    private static final String DB_DUPLICATE_ENTRY_ATTEMPT_ERROR_ID = "E11000";
    private static final String STUDEND_ADD_FAIL_DUPLICATE_ID = "Student %s with id %d can not be added to the database. Student with the same id already exists\n";
    private static final String STUDENT_ADD_FAIL = "Problem occured while trying to add %s with id %d to the database.\n";
    private static final String UNIQUE_IDENTIFIER = "id";
    private static final String STUDENT_COLLECTION_NAME = "students";
    private static final String CURRENT_DB = " Database: MongoDb";

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> studentsCollection;

    private static MyMongoDatabase singleton;

    private MyMongoDatabase() {
    }

    public static MyMongoDatabase getInstance() {
        if (singleton == null) {
            singleton = new MyMongoDatabase();
        }
        return singleton;
    }

    @Override
    public void establishConnection() {
        this.setMongoClient();
        this.setDatabase();
        this.setStudentsCollection();
        getLogger().info(InfoMessage.DATABASE_CONNECTION_SUCCESS + CURRENT_DB);
    }

    /**
     * 
     * @param host
     *            the hostname for establishing database connection port the port
     *            number for the database connection
     */
    private void setMongoClient() {
        this.mongoClient = new MongoClient(this.getHost(), this.getPort());
    }

    private MongoClient getMongoClient() {
        return this.mongoClient;
    }

    /**
     * @return the {@link MongoDatabase} property
     */
    private MongoDatabase getDatabase() {
        return this.database;
    }

    /**
     * sets the {@link MyMongoDatabase#database} parameter
     */
    private void setDatabase() {
        this.database = this.getMongoClient().getDatabase(this.getName());
    }

    /**
     * initializes a collection of student {@link Document}s and sets the id
     * property as a unique property. This prevents from creating multiple documents
     * in the collection with the same student id. Notice that the unique index is
     * set for the id property of the student object and not the auto-generated
     * "_id" property of the {@link Document}. If the collection does not exist it's
     * being automatically created.
     */
    private void setStudentsCollection() {
        this.studentsCollection = this.getDatabase().getCollection(STUDENT_COLLECTION_NAME);
        Document index = new Document(UNIQUE_IDENTIFIER, 1);
        this.studentsCollection.createIndex(index, new IndexOptions().unique(true));
    }

    /**
     * A MongoDb-specific implementation of the
     * {@link AbstractDatabase#addStudent(Student)} method. Extracts the properties
     * of the {@link Student} object as an array of Fields and then initializes a
     * {@link Document} object to be added to the collection (the MongoDb collection
     * only accepts {@link Document} objects. The method then iterates over the
     * Fields array and assigns the Student's properties to the Document object. The
     * document object is then inserted to the collection using a try-catch block in
     * case a document containing the same student id is already in the database.
     * The {@link MyMongoDatabase#setStudentsCollection} method specifies that the
     * id property must be unique.
     * 
     */
    @Override
    public boolean addStudent(Student student) {
        Field[] fields = Student.class.getDeclaredFields();
        Document newStudent = new Document();
        for (Field field : fields) {
            String fieldName = field.getName();
            field.setAccessible(true);
            try {
                newStudent.append(fieldName, field.get(student));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                getLogger().error(String.format(STUDENT_ADD_FAIL, student.getName(), student.getId()) + CURRENT_DB);
                getLogger().info("Stacktrace:", e);
            }
        }
        try {
            this.studentsCollection.insertOne(newStudent);
            this.registerEvent(new Event(DbEvents.AddStudentSuccessMongo.getMessage(), DbEvents.AddStudentSuccessMongo.getCode(),
                    System.currentTimeMillis()));
            return true;
        } catch (MongoWriteException e) {
            if (e.getMessage().startsWith(DB_DUPLICATE_ENTRY_ATTEMPT_ERROR_ID)) {
                getLogger().error(
                        String.format(STUDEND_ADD_FAIL_DUPLICATE_ID, student.getName(), student.getId()) + CURRENT_DB);
                this.registerEvent(new Event(DbEvents.AddDuplicateStudentFail.getMessage(),
                        DbEvents.AddDuplicateStudentFail.getCode(), System.currentTimeMillis()));
            } else {
                this.registerEvent(new Event(DbEvents.AddStudentFail.getMessage(), DbEvents.AddStudentFail.getCode(),
                        System.currentTimeMillis()));
                getLogger().error(String.format(STUDENT_ADD_FAIL, student.getName(), student.getId()) + CURRENT_DB);
                getLogger().error("Stacktrace:", e);
            }
        }
        return false;
    }

    /**
     * A MongoDb-specific implementation of the
     * {@link AbstractDatabase#deleteStudentById(int)()} method. Searches the database for a
     * document with an id property value equal to the studentId param. If not found
     * the method throws an exception. Else it deletes the document from the database.
     * 
     * @param studentId
     *            the id to find a student in the database
     */
    @Override
    public void deleteStudentById(int studentId) throws StudentNotFoundException {
        Document query = new Document(UNIQUE_IDENTIFIER, studentId);
        query = this.studentsCollection.find(query).first();

        if (query == null) {
            this.registerEvent(new Event(DbEvents.DeleteStudentFail.getMessage(), DbEvents.DeleteStudentFail.getCode(),
                    System.currentTimeMillis()));
            throw new StudentNotFoundException(ErrorMessage.STUDENT_NOT_EXISTS + CURRENT_DB);
        }

        this.studentsCollection.deleteOne(query);
        this.registerEvent(new Event(DbEvents.DeleteStudentSuccessMongo.getMessage(),
                DbEvents.DeleteStudentSuccessMongo.getCode(), System.currentTimeMillis()));
        getLogger().info(String.format(InfoMessage.STUDENT_DELETE_SUCCESS, studentId) + CURRENT_DB);
    }

    /**
     * A MongoDb-specific implementation of the
     * {@link AbstractDatabase#getStudentById()} method. Searches the database for a
     * document with an id property value equal to the studentId param. If not found
     * the method throws an exception. Else it creates a student object with the
     * required properties and returns it.
     * 
     * @param studentId
     *            the id to find a student in the database
     * @return a Student object with id, name, age and grade
     */
    @Override
    public Student getStudentById(int studentId) throws StudentNotFoundException {
        Document query = new Document(UNIQUE_IDENTIFIER, studentId);
        query = this.studentsCollection.find(query).first();

        if (query == null) {
            this.registerEvent(new Event(DbEvents.GetStudentFail.getMessage(), 
                    DbEvents.GetStudentFail.getCode(), 
                    System.currentTimeMillis()));
            throw new StudentNotFoundException(ErrorMessage.STUDENT_NOT_EXISTS + CURRENT_DB);
        }

        this.registerEvent(new Event(DbEvents.GetStudentSuccess.getMessage(), 
                DbEvents.GetStudentSuccess.getCode(), 
                System.currentTimeMillis()));
        return constructStudentObject(query);
    }

    /**
     * Creates a list of the Document's values and constructs a student object with
     * the extracted from the document properties.
     * 
     * @param query
     *            The Document containing the Student's information in a database
     *            format.
     * @return the new Student object
     */
    private Student constructStudentObject(Document query) {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        List list = new ArrayList(query.values());
        int id = (int) list.get(1);
        String name = Objects.toString(list.get(2));
        int age = (int) list.get(3);
        int grade = (int) list.get(4);

        return new Student(id, name, age, grade);
    }

    /**
     * A MongoDb-specific implementation of the
     * {@link AbstractDatabase#getAllStudentsArr()} method. Retrieves the collection
     * of {@link Document}s from the database, adds the values of each document to a
     * list and then assigns each value to the corresponding property of a newly
     * created Student objects. It then adds that object to a List. After adding all
     * the students to the list the method returns an array from that list.
     * 
     * @return a Student[] array
     */
    @Override
    public Student[] getAllStudentsArr() {
        List<Student> studentsList = new ArrayList<>();
        try (MongoCursor<Document> cur = this.studentsCollection.find().iterator()) {
            while (cur.hasNext()) {

                Document doc = cur.next();
                Student student = constructStudentObject(doc);
                studentsList.add(student);
            }
        } catch (MongoException e) {
            this.registerEvent(new Event(DbEvents.GetStudentsFail.getMessage(), 
                    DbEvents.GetStudentsFail.getCode(), 
                    System.currentTimeMillis()));
        }
        getLogger().info(InfoMessage.STUDENT_GET_ALL_SUCCESS + CURRENT_DB);
        this.registerEvent(new Event(DbEvents.GetStudentsSuccess.getMessage(), 
                DbEvents.GetStudentsSuccess.getCode(), 
                System.currentTimeMillis()));
        return studentsList.toArray(new Student[studentsList.size()]);
    }

    /**
     * A MongoDb-specific implementation of the
     * {@link AbstractDatabase#getAllStudentsList()} method. Calls the
     * {@link MyMongoDatabase#getAllStudentsArr()} and casts the returned array to a
     * List<Student> and returns it.
     * 
     * @return a List<Student> all students in a List
     */
    @Override
    public List<Student> getAllStudentsList() {
        return Arrays.asList(this.getAllStudentsArr());
    }

    /**
     * A MongoDb-specific implementation of the
     * {@link AbstractDatabase#addMultipleStudents(List<Student> students)} method.
     * Iterates over the List<Student> parameter and calls the
     * {@link MyMongoDatabase#addStudent(Student)} method for each Student object.
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
        this.registerEvent(new Event(DbEvents.AddMultipleSuccess.getMessage(), 
                DbEvents.AddMultipleSuccess.getCode(), 
                System.currentTimeMillis()));
    }

    /**
     * A MongoDb-specific implementation of the
     * {@link AbstractDatabase#addMultipleStudents(Student[] students)} method.
     * Casts the {@link Student} array to a List and calls the
     * {@link MyMongoDatabase#addMultipleStudents(List)} method with it.
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
}
