package dev.bti.chrona.androidsdk.dto

data class AiIntentResponseDto(
    val conversationalResponse: String,
    val actionTaken: String,
    val affectedEvent: EventDto?
)