package com.musala.simple.students.spring.web;

import java.util.ArrayList;
import java.util.List;

import com.musala.simple.students.spring.web.models.student.Student;

/**
 * This is a template class for constructing an Ajax Response to use in the
 * Spring boot controllers. The template is specifically designed for a response containing
 * a list of Student objects.
 * 
 * @author yoan.petrushinov
 *
 */
public class AjaxResponseBody {

    private String msg;
    private List<Student> result;

    public void setMsg(String msString) {
        this.msg = msString;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setResult(List<Student> studentList) {
        this.result = new ArrayList<>();
    }

    public List<Student> getResult() {
        return this.result;
    }
}