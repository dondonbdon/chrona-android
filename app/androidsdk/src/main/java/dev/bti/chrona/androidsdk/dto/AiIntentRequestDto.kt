package dev.bti.chrona.androidsdk.dto

import java.time.Instant

data class AiIntentRequestDto(
    val prompt: String,
    val targetCalendarId: String,
    val clientTimezone: String,
    val clientCurrentTime: Instant
)