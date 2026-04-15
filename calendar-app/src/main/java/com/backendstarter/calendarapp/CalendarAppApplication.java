package com.backendstarter.calendarapp;

import com.backendstarter.calendarapp.event.AbstractEvent;
import com.backendstarter.calendarapp.event.Event;
import com.backendstarter.calendarapp.event.EventType;
import com.backendstarter.calendarapp.event.Meeting;
import com.backendstarter.calendarapp.event.Todo;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CalendarAppApplication {

    public static void main(String[] args) {
        List<AbstractEvent> list = new ArrayList<>();

        HashSet<String> participants = new HashSet<>();
        participants.add("danny.kim");

        Meeting meeting1 = new Meeting(
            1, "meeting1",
            ZonedDateTime.now(), ZonedDateTime.now().plusHours(1),
            participants, "meetingRoomA", "스터디"
        );
        list.add(meeting1);

        Todo todo1 = new Todo(
            2, "todo1",
            ZonedDateTime.now(), ZonedDateTime.now().plusHours(2),
            "할 일 적기"
        );
        list.add(todo1);

        list.forEach(Event::print);

        list.stream()
            .filter(each -> each.support(EventType.MEETING))
            .forEach(Event::print);
    }

}
