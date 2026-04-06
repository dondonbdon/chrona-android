package dev.bti.chrona.androidsdk.service;

import java.util.List;

import dev.bti.chrona.androidsdk.common.SuccessResponse;
import dev.bti.chrona.androidsdk.dto.EventDto;
import dev.bti.chrona.androidsdk.dto.EventRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventService {

    @POST("calendars/{calendarId}/events")
    Call<SuccessResponse<EventDto>> createEvent(

            @Path("calendarId") String calendarId,
            @Body EventRequestDto request
    );

    @POST("calendars/{calendarId}/events/{baseEventId}/override")
    Call<SuccessResponse<EventDto>> createOverride(

            @Path("calendarId") String calendarId,
            @Path("baseEventId") String baseEventId,
            @Query("instanceTime") String instanceTime,
            @Body EventRequestDto request
    );

    @GET("calendars/{calendarId}/events")
    Call<SuccessResponse<List<EventDto>>> getEvents(

            @Path("calendarId") String calendarId,
            @Query("start") String startISO,
            @Query("end") String endISO
    );

    @DELETE("calendars/{calendarId}/events/{eventId}")
    Call<SuccessResponse<Void>> deleteEvent(

            @Path("calendarId") String calendarId,
            @Path("eventId") String eventId
    );
}