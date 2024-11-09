package com.example.finalproject;

public class Event {
    private String eventName;
    private String eventDate;
    private String eventTime;
    private String location;
    private String description;

    public Event(String eventName, String eventDate, String eventTime, String location, String description) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.location = location;
        this.description = description;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }
}
