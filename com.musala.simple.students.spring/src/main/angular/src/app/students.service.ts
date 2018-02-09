import { Injectable } from "@angular/core";
import { Subject } from "rxjs/Subject";
import { Http, Response } from "@angular/http";
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Rx';

@Injectable()
export class StudentsService {
  studentsMongo = [

  ];
  studentsMysql = [

  ];
  student: Object;
  mongoStudentsChanged = new Subject<void>();
  mysqlStudentsChanged = new Subject<void>();
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

  fetchStudents(dbType) {
    this.http.get(`http://localhost:1234/students/all/${dbType}`)
      .map((response: Response) => {    // this map comes from RXJS
        return response.json();
      })
      .subscribe(
      (studentsFromDB) => {
        switch (dbType) {
          case "mongo": {
            this.studentsMongo = studentsFromDB;
            this.mongoStudentsChanged.next();
            break;
          }
          case "mysql": {
            this.studentsMysql = studentsFromDB;
            this.mysqlStudentsChanged.next();
            break;
          }
        }
  });
}

addStudent(newStudent, dbType) : Observable < Object > {
  return this.http.post(`http://localhost:1234/students/add/${dbType}`, newStudent)
    .map((response: Response) => {
      switch (dbType) {
        case "mongo": {
          this.studentsMongo.unshift(newStudent);
          this.mongoStudentsChanged.next();
          break;
        }
        case "mysql": {
          this.studentsMysql.unshift(newStudent);
          this.mysqlStudentsChanged.next();
          break;
        }
      }
      return response.json();
    })
    .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
}

getStudents(dbType) {
  switch (dbType) {
    case "mongo": return this.studentsMongo;
    case "mysql": return this.studentsMysql;
  }
}

getStudentById(id, dbType) : Observable < Object > {
  return this.http
    .get(`http://localhost:1234/students/${dbType}/${id}`)
    .map((response: Response) => {    // this map comes from RXJS
      return response.json();
    })
    .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
}

deleteStudentById(id, dbType) {
  return this.http
    .delete(`http://localhost:1234/students/delete/${id}/${dbType}`)
    .map((response: Response) => {    // this map comes from RXJS
      switch (dbType) {
        case "mongo": {
          this.studentsMongo = this.studentsMongo.filter(s => s.id != id);
          console.log("mongo students UI updated");
          this.mongoStudentsChanged.next();
          break;
        }
        case "mysql": {
          this.studentsMysql = this.studentsMysql.filter(s => s.id != id);
          this.mysqlStudentsChanged.next();
          break;
        }
      }
      return response.json();
    })
    .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
}
}
