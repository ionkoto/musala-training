import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";

import { StudentListComponent } from "./student/list/student-list.component";
import { TeacherListComponent } from "./teacher/list/teacher-list.component";
import { StudentComponent } from "./student/student.component";
import { TeacherComponent } from "./teacher/teacher.component";
import { AddStudentComponent } from "./student/add-student/add-student.component";
import { AddTeacherComponent } from "./teacher/add-teacher/add-teacher.component";

import { AddStudentModule } from './student/add-student/add-student.module';

const routes = [
  {
    path: 'students', component: StudentListComponent, children: [
      { path: ':id/delete', component: StudentComponent },
      { path: '', redirectTo: 'all', pathMatch: 'full' }
    ]
  },
  {
    path: 'teachers', component: TeacherListComponent, children: [
      { path: '', redirectTo: 'all', pathMatch: 'full' }
    ]
  },
  { path: 'students/:dbType/:id/details', component: StudentComponent },
  { path: 'teachers/:dbType/:id/details', component: TeacherComponent },
  { path: 'add-student', loadChildren: () => AddStudentModule },
  { path: 'add-teacher', component: AddTeacherComponent },
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

export class AppRoutingModule { }
