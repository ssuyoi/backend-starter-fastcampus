package com.backendstarter.calendarapp;

import com.backendstarter.calendarapp.event.Meeting;
import com.backendstarter.calendarapp.event.Schedule;
import com.backendstarter.calendarapp.event.update.UpdateMeeting;
import com.backendstarter.calendarapp.reader.EventCsvReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CalendarAppApplication {

    public static void main(String[] args) throws IOException {
        Schedule schedule = new Schedule();

        EventCsvReader csvReader = new EventCsvReader();
        String meetingCsvPath = "/data/meeting.csv";

        List<Meeting> meetings = csvReader.readMeetings(meetingCsvPath);
        meetings.forEach(schedule::add);

        Meeting meeting = meetings.get(0);
        meeting.print();
        System.out.println("=== 수정 후 ===");

        meetings.get(0).validateAndUpdate(
            new UpdateMeeting(
                "new title",
                ZonedDateTime.now(),
                ZonedDateTime.now().plusHours(1),
                null,
                "A",
                "new Agenda"
            )
        );

        meeting.print();
    }

}
