package dev.bti.chrona.androidsdk.dto;

import dev.bti.chrona.androidsdk.constants.CalendarSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarRequestDto {
    private String name;
    private String description;
    private String color;
    private boolean isDefault;
    private CalendarSource source;
    private String externalId;
    private String sourceUrl;
    private boolean isReadOnly;
}