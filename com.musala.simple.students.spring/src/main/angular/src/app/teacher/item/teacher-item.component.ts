import { Component, Input, OnInit, TemplateRef } from '@angular/core';
import { TeachersService } from "../../teachers.service";
import { Router } from '@angular/router';

import { Subscription } from "rxjs/Subscription";
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: '[teacher-item]',
  templateUrl: './teacher-item.component.html',
  styleUrls: ['./teacher-item.component.css']
})
export class TeacherItemComponent {
  @Input("teacher") teacher;
  @Input("dbType") dbType;
  objectKeys = Object.keys;
  modalRef: BsModalRef;
  message: string;
  teachersService: TeachersService;
  router: Router;
  subscription: Subscription;

  constructor(teachersService: TeachersService, router: Router, private modalService: BsModalService) {
    this.teachersService = teachersService;
    this.router = router;
  }

  openModal(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template, { class: 'modal-sm' });
  }

  confirmDelete(id, dbType): void {
    this.teachersService
      .deleteTeacherById(id, dbType)
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
