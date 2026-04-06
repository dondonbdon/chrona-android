package dev.bti.chrona.androidsdk.dto;

import dev.bti.chrona.androidsdk.constants.SyncStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class SyncStatusDto {
    private String calendarId;
    private SyncStatus status;
    private Instant lastRun;
    private String errorMessage;
}