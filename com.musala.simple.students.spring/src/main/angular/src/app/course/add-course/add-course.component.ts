import { Component } from '@angular/core';
import {CoursesService} from "../../courses.service";
import { NgForm } from '@angular/forms';
import {Router} from '@angular/router';
import {ToasterModule, ToasterService, ToasterConfig, Toast} from 'angular2-toaster';
import { CustomToast } from '../../toaster.service';

@Component({
  selector: 'app-add-course',
  templateUrl: './add-course.component.html',
  styleUrls: ['./add-course.component.css']
})
export class AddCourseComponent {
  courseService: CoursesService;
  private toasterService: ToasterService;

  availableDbs = [
    { display: 'MySql', value: 'mysql'},
    { display: 'MongoDb', value: 'mongo'}
  ];

  constructor(courseService: CoursesService, private router: Router, toasterService: ToasterService) {
    this.courseService = courseService;
    this.toasterService = toasterService;
  }

  public config1 : ToasterConfig = new ToasterConfig({
    positionClass: 'toast-top-right'
  });

  onSubmit(submittedForm : NgForm) {
    if (submittedForm.invalid){
      return;
    }
    const newCourse = {
      id: submittedForm.value.id,
      name: submittedForm.value.name,
      email: submittedForm.value.email
    }
    const dbType = submittedForm.value.dbType;
    this.courseService.addCourse(newCourse, dbType)
      .subscribe(
        res => {
          submittedForm.resetForm();
          console.log("navigiram");
          this.router.navigateByUrl(`courses`);
        }, 
        err => {
          const possibleError = "Possible reason: Duplicate Course ID.";
          let toast = new CustomToast().generateToast("error", "Course add fail", `${err.msg} ${possibleError}`);
          this.toasterService.pop(toast);
        });
  }
}
