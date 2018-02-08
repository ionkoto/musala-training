import {NgModule} from "@angular/core";
import {RouterModule} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import { AddStudentComponent } from "./add-student.component";

@NgModule({
  declarations: [
    AddStudentComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild([
      { path: '', component: AddStudentComponent }
    ]),
    FormsModule
  ]
})
export class AddStudentModule {}
