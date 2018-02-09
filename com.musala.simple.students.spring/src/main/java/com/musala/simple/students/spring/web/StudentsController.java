package com.musala.simple.students.spring.web;

import java.util.List;
import java.util.Properties;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.mongodb.client.MongoDatabase;
import com.musala.simple.students.spring.web.database.DatabaseType;
import com.musala.simple.students.spring.web.helper.FileHelper;
import com.musala.simple.students.spring.web.student.Student;

/**
 * This is a Spring boot controller class annotated with @RestController
 * used for configuring REST-endpoints for interaction between the client
 * and the server/database.
 * 
 * @author yoan.petrushinov
 *
 */
@RestController
public class StudentsController {

    private static final String APP_PROPERTIES_FILE_PATH = "/application.properties";
    private static final String HOST_PROP = "management.address";
    private static final String PORT_PROP = "server.port";
    private static final String MONGO = "mongo";
    private static final String MYSQL = "mysql";

    @PostMapping("students/add/{dbType}")
    @CrossOrigin(origins = { "http://localhost:4200", "http://localhost:1234" })
    public ResponseEntity<?> addStudentAjax(@Valid @RequestBody Student student,
            @PathVariable("dbType") String dbType) {

        boolean studentAddSuccess = false;

        switch (dbType) {
            case MONGO:
                studentAddSuccess = StudentService.addStudent(student, DatabaseType.MongoDb);
                break;
            case MYSQL:
                studentAddSuccess = StudentService.addStudent(student, DatabaseType.MySQL);
                break;
            default:
                break;
        }

        AjaxResponseBody result = new AjaxResponseBody();
        if (!studentAddSuccess) {
            result.setMsg("Student adding failed.");
            return ResponseEntity.badRequest().body(result);
        }
        result.setMsg("success");
        result.setResult(null);

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/students/all/{dbType}")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Student> showAllStudents(@PathVariable("dbType") String dbType) {
        List<Student> studentList = null;
        switch (dbType) {
            case MONGO:
                studentList = StudentService.getStudents(DatabaseType.MongoDb);
                break;
            case MYSQL:
                studentList = StudentService.getStudents(DatabaseType.MySQL);
                break;
            default:
                break;
        }
        return studentList;
    }

    @GetMapping("students/{dbType}/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Student> showStudent(@PathVariable("dbType") String dbType, @PathVariable("id") int id) {
        Student student = null;

        switch (dbType) {
            case MONGO:
                student = StudentService.getStudent(DatabaseType.MongoDb, id);
                break;
            case MYSQL:
                student = StudentService.getStudent(DatabaseType.MySQL, id);
                break;
            default:
                break;
        }

        if (student == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping("students/delete/{id}")
    public RedirectView deleteStudent(@PathVariable int id, Model model) {
        StudentService.deleteStudent(DatabaseType.MongoDb, id);
        Properties hostProperties = FileHelper.readPropertiesFile(APP_PROPERTIES_FILE_PATH);
        String host = hostProperties.getProperty(HOST_PROP);
        String port = hostProperties.getProperty(PORT_PROP);
        String redirectUrl = "http://" + host + ":" + port + "/students/all";
        return new RedirectView(redirectUrl);
    }

    @DeleteMapping("students/delete/{id}/{dbType}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Boolean> deleteStudent(@PathVariable int id, @PathVariable String dbType) {
        boolean success = false;

        switch (dbType) {
            case MONGO:
                success = StudentService.deleteStudent(DatabaseType.MongoDb, id);
                break;
            case MYSQL:
                success = StudentService.deleteStudent(DatabaseType.MySQL, id);
                break;
            default:
                break;
        }

        if (!success) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(success, HttpStatus.OK);
    }
}