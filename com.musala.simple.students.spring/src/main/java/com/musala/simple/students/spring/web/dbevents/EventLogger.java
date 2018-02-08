package com.musala.simple.students.spring.web.dbevents;

import java.util.Queue;

import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 * The EventLogger class is used to store Event objects. The objects
 * are stored in a CircularFifoQueue, which is a specific queue implementation
 * which only stores 5 elements. When the queue is full (contains 5 elements) on each
 * new addition the first element in the queue (the "oldest") gets removed and the new
 * element is added at the end of the queue.
 * 
 * @author yoan.petrushinov
 *
 */
public class EventLogger {

    private EventLogger() {

    }

    private static Queue<Event> events = new CircularFifoQueue<>(5);

    public static void addEvent(Event event) {
        events.add(event);
    }

    public static Queue<Event> getEvents() {
        return events;
    }
}
