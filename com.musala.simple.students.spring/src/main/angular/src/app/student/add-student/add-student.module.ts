import {NgModule} from "@angular/core";
import {RouterModule} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import { AddStudentComponent } from "./add-student.component";
import { ToasterModule, ToasterService } from 'angular2-toaster';

@NgModule({
  declarations: [
    AddStudentComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild([
      { path: '', component: AddStudentComponent }
    ]),
    FormsModule,
    ToasterModule,
    ToasterModule.forRoot()
  ]
})
export class AddStudentModule {}
