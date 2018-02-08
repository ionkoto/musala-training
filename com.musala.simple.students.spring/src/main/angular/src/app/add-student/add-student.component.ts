import { Component, OnInit } from '@angular/core';
import {StudentsService} from "../students.service";
import { NgForm } from '@angular/forms';
import {Router} from '@angular/router';

@Component({
  selector: 'app-add-student',
  templateUrl: './add-student.component.html',
  styleUrls: ['./add-student.component.css']
})
export class AddStudentComponent implements OnInit {
  studentService: StudentsService;
  availableDbs = [
    { display: 'MySql', value: 'mysql'},
    { display: 'MongoDb', value: 'mongo'}
  ];

  onSubmit(submittedForm : NgForm) {
    if (submittedForm.invalid){
      return;
    }
    const newStudent = {
      id: submittedForm.value.id,
      name: submittedForm.value.name,
      age: submittedForm.value.age,
      grade: submittedForm.value.grade
    }
    const dbType = submittedForm.value.dbType;
    this.studentService.addStudent(newStudent, dbType)
      .subscribe(
        res => {
          submittedForm.resetForm();
          this.router.navigateByUrl(`students/all`);
        }, 
        err => {
          console.log(err);
        });
  }
  constructor(studentService: StudentsService, private router: Router) {
    this.studentService = studentService;
  }

  ngOnInit() {
  }
}
