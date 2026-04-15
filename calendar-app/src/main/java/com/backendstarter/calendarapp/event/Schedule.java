package com.backendstarter.calendarapp.event;

import java.util.ArrayList;
import java.util.List;

public class Schedule {

    private List<AbstractEvent> events = new ArrayList<>();

    public void add(AbstractEvent event) {
        if (hasScheduleConflictWith(event)) {
            return;
        }
        this.events.add(event);
    }

    public void printAll() {
        events.forEach(event -> event.print());
    }

    public void printBy(EventType type) {
        events.stream()
            .filter(event -> event.support(type))
            .forEach(event -> event.print());
    }

    private boolean hasScheduleConflictWith(AbstractEvent event) {
        return events.stream()
            .anyMatch(each ->
                (event.getStartAt().isBefore(each.getEndAt()) && event.getStartAt()
                    .isAfter(each.getStartAt()))
                    || (event.getEndAt().isAfter(each.getStartAt()) && event.getEndAt()
                    .isBefore(each.getEndAt())));
    }

}
