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

import com.musala.simple.students.spring.web.database.DatabaseType;
import com.musala.simple.students.spring.web.helper.FileHelper;
import com.musala.simple.students.spring.web.models.teacher.Teacher;

/**
 * This is a Spring boot controller class annotated with @RestController
 * used for configuring REST-endpoints for interaction between the client
 * and the server/database student entities.
 * 
 * @author yoan.petrushinov
 *
 */
@RestController
public class TeachersController {

    private static final String APP_PROPERTIES_FILE_PATH = "/application.properties";
    private static final String HOST_PROP = "management.address";
    private static final String PORT_PROP = "server.port";
    private static final String MONGO = "mongo";
    private static final String MYSQL = "mysql";

    @PostMapping("teachers/add/{dbType}")
    @CrossOrigin(origins = { "http://localhost:4200", "http://localhost:1234" })
    public ResponseEntity<?> addTeacherAjax(@Valid @RequestBody Teacher teacher,
            @PathVariable("dbType") String dbType) {

        boolean teacherAddSuccess = false;
        switch (dbType) {
            case MONGO:
                teacherAddSuccess = TeacherService.addTeacher(teacher, DatabaseType.MongoDb);
                break;
            case MYSQL:
                teacherAddSuccess = TeacherService.addTeacher(teacher, DatabaseType.MySQL);
                break;
            default:
                break;
        }

        AjaxResponseBody result = new AjaxResponseBody();
        if (!teacherAddSuccess) {
            result.setMsg("Teacher adding failed.");
            return ResponseEntity.badRequest().body(result);
        }
        result.setMsg("success");
        result.setResult(null);

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/teachers/all/{dbType}")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Teacher> showAllTeachers(@PathVariable("dbType") String dbType) {
        List<Teacher> teacherList = null;
        switch (dbType) {
            case MONGO:
                teacherList = TeacherService.getTeachers(DatabaseType.MongoDb);
                break;
            case MYSQL:
                teacherList = TeacherService.getTeachers(DatabaseType.MySQL);
                break;
            default:
                break;
        }
        return teacherList;
    }

    @GetMapping("teachers/{dbType}/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Teacher> showTeacher(@PathVariable("dbType") String dbType, @PathVariable("id") int id) {
        Teacher teacher = null;

        switch (dbType) {
            case MONGO:
                teacher = TeacherService.getTeacher(DatabaseType.MongoDb, id);
                break;
            case MYSQL:
                teacher = TeacherService.getTeacher(DatabaseType.MySQL, id);
                break;
            default:
                break;
        }

        if (teacher == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(teacher, HttpStatus.OK);
    }

    @GetMapping("teachers/delete/{id}")
    public RedirectView deleteTeacher(@PathVariable int id, Model model) {
        TeacherService.deleteTeacher(DatabaseType.MongoDb, id);
        Properties hostProperties = FileHelper.readPropertiesFile(APP_PROPERTIES_FILE_PATH);
        String host = hostProperties.getProperty(HOST_PROP);
        String port = hostProperties.getProperty(PORT_PROP);
        String redirectUrl = "http://" + host + ":" + port + "/teachers/all";
        return new RedirectView(redirectUrl);
    }

    @DeleteMapping("teachers/delete/{id}/{dbType}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Boolean> deleteTeacher(@PathVariable int id, @PathVariable String dbType) {
        boolean success = false;

        switch (dbType) {
            case MONGO:
                success = TeacherService.deleteTeacher(DatabaseType.MongoDb, id);
                break;
            case MYSQL:
                success = TeacherService.deleteTeacher(DatabaseType.MySQL, id);
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