package com.musala.simple.students.spring.web;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musala.simple.students.spring.web.dbevents.Event;

@RestController
public class EventsController {
    private static final String ANGULAR_HOST = "http://localhost:4200";

    @GetMapping(value = "/events")
    @CrossOrigin(origins = ANGULAR_HOST)
    public List<Event> getAllEvents() {
        return EventLoggerService.getLoggerEvents();
    }
}