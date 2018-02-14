import { Component } from '@angular/core';
import {TeachersService} from "../../teachers.service";
import { NgForm } from '@angular/forms';
import {Router} from '@angular/router';
import {ToasterModule, ToasterService, ToasterConfig, Toast} from 'angular2-toaster';
import { CustomToast } from '../../toaster.service';

@Component({
  selector: 'app-add-teacher',
  templateUrl: './add-teacher.component.html',
  styleUrls: ['./add-teacher.component.css']
})
export class AddTeacherComponent {
  teacherService: TeachersService;
  private toasterService: ToasterService;

  availableDbs = [
    { display: 'MySql', value: 'mysql'},
    { display: 'MongoDb', value: 'mongo'}
  ];

  constructor(teacherService: TeachersService, private router: Router, toasterService: ToasterService) {
    this.teacherService = teacherService;
    this.toasterService = toasterService;
  }

  public config1 : ToasterConfig = new ToasterConfig({
    positionClass: 'toast-top-right'
  });

  onSubmit(submittedForm : NgForm) {
    if (submittedForm.invalid){
      return;
    }
    const newTeacher = {
      id: submittedForm.value.id,
      name: submittedForm.value.name,
      email: submittedForm.value.email
    }
    const dbType = submittedForm.value.dbType;
    this.teacherService.addTeacher(newTeacher, dbType)
      .subscribe(
        res => {
          submittedForm.resetForm();
          console.log("navigiram");
          this.router.navigateByUrl(`teachers`);
        }, 
        err => {
          const possibleError = "Possible reason: Duplicate Teacher ID.";
          let toast = new CustomToast().generateToast("error", "Teacher add fail", `${err.msg} ${possibleError}`);
          this.toasterService.pop(toast);
        });
  }
}
