import {NgModule} from "@angular/core";
import { RouterModule } from "@angular/router";

import {ListComponent} from "./list/list.component";
import { StudentComponent } from "./student/student.component";
import { AddStudentComponent } from "./add-student/add-student.component";

import { AddStudentModule } from './add-student/add-student.module';

const routes = [
  { path: 'students', component: ListComponent, children: [
    { path: ':id/delete', component: StudentComponent },
    { path: '', redirectTo: 'all', pathMatch: 'full' }] 
  },
  { path: 'students/:dbType/:id/details', component: StudentComponent },
  { path: 'add-student', loadChildren: () => AddStudentModule },
  { path: '**', redirectTo: '/students' } 
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes) 
  ],
  exports: [
    RouterModule
  ]
})
 
export class AppRoutingModule {}
