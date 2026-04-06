package dev.bti.chrona.androidsdk.dto;

import java.time.Instant;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventDto {
    private String id;
    private String calendarId;
    private String title;
    private String description;
    private String location;
    private String status;
    private boolean isAllDay;
    private Instant startTime;
    private Instant endTime;
    private String timezone;
    private String rrule;
    private List<Instant> exDates;
    private String baseEventId;
    private Instant recurrenceId;
    private Instant updatedAt;

}