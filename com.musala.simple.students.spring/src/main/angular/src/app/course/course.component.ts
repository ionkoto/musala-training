import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { CoursesService } from '../courses.service';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css']
})

export class CourseComponent implements OnInit, OnDestroy {
  course: Object = {};
  isCourseLoaded: boolean = false;
  id: number;
  courseStudents: any = [];
  coursesService: CoursesService;
  activatedRoute: ActivatedRoute;
  subscription: Subscription;
  dbType: String;

  constructor(coursesService: CoursesService, activatedRoute: ActivatedRoute) {
    this.activatedRoute = activatedRoute;
    this.coursesService = coursesService;
  }

  ngOnInit() {
    this.subscription = this.activatedRoute.params.subscribe(params => {
      this.id = parseInt(params['id']);
      this.dbType = params['dbType'];
      this.loadCourseDetails(this.id, this.dbType);
    });
  }

  loadCourseDetails(id, dbType) {
    this.coursesService
      .getCourseById(this.id, dbType)
      .subscribe(
      course => {
        this.course = course;

        this.coursesService
          .getCourseStudents(id, dbType)
          .subscribe(
            students => {
              this.course["students"] = students;
              this.courseStudents = students;
              this.isCourseLoaded = true;
              console.log(this.course);
            },
            err => {
              console.log(err);
            });
      },
      err => {
        console.log(err);
      });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
