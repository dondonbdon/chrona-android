package dev.bti.chrona.androidsdk.service;

import java.util.List;

import dev.bti.chrona.androidsdk.common.SuccessResponse;
import dev.bti.chrona.androidsdk.dto.CalendarDto;
import dev.bti.chrona.androidsdk.dto.CalendarRequestDto;
import dev.bti.chrona.androidsdk.dto.DevicePreferences;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface CalendarService {

    @POST("calendars")
    Call<SuccessResponse<CalendarDto>> createCalendar(

            @Body CalendarRequestDto request);

    @GET("calendars")
    Call<SuccessResponse<List<CalendarDto>>> getMyCalendars();

    @GET("calendars/{calendarId}")
    Call<SuccessResponse<CalendarDto>> getCalendar(
            @Path("calendarId") String calendarId);

    @PUT("calendars/{calendarId}")
    Call<SuccessResponse<CalendarDto>> updateCalendar(
            @Path("calendarId") String calendarId, @Body CalendarRequestDto request);

    @DELETE("calendars/{calendarId}")
    Call<SuccessResponse<Void>> deleteCalendar(
            @Path("calendarId") String calendarId);

    @PUT("calendars/{calendarId}/share/{targetUserId}")
    Call<SuccessResponse<Void>> shareCalendar(
            @Path("calendarId") String calendarId, @Path("targetUserId") String targetUserId, @Query("role") String role);

    @PUT("calendars/{calendarId}/preferences/{deviceId}")
    Call<SuccessResponse<Void>> updatePreferences(
            @Path("calendarId") String calendarId, @Path("deviceId") String deviceId, @Body DevicePreferences preferences);
}