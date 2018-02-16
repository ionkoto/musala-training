package com.musala.simple.students.spring.web;

import java.util.ArrayList;
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
import com.musala.simple.students.spring.web.models.course.Course;
import com.musala.simple.students.spring.web.models.student.Student;
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
public class CoursesController {

    private static final String APP_PROPERTIES_FILE_PATH = "/application.properties";
    private static final String HOST_PROP = "management.address";
    private static final String PORT_PROP = "server.port";
    private static final String MONGO = "mongo";
    private static final String MYSQL = "mysql";

    @PostMapping("courses/add/{dbType}")
    @CrossOrigin(origins = { "http://localhost:4200", "http://localhost:1234" })
    public ResponseEntity<?> addCourseAjax(@Valid @RequestBody Course course, @PathVariable("dbType") String dbType) {

        boolean courseAddSuccess = false;
        switch (dbType) {
            case MONGO:
                courseAddSuccess = CourseService.addCourse(course, DatabaseType.MongoDb);
                break;
            case MYSQL:
                courseAddSuccess = CourseService.addCourse(course, DatabaseType.MySQL);
                break;
            default:
                break;
        }

        AjaxResponseBody result = new AjaxResponseBody();
        if (!courseAddSuccess) {
            result.setMsg("Course adding failed.");
            return ResponseEntity.badRequest().body(result);
        }
        result.setMsg("success");
        result.setResult(null);

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/courses/all/{dbType}")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Course> showAllCourses(@PathVariable("dbType") String dbType) {
        List<Course> courseList = null;
        switch (dbType) {
            case MONGO:
                courseList = CourseService.getCourses(DatabaseType.MongoDb);
                break;
            case MYSQL:
                courseList = CourseService.getCourses(DatabaseType.MySQL);
                break;
            default:
                break;
        }
        return courseList;
    }

    @GetMapping("courses/{dbType}/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Course> showCourse(@PathVariable("dbType") String dbType, @PathVariable("id") int id) {
        Course course = null;

        switch (dbType) {
            case MONGO:
                course = CourseService.getCourse(DatabaseType.MongoDb, id);
                break;
            case MYSQL:
                course = CourseService.getCourse(DatabaseType.MySQL, id);
                break;
            default:
                break;
        }

        if (course == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @GetMapping("courses/delete/{id}")
    public RedirectView deleteCourse(@PathVariable int id, Model model) {
        CourseService.deleteCourse(DatabaseType.MongoDb, id);
        Properties hostProperties = FileHelper.readPropertiesFile(APP_PROPERTIES_FILE_PATH);
        String host = hostProperties.getProperty(HOST_PROP);
        String port = hostProperties.getProperty(PORT_PROP);
        String redirectUrl = "http://" + host + ":" + port + "/courses/all";
        return new RedirectView(redirectUrl);
    }

    @DeleteMapping("courses/delete/{id}/{dbType}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Boolean> deleteCourse(@PathVariable int id, @PathVariable String dbType) {
        boolean success = false;

        switch (dbType) {
            case MONGO:
                success = CourseService.deleteCourse(DatabaseType.MongoDb, id);
                break;
            case MYSQL:
                success = CourseService.deleteCourse(DatabaseType.MySQL, id);
                break;
            default:
                break;
        }

        if (!success) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @GetMapping("courses/{dbType}/{id}/students")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Student> showCourseStudents(@PathVariable("dbType") String dbType, @PathVariable("id") int courseId) {
        List<Student> courseStudents = null;

        switch (dbType) {
            case MONGO:
                courseStudents = CourseService.getCourseStudents(DatabaseType.MongoDb, courseId);
                break;
            case MYSQL:
                courseStudents = CourseService.getCourseStudents(DatabaseType.MySQL, courseId);
                break;
            default:
                break;
        }

        if (courseStudents == null) {
            return new ArrayList<>();
        }
        return courseStudents;
    }

    @GetMapping("courses/{dbType}/{id}/teachers")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Teacher> showCourseTeachers(@PathVariable("dbType") String dbType, @PathVariable("id") int courseId) {
        List<Teacher> courseTeachers = null;

        switch (dbType) {
            case MONGO:
                courseTeachers = CourseService.getCourseTeachers(DatabaseType.MongoDb, courseId);
                break;
            case MYSQL:
                courseTeachers = CourseService.getCourseTeachers(DatabaseType.MySQL, courseId);
                break;
            default:
                break;
        }

        if (courseTeachers == null) {
            return new ArrayList<>();
        }
        return courseTeachers;
    }
}