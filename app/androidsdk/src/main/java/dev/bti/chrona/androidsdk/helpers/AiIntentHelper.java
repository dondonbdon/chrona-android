package dev.bti.chrona.androidsdk.helpers;

import android.content.Context;

import java.time.Instant;
import java.time.ZoneId;

import dev.bti.chrona.androidsdk.client.CallHandler;
import dev.bti.chrona.androidsdk.client.ProviderApiClient;
import dev.bti.chrona.androidsdk.client.TokenProvider;
import dev.bti.chrona.androidsdk.dto.AiIntentRequestDto;
import dev.bti.chrona.androidsdk.dto.AiIntentResponseDto;
import dev.bti.chrona.androidsdk.service.AiIntentService;
import retrofit2.Retrofit;

public class AiIntentHelper {

    private final TokenProvider tokenProvider;

    private final AiIntentService service;
    private final Retrofit retrofit;

    public AiIntentHelper(Context context, TokenProvider tokenProvider, String appVersion) {
        this.tokenProvider = tokenProvider;
        this.service = ProviderApiClient.GetAuthenticatedInstance(AiIntentService.class, context, tokenProvider, appVersion);
        this.retrofit = ProviderApiClient.getAuthenticatedRetrofit();
    }

    public CallHandler<AiIntentResponseDto> sendPromptToAi(String promptText, String targetCalendarId) {
        
        String currentZone = ZoneId.systemDefault().getId();
        Instant currentTime = Instant.now();

        AiIntentRequestDto request = new AiIntentRequestDto(
                promptText,
                targetCalendarId,
                currentZone,
                currentTime
        );

        return new CallHandler<>(service.processAiIntent(request), retrofit);
    }
}