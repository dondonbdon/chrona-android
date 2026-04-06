package dev.bti.chrona.androidsdk.dto;

import dev.bti.chrona.androidsdk.constants.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {
    private String title;
    private String description;
    private String location;
    private EventStatus status;
    
    private boolean isAllDay;
    private Instant startTime;
    private Instant endTime;
    private String timezone;
    
    private String rrule;
    
    private String baseEventId;
    private Instant recurrenceId; 
}