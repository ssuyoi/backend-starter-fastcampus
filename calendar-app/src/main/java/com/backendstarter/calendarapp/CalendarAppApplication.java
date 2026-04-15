package com.backendstarter.calendarapp;

import com.backendstarter.calendarapp.event.AbstractEvent;
import com.backendstarter.calendarapp.event.Event;
import com.backendstarter.calendarapp.event.EventType;
import com.backendstarter.calendarapp.event.Meeting;
import com.backendstarter.calendarapp.event.Schedule;
import com.backendstarter.calendarapp.event.Todo;
import com.backendstarter.calendarapp.reader.EventCsvReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CalendarAppApplication {

    public static void main(String[] args) throws IOException {
        Schedule schedule = new Schedule();

        EventCsvReader csvReader = new EventCsvReader();
        String meetingCsvPath = "/data/meeting.csv";

        List<Meeting> meetings = csvReader.readMeetings(meetingCsvPath);
        meetings.forEach(schedule::add);

        schedule.printAll();
    }

}
