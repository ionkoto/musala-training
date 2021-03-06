import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { ListComponent } from './list/list.component';
import { ItemComponent } from './item/item.component';
import { StudentsService } from './students.service';
import { HeaderComponent } from './header/header.component';
import {HttpModule} from "@angular/http";
import {AppRoutingModule} from "./app-routing.module";
import { StudentComponent } from './student/student.component';
import { AddStudentModule } from './add-student/add-student.module';
import { ModalModule, ButtonsModule } from 'ngx-bootstrap';
import { ToasterModule, ToasterService } from 'angular2-toaster';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

@NgModule({
  declarations: [
    AppComponent,
    ListComponent,
    ItemComponent,
    HeaderComponent,
    StudentComponent
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
  providers: [StudentsService],
  bootstrap: [AppComponent]
})
export class AppModule { }
