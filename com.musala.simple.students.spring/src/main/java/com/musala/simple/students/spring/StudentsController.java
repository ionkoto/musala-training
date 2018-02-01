package com.musala.simple.students.spring;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.musala.simple.students.spring.database.DatabaseType;
import com.musala.simple.students.spring.helper.FileHelper;
import com.musala.simple.students.spring.student.Student;

@Controller
public class StudentsController {

    private static final String APP_PROPERTIES_FILE_PATH = "/application.properties";
    private static final String HOST_PROP = "management.address";
    private static final String PORT_PROP = "server.port";

    @PostMapping("students/add")
    public ResponseEntity<?> addStudentAjax(@Valid @RequestBody Student student, Errors errors) {
        StudentService.addStudent(student, DatabaseType.MongoDb);

        AjaxResponseBody result = new AjaxResponseBody();
        if (errors.hasErrors()) {
            result.setMsg(
                    errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }
        result.setMsg("success");
        result.setResult(null);

        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "students/all")
    public @ResponseBody String showAllStudents() {
        List<Student> studentsList = StudentService.getStudents(DatabaseType.MongoDb);
        StringBuilder sBuilder = new StringBuilder();
        for (Student student : studentsList) {
            sBuilder.append("<div>").append(student.toString())
                    .append(" <a href='/students/delete/" + student.getId() + "'>Delete</a>")
                    .append(" <a href='/students/" + student.getId() + "'>View Details</a>").append("</div>");
        }
        return sBuilder.toString();
    }

    @RequestMapping("students/{id}")
    public @ResponseBody String showStudent(@PathVariable int id, Model model) {
        Student student = StudentService.getStudent(DatabaseType.MongoDb, id);
        if (student == null) {
            return "No student with this id";
        }
        return student.toString();
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
}