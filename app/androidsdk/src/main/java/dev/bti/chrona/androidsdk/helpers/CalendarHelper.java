package dev.bti.chrona.androidsdk.helpers;

import android.content.Context;

import java.util.List;

import dev.bti.chrona.androidsdk.client.CallHandler;
import dev.bti.chrona.androidsdk.client.ProviderApiClient;
import dev.bti.chrona.androidsdk.client.TokenProvider;
import dev.bti.chrona.androidsdk.dto.CalendarDto;
import dev.bti.chrona.androidsdk.dto.CalendarRequestDto;
import dev.bti.chrona.androidsdk.dto.DevicePreferences;
import dev.bti.chrona.androidsdk.service.CalendarService;
import retrofit2.Retrofit;

public class CalendarHelper {

    private final TokenProvider tokenProvider;

    private final CalendarService service;
    private final Retrofit retrofit;

    public CalendarHelper(Context context, TokenProvider tokenProvider, String appVersion) {
        this.tokenProvider = tokenProvider;
        this.service = ProviderApiClient.GetAuthenticatedInstance(CalendarService.class, context, tokenProvider, appVersion);
        this.retrofit = ProviderApiClient.getAuthenticatedRetrofit();
    }

    public CallHandler<CalendarDto> createCalendar(CalendarRequestDto request) {
        return new CallHandler<>(service.createCalendar(request), retrofit);
    }

    public CallHandler<List<CalendarDto>> getMyCalendars() {
        return new CallHandler<>(service.getMyCalendars(), retrofit);
    }

    public CallHandler<CalendarDto> getCalendar(String calendarId) {
        return new CallHandler<>(service.getCalendar(calendarId), retrofit);
    }

    public CallHandler<CalendarDto> updateCalendar(String calendarId, CalendarRequestDto request) {
        return new CallHandler<>(service.updateCalendar(calendarId, request), retrofit);
    }

    public CallHandler<Void> deleteCalendar(String calendarId) {
        return new CallHandler<>(service.deleteCalendar(calendarId), retrofit);
    }

    public CallHandler<Void> shareCalendar(String calendarId, String targetUserId, String role) {
        return new CallHandler<>(service.shareCalendar(calendarId, targetUserId, role), retrofit);
    }

    public CallHandler<Void> updatePreferences(String calendarId, String deviceId, DevicePreferences preferences) {
        return new CallHandler<>(service.updatePreferences(calendarId, deviceId, preferences), retrofit);
    }
}
