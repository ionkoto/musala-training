package com.musala.simple.students.spring.web;

import java.util.ArrayList;
import java.util.List;

import com.musala.simple.students.spring.web.dbevents.Event;
import com.musala.simple.students.spring.web.dbevents.EventLogger;

/**
 * This is a Service class acting as a middle layer between REST endpoints and the EventLogger class,
 * providing method for getting the list of Events from the logger class.
 * 
 * @author yoan.petrushinov
 *
 */
public class EventLoggerService {

    private EventLoggerService() {

    }

    public static List<Event> getLoggerEvents() {
        return new ArrayList<>(EventLogger.getEvents());
    }
}
