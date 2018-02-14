import { Injectable } from "@angular/core";
import { Subject } from "rxjs/Subject";
import { Http, Response } from "@angular/http";
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Rx';

@Injectable()
export class TeachersService {
  teachersMongo = [

  ];
  teachersMysql = [

  ];
  teacher: Object;
  mongoTeachersChanged = new Subject<void>();
  mysqlTeachersChanged = new Subject<void>();
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

  fetchTeachers(dbType) {
    this.http.get(`http://localhost:1234/teachers/all/${dbType}`)
      .map((response: Response) => {    // this map comes from RXJS
        return response.json();
      })
      .subscribe(
      (teachersFromDB) => {
        switch (dbType) {
          case "mongo": {
            this.teachersMongo = teachersFromDB;
            this.mongoTeachersChanged.next();
            break;
          }
          case "mysql": {
            this.teachersMysql = teachersFromDB;
            this.mysqlTeachersChanged.next();
            break;
          }
        }
      });
  }

  addTeacher(newTeacher, dbType): Observable<Object> {
    return this.http.post(`http://localhost:1234/teachers/add/${dbType}`, newTeacher)
      .map((response: Response) => {
        switch (dbType) {
          case "mongo": {
            this.teachersMongo.unshift(newTeacher);
            this.mongoTeachersChanged.next();
            break;
          }
          case "mysql": {
            this.teachersMysql.unshift(newTeacher);
            this.mysqlTeachersChanged.next();
            break;
          }
        }
        return response.json();
      })
      .catch((error: any) => Observable.throw(error.json() || 'Server error'));
  }

  getTeachers(dbType) {
    switch (dbType) {
      case "mongo": return this.teachersMongo;
      case "mysql": return this.teachersMysql;
    }
  }

  getTeacherById(id, dbType): Observable<Object> {
    return this.http
      .get(`http://localhost:1234/teachers/${dbType}/${id}`)
      .map((response: Response) => {    // this map comes from RXJS
        return response.json();
      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

  deleteTeacherById(id, dbType) {
    return this.http
      .delete(`http://localhost:1234/teachers/delete/${id}/${dbType}`)
      .map((response: Response) => {    // this map comes from RXJS
        switch (dbType) {
          case "mongo": {
            this.teachersMongo = this.teachersMongo.filter(s => s.id != id);
            console.log("mongo teachers UI updated");
            this.mongoTeachersChanged.next();
            break;
          }
          case "mysql": {
            this.teachersMysql = this.teachersMysql.filter(s => s.id != id);
            this.mysqlTeachersChanged.next();
            break;
          }
        }
        return response.json();
      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }
}
