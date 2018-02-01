package com.musala.simple.students.spring;

import java.util.ArrayList;
import java.util.List;

import com.musala.simple.students.spring.student.Student;

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