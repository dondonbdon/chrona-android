package dev.bti.chrona.androidsdk.helpers;


import android.content.Context;

import java.util.List;

import dev.bti.chrona.androidsdk.client.CallHandler;
import dev.bti.chrona.androidsdk.client.ProviderApiClient;
import dev.bti.chrona.androidsdk.client.TokenProvider;
import dev.bti.chrona.androidsdk.dto.EventDto;
import dev.bti.chrona.androidsdk.dto.EventRequestDto;
import dev.bti.chrona.androidsdk.service.EventService;
import retrofit2.Retrofit;

public class EventHelper {

    private final TokenProvider tokenProvider;

    private final EventService service;
    private final Retrofit retrofit;

    public EventHelper(Context context, TokenProvider tokenProvider, String appVersion) {
        this.tokenProvider = tokenProvider;
        this.service = ProviderApiClient.GetAuthenticatedInstance(EventService.class, context, tokenProvider, appVersion);
        this.retrofit = ProviderApiClient.getAuthenticatedRetrofit();
    }

    public CallHandler<EventDto> createEvent(String calendarId, EventRequestDto request) {
        return new CallHandler<>(service.createEvent(calendarId, request), retrofit);
    }

    public CallHandler<EventDto> createOverride(String calendarId, String baseEventId, String instanceTimeISO, EventRequestDto request) {
        return new CallHandler<>(service.createOverride(calendarId, baseEventId, instanceTimeISO, request), retrofit);
    }

    public CallHandler<List<EventDto>> getEvents(String calendarId, String startISO, String endISO) {
        return new CallHandler<>(service.getEvents(calendarId, startISO, endISO), retrofit);
    }

    public CallHandler<Void> deleteEvent(String calendarId, String eventId) {
        return new CallHandler<>(service.deleteEvent(calendarId, eventId), retrofit);
    }
}