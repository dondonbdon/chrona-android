package dev.bti.chrona.androidsdk.dto;

import java.time.Instant;
import java.util.Map;

import dev.bti.chrona.androidsdk.constants.CalendarRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarDto {
    private String id;
    private String ownerId;
    private String name;
    private String description;
    private String color;
    private boolean isDefault;
    private boolean isReadOnly;
    private Instant createdAt;
    private Instant updatedAt;

    private Map<String, CalendarRole> sharedWith;
    private Map<String, DevicePreferences> devicePreferences;

}