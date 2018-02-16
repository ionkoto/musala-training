import { Injectable } from "@angular/core";
import { Subject } from "rxjs/Subject";
import { Http, Response } from "@angular/http";
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Rx';

@Injectable()
export class CoursesService {
  coursesMongo = [

  ];
  coursesMysql = [

  ];
  course: Object;
  mongoCoursesChanged = new Subject<void>();
  mysqlCoursesChanged = new Subject<void>();
  http: Http;

  constructor(http: Http) {
    this.http = http;
  }

  fetchEvents(): Observable<Object> {
    return this.http.get('http://localhost:1234/events')
      .map((response: Response) => {
        console.log(response.json());
        return response.json();
      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

  fetchCourses(dbType) {
    this.http.get(`http://localhost:1234/courses/all/${dbType}`)
      .map((response: Response) => {    // this map comes from RXJS
        return response.json();
      })
      .subscribe(
      (coursesFromDB) => {
        switch (dbType) {
          case "mongo": {
            this.coursesMongo = coursesFromDB;
            this.mongoCoursesChanged.next();
            break;
          }
          case "mysql": {
            this.coursesMysql = coursesFromDB;
            this.mysqlCoursesChanged.next();
            break;
          }
        }
      });
  }

  addCourse(newCourse, dbType): Observable<Object> {
    return this.http.post(`http://localhost:1234/courses/add/${dbType}`, newCourse)
      .map((response: Response) => {
        switch (dbType) {
          case "mongo": {
            this.coursesMongo.unshift(newCourse);
            this.mongoCoursesChanged.next();
            break;
          }
          case "mysql": {
            this.coursesMysql.unshift(newCourse);
            this.mysqlCoursesChanged.next();
            break;
          }
        }
        return response.json();
      })
      .catch((error: any) => Observable.throw(error.json() || 'Server error'));
  }

  getCourses(dbType) {
    switch (dbType) {
      case "mongo": return this.coursesMongo;
      case "mysql": return this.coursesMysql;
    }
  }

  getCourseById(id, dbType): Observable<Object> {
    return this.http
      .get(`http://localhost:1234/courses/${dbType}/${id}`)
      .map((response: Response) => {
        return response.json();
      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

  getCourseStudents(id, dbType): Observable<Object> {
    return this.http.get(`http://localhost:1234/courses/${dbType}/${id}/students`)
      .map((courseStudents: Response) => {
        return courseStudents.json();
      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

  getCourseTeachers(id, dbType): Observable<Object> {
    return this.http.get(`http://localhost:1234/courses/${dbType}/${id}/teachers`)
      .map((courseTeachers: Response) => {
        return courseTeachers.json();
      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

  deleteCourseById(id, dbType) {
    return this.http
      .delete(`http://localhost:1234/courses/delete/${id}/${dbType}`)
      .map((response: Response) => {    // this map comes from RXJS
        switch (dbType) {
          case "mongo": {
            this.coursesMongo = this.coursesMongo.filter(s => s.id != id);
            console.log("mongo courses UI updated");
            this.mongoCoursesChanged.next();
            break;
          }
          case "mysql": {
            this.coursesMysql = this.coursesMysql.filter(s => s.id != id);
            this.mysqlCoursesChanged.next();
            break;
          }
        }
        return response.json();
      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }
}
