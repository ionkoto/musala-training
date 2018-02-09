import { Component, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { StudentsService } from "../students.service";
import { Subscription } from "rxjs/Subscription";
import { NgForm, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';

import { Observable } from 'rxjs/Rx';
import { Subject } from "rxjs/Subject";

import {ToasterModule, ToasterService, ToasterConfig, Toast} from 'angular2-toaster';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit, OnDestroy {
  mongoStudents = [];
  mysqlStudents = [];
  activatedRoute: ActivatedRoute;
  studentsService: StudentsService;
  myForm: FormGroup;
  mongoChangeSubscription: Subscription;
  mysqlChangeSubscription: Subscription;
  private toasterService: ToasterService;
  dbActive = {"mongo" : true, "mysql" : true};

  constructor(activatedRoute: ActivatedRoute, studentsService: StudentsService, private formBuilder: FormBuilder, toasterService: ToasterService ) {
    this.activatedRoute = activatedRoute;
    this.studentsService = studentsService;
    this.toasterService = toasterService;
  }

  public config1 : ToasterConfig = new ToasterConfig({
    positionClass: 'toast-top-right'
  });

  onDbChange(): void {
    this.myForm.valueChanges.subscribe(val => {

      if (val.mongo === true && this.dbActive.mongo === false) {
        this.studentsService.fetchStudents("mongo");
        this.dbActive.mongo = true;
      } else if (val.mongo === false) {
        this.mongoStudents = [];
        this.dbActive.mongo = false;
      } 
      if (val.mysql === true && this.dbActive.mysql === false) {
        this.studentsService.fetchStudents("mysql");
        this.dbActive.mysql = true;
      } else if (val.mysql === false) {
        this.mysqlStudents = [];
        this.dbActive.mysql = false;
      } 
    });
  }

  ngOnInit() {
    this.myForm = this.formBuilder.group({
      mongo: true,
      mysql: true,
    });

    if (this.myForm.value.mongo) {
      this.studentsService.fetchStudents("mongo");
    }
    if (this.myForm.value.mysql) {
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
        this.popToast("info", "Students loaded", "All students from Mongo DB where loaded successfully!");
      }
    );

    this.mysqlChangeSubscription = this.studentsService.mysqlStudentsChanged.subscribe(
      () => {
        this.mysqlStudents = this.studentsService.getStudents("mysql");
        this.popToast("info", "Students loaded", "All students from MySql database where loaded successfully!");        
      }
    )
  }

  popToast(info, title, message) {
    var toast: Toast = {
      type: info,
      title: title,
      body: message
    };
    
    this.toasterService.pop(toast);
  }

  ngOnDestroy() {
    this.mongoChangeSubscription.unsubscribe();
    this.mysqlChangeSubscription.unsubscribe();
  }
}
