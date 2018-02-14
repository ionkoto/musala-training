import { Component, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { TeachersService } from "../../teachers.service";
import { NgForm, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { Observable, Subject, Subscription } from 'rxjs';

import { ToasterModule, ToasterService, ToasterConfig, Toast } from 'angular2-toaster';
import { CustomToast } from '../../toaster.service';

@Component({
  selector: 'app-list',
  templateUrl: './teacher-list.component.html',
  styleUrls: ['./teacher-list.component.css']
})
export class TeacherListComponent implements OnInit, OnDestroy {
  mongoTeachers = [];
  mongoTeachersToShow = [];
  mysqlTeachers = [];
  mysqlTeachersToShow = [];
  activatedRoute: ActivatedRoute;
  teachersService: TeachersService;
  myForm: FormGroup;
  mongoChangeSubscription: Subscription;
  mysqlChangeSubscription: Subscription;
  private toasterService: ToasterService;
  dbActive = { "mongo": true, "mysql": true };

  public config1: ToasterConfig = new ToasterConfig({
    positionClass: 'toast-top-right'
  });

  constructor(activatedRoute: ActivatedRoute, teachersService: TeachersService, private formBuilder: FormBuilder, toasterService: ToasterService) {
    this.activatedRoute = activatedRoute;
    this.teachersService = teachersService;
    this.toasterService = toasterService;
  }

  mongoTeachersChanged() {
    return this.mongoTeachers.length !== this.teachersService.getTeachers("mongo").length;
  }

  mysqlTeachersChanged() {
    return this.mysqlTeachers.length !== this.teachersService.getTeachers("mysql").length;
  }

  ngOnInit() {
    this.myForm = this.formBuilder.group({
      mongo: false,
      mysql: true,
    });

    if (this.myForm.value.mongo && this.mongoTeachers.length === 0) {
      this.teachersService.fetchTeachers("mongo");  
    }
    if (this.myForm.value.mysql && this.mysqlTeachers.length === 0) {
      this.teachersService.fetchTeachers("mysql");
    }

    this.onDbChange();

    this.activatedRoute.params.subscribe(
      (params) => {
        this.mongoTeachers = this.teachersService.getTeachers("mongo");
        this.mysqlTeachers = this.teachersService.getTeachers("mysql");
      }
    );

    this.mongoChangeSubscription = this.teachersService.mongoTeachersChanged.subscribe(
      () => {
        this.mongoTeachers = this.teachersService.getTeachers("mongo");
        this.mongoTeachersToShow = this.teachersService.getTeachers("mongo");
        let toast;
        if (this.mongoTeachers.length > 0) {
          toast = new CustomToast().generateToast("info", "Teachers loaded", "All teachers from Mongo DB where loaded successfully!");
        } else {
          toast = new CustomToast().generateToast("info", "Database empty", "There are currently no teachers in Mongo database.");
        }
        this.toasterService.pop(toast);
      }
    );

    this.mysqlChangeSubscription = this.teachersService.mysqlTeachersChanged.subscribe(
      () => {
        this.mysqlTeachers = this.teachersService.getTeachers("mysql");
        this.mysqlTeachersToShow = this.teachersService.getTeachers("mysql");        
        let toast;
        if (this.mysqlTeachers.length > 0) {
          toast = new CustomToast().generateToast("info", "Teachers loaded", "All teachers from MySql DB where loaded successfully!");
        } else {
          toast = new CustomToast().generateToast("info", "Database empty", "There are currently no teachers in MySql database.");
        }
        this.toasterService.pop(toast);
      }
    )
  }

  onDbChange(): void {
    this.myForm.valueChanges.subscribe(val => {

      if (val.mongo === true && this.dbActive.mongo === false) {
        if (this.mongoTeachersChanged()) {
          this.teachersService.fetchTeachers("mongo");
        }
        this.mongoTeachersToShow = this.mongoTeachers.slice();
        this.dbActive.mongo = true;
      } else if (val.mongo === false) {
        this.mongoTeachersToShow = [];
        this.dbActive.mongo = false;
      }
      if (val.mysql === true && this.dbActive.mysql === false) {
        if (this.mysqlTeachersChanged()) {
          console.log("fetchvam");
          this.teachersService.fetchTeachers("mysql");
        }
        this.mysqlTeachersToShow = this.mysqlTeachers.slice();
        this.dbActive.mysql = true;
      } else if (val.mysql === false) {
        this.mysqlTeachersToShow = [];
        this.dbActive.mysql = false;
      }
    
    });
  }

  ngOnDestroy() {
    this.mongoChangeSubscription.unsubscribe();
    this.mysqlChangeSubscription.unsubscribe();
  }
}
