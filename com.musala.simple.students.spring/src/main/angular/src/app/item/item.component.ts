import {Component, Input, OnInit, TemplateRef} from '@angular/core';
import {StudentsService} from "../students.service";
import { Router } from '@angular/router';

import {Subscription} from "rxjs/Subscription";
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';

@Component({
  selector: '[app-item]',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.css']
})
export class ItemComponent {
  @Input("student") student;
  @Input("dbType") dbType;
  objectKeys = Object.keys;
  modalRef: BsModalRef;
  message: string;
  studentsService: StudentsService;
  router: Router;
  subscription: Subscription;

  constructor(studentsService: StudentsService, router: Router, private modalService: BsModalService) {
    this.studentsService = studentsService;
    this.router = router;
  }

  openModal(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template, {class: 'modal-sm'});
  }

  confirmDelete(id, dbType): void {
    this.studentsService
    .deleteStudentById(id, dbType)
    .subscribe(
      res => {
        this.message = 'Confirmed!';
        this.modalRef.hide();
      },
      err => {
        console.log(err);
        this.message = 'Server Error!';
      }
    );
  }
 
  decline(): void {
    this.message = 'Declined!';
    this.modalRef.hide();
  }

}
