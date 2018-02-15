import { Component, Input, OnInit, TemplateRef } from '@angular/core';
import { CoursesService } from "../../courses.service";
import { Router } from '@angular/router';

import { Subscription } from "rxjs/Subscription";
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: '[course-item]',
  templateUrl: './course-item.component.html',
  styleUrls: ['./course-item.component.css']
})
export class CourseItemComponent {
  @Input("course") course;
  @Input("dbType") dbType;
  objectKeys = Object.keys;
  modalRef: BsModalRef;
  message: string;
  coursesService: CoursesService;
  router: Router;
  subscription: Subscription;

  constructor(coursesService: CoursesService, router: Router, private modalService: BsModalService) {
    this.coursesService = coursesService;
    this.router = router;
  }

  openModal(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template, { class: 'modal-sm' });
  }

  confirmDelete(id, dbType): void {
    this.coursesService
      .deleteCourseById(id, dbType)
      .subscribe(
      res => {
        this.message = 'Confirmed!';
        this.modalRef.hide();
      },
      err => {
        console.log(err);
        this.message = 'Server Error!';
      });
  }

  decline(): void {
    this.message = 'Declined!';
    this.modalRef.hide();
  }
}
