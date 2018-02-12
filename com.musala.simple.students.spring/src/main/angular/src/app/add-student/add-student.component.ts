import { Component } from '@angular/core';
import { StudentsService } from "../students.service";
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { ToasterModule, ToasterService, ToasterConfig, Toast } from 'angular2-toaster';
import { CustomToast } from '../toaster.service';

@Component({
  selector: 'app-add-student',
  templateUrl: './add-student.component.html',
  styleUrls: ['./add-student.component.css']
})
export class AddStudentComponent {
  studentService: StudentsService;
  private toasterService: ToasterService;

  availableDbs = [
    { display: 'MySql', value: 'mysql' },
    { display: 'MongoDb', value: 'mongo' }
  ];

  constructor(studentService: StudentsService, private router: Router, toasterService: ToasterService) {
    this.studentService = studentService;
    this.toasterService = toasterService;
  }

  public config1: ToasterConfig = new ToasterConfig({
    positionClass: 'toast-top-right'
  });

  onSubmit(submittedForm: NgForm) {
    if (submittedForm.invalid) {
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
        const possibleError = "Possible reason: Duplicate student ID.";
        let toast = new CustomToast().generateToast("error", "Student add fail", `${err.msg} ${possibleError}`);
        this.toasterService.pop(toast);
      });
  }
}
