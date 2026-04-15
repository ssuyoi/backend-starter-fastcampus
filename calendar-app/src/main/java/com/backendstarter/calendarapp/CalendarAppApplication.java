package com.backendstarter.calendarapp;

import com.backendstarter.calendarapp.event.AbstractEvent;
import com.backendstarter.calendarapp.event.Event;
import com.backendstarter.calendarapp.event.EventType;
import com.backendstarter.calendarapp.event.Meeting;
import com.backendstarter.calendarapp.event.Schedule;
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
        Schedule schedule = new Schedule();

        HashSet<String> participants = new HashSet<>();
        participants.add("danny.kim");

        Meeting meeting1 = new Meeting(
            1, "meeting1",
            ZonedDateTime.now(), ZonedDateTime.now().plusHours(1),
            participants, "meetingRoomA", "스터디"
        );
        schedule.add(meeting1);

        // 일정 충돌로 인해 등록 x
        Todo todo1 = new Todo(
            2, "todo1",
            ZonedDateTime.now(), ZonedDateTime.now().plusHours(2),
            "할 일 적기"
        );
        schedule.add(todo1);

        // 일정 시작시간보다 일정 종료시간이 빠를 경우 예외 발생
        Todo todo2 = new Todo(
            3, "todo2",
            ZonedDateTime.now().plusHours(5), ZonedDateTime.now().plusHours(4),
            "할 일 적기"
        );
        schedule.add(todo2);

        schedule.printAll();

        schedule.printBy(EventType.MEETING);
    }

}
