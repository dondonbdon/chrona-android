package dev.bti.chrona.androidsdk.service;

import dev.bti.chrona.androidsdk.common.SuccessResponse;
import dev.bti.chrona.androidsdk.dto.SyncStatusDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SyncService {

    @POST("calendars/{calendarId}/sync")
    Call<SuccessResponse<Void>> triggerManualSync(
            @Path("calendarId") String calendarId
    );

    @GET("calendars/{calendarId}/sync/status")
    Call<SuccessResponse<SyncStatusDto>> getSyncStatus(
            @Path("calendarId") String calendarId
    );
}