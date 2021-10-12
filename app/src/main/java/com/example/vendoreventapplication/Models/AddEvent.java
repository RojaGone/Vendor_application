package com.example.vendoreventapplication.Models;

public class AddEvent {
    private String event_name;

    public AddEvent() {
    }

    public AddEvent(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }
}
