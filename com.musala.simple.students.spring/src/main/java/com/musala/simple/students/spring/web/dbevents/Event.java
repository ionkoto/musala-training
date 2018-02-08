package com.musala.simple.students.spring.web.dbevents;

/**
 * This is a template class for creating event-type objects, containing message,
 * code and timestamp (time of event creation).
 * 
 * @author yoan.petrushinov
 *
 */
public class Event {
    private Long timestamp;
    private String code;
    private String message;

    public Event(String message, String code, Long timeStamp) {
        this.setMessage(message);
        this.setCode(code);
        this.setTimestamp(timeStamp);
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    private void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCode() {
        return this.code;
    }

    private void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    private void setMessage(String message) {
        this.message = message;
    }
}
