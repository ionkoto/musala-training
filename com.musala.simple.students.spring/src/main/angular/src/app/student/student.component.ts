import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { StudentsService } from '../students.service';

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})

export class StudentComponent implements OnInit, OnDestroy {
  student: Object = {};
  isStudentLoaded: boolean = false;
  id: number;
  studentsService: StudentsService;
  activatedRoute: ActivatedRoute;
  subscription: Subscription;
  dbType: String;

  constructor(studentsService: StudentsService, activatedRoute: ActivatedRoute) {
    this.activatedRoute = activatedRoute;
    this.studentsService = studentsService;
  }

  ngOnInit() {
    this.subscription = this.activatedRoute.params.subscribe(params => {
      this.id = +params['id'];
      this.dbType = params['dbType'];
      this.loadStudentDetails(this.id, this.dbType);
    });
  }

  loadStudentDetails(id, dbType) {
    this.studentsService
      .getStudentById(this.id, dbType)
      .subscribe(
      student => {
        this.student = student;
        this.isStudentLoaded = true;
      },
      err => {
        console.log(err);
      }
      );
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
