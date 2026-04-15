package com.backendstarter.calendarapp.event;

public interface Event {

    void print();

    boolean support(EventType type);
}
