import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { TeachersService } from '../teachers.service';

@Component({
  selector: 'app-teacher',
  templateUrl: './teacher.component.html',
  styleUrls: ['./teacher.component.css']
})

export class TeacherComponent implements OnInit, OnDestroy {
  teacher: Object = {};
  isTeacherLoaded: boolean = false;
  id: number;
  teachersService: TeachersService;
  activatedRoute: ActivatedRoute;
  subscription: Subscription;
  dbType: String;

  constructor(teachersService: TeachersService, activatedRoute: ActivatedRoute) {
    this.activatedRoute = activatedRoute;
    this.teachersService = teachersService;
  }

  ngOnInit() {
    this.subscription = this.activatedRoute.params.subscribe(params => {
      this.id = parseInt(params['id']);
      this.dbType = params['dbType'];
      this.loadTeacherDetails(this.id, this.dbType);
    });
  }

  loadTeacherDetails(id, dbType) {
    this.teachersService
      .getTeacherById(this.id, dbType)
      .subscribe(
      teacher => {
        this.teacher = teacher;
        this.isTeacherLoaded = true;
      },
      err => {
        console.log(err);
      });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
