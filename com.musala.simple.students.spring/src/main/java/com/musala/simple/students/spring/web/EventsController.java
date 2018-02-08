package com.musala.simple.students.spring.web;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.musala.simple.students.spring.web.dbevents.Event;

@RestController
public class EventsController {

    @GetMapping(value = "/events")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Event> getAllEvents() throws JsonProcessingException {
        List<Event> events = EventLoggerService.getLoggerEvents();

        return events;
    }
}