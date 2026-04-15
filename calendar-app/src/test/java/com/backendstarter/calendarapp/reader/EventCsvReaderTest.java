package com.backendstarter.calendarapp.reader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.backendstarter.calendarapp.event.Meeting;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventCsvReaderTest {

    private final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    @Mock
    private RawCsvReader rawCsvReader;

    @InjectMocks
    private EventCsvReader sut;

    @Test
    public void reader() throws IOException {
        //given
        String path = "";
        //@InjectMocks 로 대체
        //EventCsvReader sut = new EventCsvReader(rawCsvReader);

        List<String[]> mockData = new ArrayList<>();
        mockData.add(new String[]{"id", "title", "startAt", "endAt", "participants", "meetingRoom",
            "agenda"});
        int mockSize = 5;
        for (int i = 0; i < mockSize; i++) {
            mockData.add(generateMock(i));
        }

        when(rawCsvReader.readAll(path)).thenReturn(mockData);

        //when
        List<Meeting> meetings = sut.readMeetings(path);

        //then
        assertEquals(mockSize, meetings.size());
        assertEquals("title0", meetings.get(0).getTitle());
    }

    private String[] generateMock(int id) {
        String[] mock = new String[8];
        mock[0] = String.valueOf(id);
        mock[1] = "MEETING" + id;
        mock[2] = "title" + id;
        mock[3] = "A,B,C" + id;
        mock[4] = "A1" + id;
        mock[5] = "test" + id;
        mock[6] = of(ZonedDateTime.now().plusHours(id));
        mock[7] = of(ZonedDateTime.now().plusHours(id + 1));

        return mock;
    }

    private static String of(ZonedDateTime zonedDateTime) {
        return zonedDateTime.format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
    }
}