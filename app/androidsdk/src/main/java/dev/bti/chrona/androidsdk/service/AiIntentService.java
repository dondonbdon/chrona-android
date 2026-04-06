package dev.bti.chrona.androidsdk.service;

import dev.bti.chrona.androidsdk.common.SuccessResponse;
import dev.bti.chrona.androidsdk.dto.AiIntentRequestDto;
import dev.bti.chrona.androidsdk.dto.AiIntentResponseDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AiIntentService {

    @POST("api/v1/ai/intent")
    Call<SuccessResponse<AiIntentResponseDto>> processAiIntent(@Body AiIntentRequestDto request);
}