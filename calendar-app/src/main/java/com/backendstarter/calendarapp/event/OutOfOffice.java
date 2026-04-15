package com.backendstarter.calendarapp.event;

import com.backendstarter.calendarapp.event.update.AbstractAuditableEvent;
import java.time.ZonedDateTime;

public class OutOfOffice extends AbstractEvent {

    public OutOfOffice(int id, String title, ZonedDateTime startAt,
        ZonedDateTime endAt) {
        super(id, title, startAt, endAt);
    }

    @Override
    protected void update(AbstractAuditableEvent update) {

    }

    @Override
    public void print() {

    }

    @Override
    public boolean support(EventType type) {
        return type == EventType.OUT_OF_OFFICE;
    }
}
