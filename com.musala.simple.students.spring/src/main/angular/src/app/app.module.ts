import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { StudentListComponent } from "./student/list/student-list.component";
import { StudentItemComponent } from "./student/item/student-item.component";
import { StudentsService } from './students.service';
import { TeachersService } from './teachers.service';
import { HeaderComponent } from './header/header.component';
import { HttpModule } from "@angular/http";
import { AppRoutingModule } from "./app-routing.module";
import { StudentComponent } from './student/student.component';
import { TeacherComponent } from './teacher/teacher.component';
import { AddTeacherComponent } from './teacher/add-teacher/add-teacher.component';
import { AddStudentModule } from './student/add-student/add-student.module';
import { ModalModule, ButtonsModule } from 'ngx-bootstrap';
import { ToasterModule, ToasterService } from 'angular2-toaster';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TeacherItemComponent } from './teacher/item/teacher-item.component';
import { TeacherListComponent } from './teacher/list/teacher-list.component';

@NgModule({
  declarations: [
    AppComponent,
    StudentListComponent,
    StudentItemComponent,
    TeacherListComponent,
    TeacherItemComponent,
    HeaderComponent,
    StudentComponent,
    TeacherComponent,
    AddTeacherComponent
  ],
  imports: [
    ModalModule.forRoot(),
    BrowserModule,
    AppRoutingModule,
    HttpModule,
    AddStudentModule,
    FormsModule,
    ReactiveFormsModule,
    ToasterModule,
    ButtonsModule.forRoot(),
    BrowserAnimationsModule,
    ToasterModule.forRoot()
  ],
  providers: [StudentsService, TeachersService],
  bootstrap: [AppComponent]
})
export class AppModule { }
