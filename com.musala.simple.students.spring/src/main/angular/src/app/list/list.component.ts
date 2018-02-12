import { Component, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { StudentsService } from "../students.service";
import { NgForm, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { Observable, Subject, Subscription } from 'rxjs';

import { ToasterModule, ToasterService, ToasterConfig, Toast } from 'angular2-toaster';
import { CustomToast } from '../toaster.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit, OnDestroy {
  mongoStudents = [];
  mongoStudentsToShow = [];
  mysqlStudents = [];
  mysqlStudentsToShow = [];
  activatedRoute: ActivatedRoute;
  studentsService: StudentsService;
  myForm: FormGroup;
  mongoChangeSubscription: Subscription;
  mysqlChangeSubscription: Subscription;
  private toasterService: ToasterService;
  dbActive = { "mongo": true, "mysql": true };

  public config1: ToasterConfig = new ToasterConfig({
    positionClass: 'toast-top-right'
  });

  constructor(activatedRoute: ActivatedRoute, studentsService: StudentsService, private formBuilder: FormBuilder, toasterService: ToasterService) {
    this.activatedRoute = activatedRoute;
    this.studentsService = studentsService;
    this.toasterService = toasterService;
  }

  mongoStudentsChanged() {
    return this.mongoStudents.length !== this.studentsService.getStudents("mongo").length;
  }

  mysqlStudentsChanged() {
    return this.mysqlStudents.length !== this.studentsService.getStudents("mysql").length;
  }

  onDbChange(): void {
    this.myForm.valueChanges.subscribe(val => {

      if (val.mongo === true && this.dbActive.mongo === false) {
        if (this.mongoStudentsChanged()) {
          this.studentsService.fetchStudents("mongo");
        }
        this.mongoStudentsToShow = this.mongoStudents.slice();
        this.dbActive.mongo = true;
      } else if (val.mongo === false) {
        this.mongoStudentsToShow = [];
        this.dbActive.mongo = false;
      }
      if (val.mysql === true && this.dbActive.mysql === false) {
        if (this.mysqlStudentsChanged()) {
          console.log("fetchvam");
          this.studentsService.fetchStudents("mysql");
        }
        this.mysqlStudentsToShow = this.mysqlStudents.slice();
        this.dbActive.mysql = true;
      } else if (val.mysql === false) {
        this.mysqlStudentsToShow = [];
        this.dbActive.mysql = false;
      }
    
    });
  }

  ngOnInit() {
    this.myForm = this.formBuilder.group({
      mongo: true,
      mysql: true,
    });

    if (this.myForm.value.mongo && this.mongoStudents.length === 0) {
      this.studentsService.fetchStudents("mongo");      
    }
    if (this.myForm.value.mysql && this.mysqlStudents.length === 0) {
      this.studentsService.fetchStudents("mysql");
    }

    this.onDbChange();

    this.activatedRoute.params.subscribe(
      (params) => {
        this.mongoStudents = this.studentsService.getStudents("mongo");
        this.mysqlStudents = this.studentsService.getStudents("mysql");
      }
    );

    this.mongoChangeSubscription = this.studentsService.mongoStudentsChanged.subscribe(
      () => {
        this.mongoStudents = this.studentsService.getStudents("mongo");
        this.mongoStudentsToShow = this.studentsService.getStudents("mongo");
        let toast;
        if (this.mongoStudents.length > 0) {
          toast = new CustomToast().generateToast("info", "Students loaded", "All students from Mongo DB where loaded successfully!");
        } else {
          toast = new CustomToast().generateToast("info", "Database empty", "There are currently no students in Mongo database.");
        }
        this.toasterService.pop(toast);
      }
    );

    this.mysqlChangeSubscription = this.studentsService.mysqlStudentsChanged.subscribe(
      () => {
        this.mysqlStudents = this.studentsService.getStudents("mysql");
        this.mysqlStudentsToShow = this.studentsService.getStudents("mysql");        
        let toast;
        if (this.mysqlStudents.length > 0) {
          toast = new CustomToast().generateToast("info", "Students loaded", "All students from MySql DB where loaded successfully!");
        } else {
          toast = new CustomToast().generateToast("info", "Database empty", "There are currently no students in MySql database.");
        }
        this.toasterService.pop(toast);
      }
    )
  }

  ngOnDestroy() {
    this.mongoChangeSubscription.unsubscribe();
    this.mysqlChangeSubscription.unsubscribe();
  }
}
