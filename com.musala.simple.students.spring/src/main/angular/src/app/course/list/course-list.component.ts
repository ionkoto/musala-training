import { Component, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { CoursesService } from "../../courses.service";
import { NgForm, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { Observable, Subject, Subscription } from 'rxjs';

import { ToasterModule, ToasterService, ToasterConfig, Toast } from 'angular2-toaster';
import { CustomToast } from '../../toaster.service';

@Component({
  selector: 'app-list',
  templateUrl: './course-list.component.html',
  styleUrls: ['./course-list.component.css']
})
export class CourseListComponent implements OnInit, OnDestroy {
  mongoCourses = [];
  mongoCoursesToShow = [];
  mysqlCourses = [];
  mysqlCoursesToShow = [];
  activatedRoute: ActivatedRoute;
  coursesService: CoursesService;
  myForm: FormGroup;
  mongoChangeSubscription: Subscription;
  mysqlChangeSubscription: Subscription;
  private toasterService: ToasterService;
  dbActive = { "mongo": true, "mysql": true };

  public config1: ToasterConfig = new ToasterConfig({
    positionClass: 'toast-top-right'
  });

  constructor(activatedRoute: ActivatedRoute, coursesService: CoursesService, private formBuilder: FormBuilder, toasterService: ToasterService) {
    this.activatedRoute = activatedRoute;
    this.coursesService = coursesService;
    this.toasterService = toasterService;
  }

  mongoCoursesChanged() {
    return this.mongoCourses.length !== this.coursesService.getCourses("mongo").length;
  }

  mysqlCoursesChanged() {
    return this.mysqlCourses.length !== this.coursesService.getCourses("mysql").length;
  }

  ngOnInit() {
    this.myForm = this.formBuilder.group({
      mongo: false,
      mysql: true,
    });

    if (this.myForm.value.mongo && this.mongoCourses.length === 0) {
      this.coursesService.fetchCourses("mongo");  
    }
    if (this.myForm.value.mysql && this.mysqlCourses.length === 0) {
      this.coursesService.fetchCourses("mysql");
    }

    this.onDbChange();

    this.activatedRoute.params.subscribe(
      (params) => {
        this.mongoCourses = this.coursesService.getCourses("mongo");
        this.mysqlCourses = this.coursesService.getCourses("mysql");
      }
    );

    this.mongoChangeSubscription = this.coursesService.mongoCoursesChanged.subscribe(
      () => {
        this.mongoCourses = this.coursesService.getCourses("mongo");
        this.mongoCoursesToShow = this.coursesService.getCourses("mongo");
        let toast;
        if (this.mongoCourses.length > 0) {
          toast = new CustomToast().generateToast("info", "Courses loaded", "All courses from Mongo DB where loaded successfully!");
        } else {
          toast = new CustomToast().generateToast("info", "Database empty", "There are currently no courses in Mongo database.");
        }
        this.toasterService.pop(toast);
      }
    );

    this.mysqlChangeSubscription = this.coursesService.mysqlCoursesChanged.subscribe(
      () => {
        this.mysqlCourses = this.coursesService.getCourses("mysql");
        this.mysqlCoursesToShow = this.coursesService.getCourses("mysql");        
        let toast;
        if (this.mysqlCourses.length > 0) {
          toast = new CustomToast().generateToast("info", "Courses loaded", "All courses from MySql DB where loaded successfully!");
        } else {
          toast = new CustomToast().generateToast("info", "Database empty", "There are currently no courses in MySql database.");
        }
        this.toasterService.pop(toast);
      }
    )
  }

  onDbChange(): void {
    this.myForm.valueChanges.subscribe(val => {

      if (val.mongo === true && this.dbActive.mongo === false) {
        if (this.mongoCoursesChanged()) {
          this.coursesService.fetchCourses("mongo");
        }
        this.mongoCoursesToShow = this.mongoCourses.slice();
        this.dbActive.mongo = true;
      } else if (val.mongo === false) {
        this.mongoCoursesToShow = [];
        this.dbActive.mongo = false;
      }
      if (val.mysql === true && this.dbActive.mysql === false) {
        if (this.mysqlCoursesChanged()) {
          console.log("fetchvam");
          this.coursesService.fetchCourses("mysql");
        }
        this.mysqlCoursesToShow = this.mysqlCourses.slice();
        this.dbActive.mysql = true;
      } else if (val.mysql === false) {
        this.mysqlCoursesToShow = [];
        this.dbActive.mysql = false;
      }
    
    });
  }

  ngOnDestroy() {
    this.mongoChangeSubscription.unsubscribe();
    this.mysqlChangeSubscription.unsubscribe();
  }
}
