import { Component, OnInit } from '@angular/core';
import { StudentsService } from "./students.service";
import { Observable } from 'rxjs/Rx';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'app';
  studentsService: StudentsService;
  events = [];
  lastMysqlChangeTimestamp = (new Date).getTime();
  lastMongoChangeTimestamp = (new Date).getTime();

  constructor(studentsService: StudentsService) {
    this.studentsService = studentsService;
  }

  stopPooling = false;
  public startPooling() {
    var timer = Observable.timer(0, 5000);
    var self = this;
    var obj = timer.subscribe(t => {
      console.log("Fetched events");
      self.getEventsData();
      if (self.stopPooling) { obj.unsubscribe(); }
    });
  }

  ngOnInit() {
    this.startPooling();
    this.getEventsData();
  }

  private getEventsData() {
    var self = this;
    this.studentsService.fetchEvents()
      .subscribe((data: Response) => {
        
        let incomingEvents = []
        for (let index in data) {
          incomingEvents.push(data[index]);
        }
        
        // check for local events
        if (this.events.length === 0) {
          this.events = incomingEvents.slice();
        }

        // check for new events
        const lastEventIndex = this.events.length - 1;
        const lastEventIndexIncoming = incomingEvents.length - 1;
        const addDeleteEventCode = 3;
        const addDeleteCodeMongo = 2;
        const addDeleteCodeMysql = 1;

        if (this.events[lastEventIndex]["timestamp"] !== incomingEvents[lastEventIndexIncoming]["timestamp"]) {

          for (let event of incomingEvents) {
            // added or deleted student
            if (event["code"][0] == addDeleteEventCode) {
              const dbChanged = event["code"][4];
              if (dbChanged == addDeleteCodeMongo) {
                // mongoDb change
                if (this.lastMongoChangeTimestamp < parseInt(event["timestamp"])) {
                  this.lastMongoChangeTimestamp = event["timestamp"];
                  this.studentsService.fetchStudents("mongo");
                }

              } else if (dbChanged == addDeleteCodeMysql) {
                //mysqldb change
                if (this.lastMysqlChangeTimestamp < parseInt(event["timestamp"])) {
                  this.lastMysqlChangeTimestamp = event["timestamp"];
                  this.studentsService.fetchStudents("mysql");
                }
              }
            }
          }
          // update events;
          this.events = incomingEvents;
        }
      }, (error) => {
        self.stopPooling = true;
      });
  }
}
