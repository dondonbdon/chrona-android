package dev.bti.chrona.androidsdk.helpers;


import android.content.Context;

import dev.bti.chrona.androidsdk.client.CallHandler;
import dev.bti.chrona.androidsdk.client.ProviderApiClient;
import dev.bti.chrona.androidsdk.client.TokenProvider;
import dev.bti.chrona.androidsdk.dto.SyncStatusDto;
import dev.bti.chrona.androidsdk.service.SyncService;
import retrofit2.Retrofit;

public class SyncHelper {

    private final TokenProvider tokenProvider;

    private final SyncService service;
    private final Retrofit retrofit;

    public SyncHelper(Context context, TokenProvider tokenProvider, String appVersion) {
        this.tokenProvider = tokenProvider;
        this.service = ProviderApiClient.GetAuthenticatedInstance(SyncService.class, context, tokenProvider, appVersion);
        this.retrofit = ProviderApiClient.getAuthenticatedRetrofit();
    }

    public CallHandler<Void> triggerManualSync(String calendarId) {
        return new CallHandler<>(service.triggerManualSync(calendarId), retrofit);
    }

    public CallHandler<SyncStatusDto> getSyncStatus(String calendarId) {
        return new CallHandler<>(service.getSyncStatus(calendarId), retrofit);
    }
}