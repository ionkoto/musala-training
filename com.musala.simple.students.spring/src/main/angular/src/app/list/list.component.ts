import { Component, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { StudentsService } from "../students.service";
import { Subscription } from "rxjs/Subscription";
import { NgForm, FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';

import { Observable } from 'rxjs/Rx';
import { Subject } from "rxjs/Subject";

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

  constructor(activatedRoute: ActivatedRoute, studentsService: StudentsService, private formBuilder: FormBuilder) {
    this.activatedRoute = activatedRoute;
    this.studentsService = studentsService;
  }

  onDbChange(): void {
    this.myForm.valueChanges.subscribe(val => {

      if (val.mongo === true) {
        this.studentsService.fetchStudents("mongo");
      } else {
        this.mongoStudents = [];
      }
      if (val.mysql === true) {
        this.studentsService.fetchStudents("mysql");
      } else {
        this.mysqlStudents = [];
      }
    });
  }

  ngOnInit() {
    this.myForm = this.formBuilder.group({
      mongo: false,
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
      }
    );

    this.mysqlChangeSubscription = this.studentsService.mysqlStudentsChanged.subscribe(
      () => {
        this.mysqlStudents = this.studentsService.getStudents("mysql");
      }
    )
  }

  ngOnDestroy() {
    this.mongoChangeSubscription.unsubscribe();
    this.mysqlChangeSubscription.unsubscribe();
  }
}
