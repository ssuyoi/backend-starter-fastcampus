package com.backendstarter.calendarapp;

import com.backendstarter.calendarapp.event.Meeting;
import com.backendstarter.calendarapp.event.Schedule;
import com.backendstarter.calendarapp.event.update.UpdateMeeting;
import com.backendstarter.calendarapp.reader.EventCsvReader;
import com.backendstarter.calendarapp.reader.RawCsvReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CalendarAppApplication {

    public static void main(String[] args) throws IOException {
        Schedule schedule = new Schedule();

        EventCsvReader csvReader = new EventCsvReader(new RawCsvReader());
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

        meeting.delete(true);
        System.out.println("삭제 후 수정 시도 ...");

        // Exception in thread "main" java.lang.RuntimeException: 이미 삭제된 이벤트는 수정할 수 없습니다.
        meetings.get(0).validateAndUpdate(
            new UpdateMeeting(
                "new title2",
                ZonedDateTime.now(),
                ZonedDateTime.now().plusHours(1),
                null,
                "B",
                "new Agenda2"
            )
        );

        meeting.print();
    }

}
