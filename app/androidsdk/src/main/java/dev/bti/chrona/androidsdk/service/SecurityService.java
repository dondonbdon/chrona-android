package dev.bti.chrona.androidsdk.service;


import dev.bti.chrona.androidsdk.common.SuccessResponse;
import dev.bti.chrona.androidsdk.dto.Credential;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SecurityService {

    @Headers({"Content-Type: application/json"})
    @POST("security/request-reset-code")
    Call<SuccessResponse<Void>> requestResetCode(@Body Credential credential);

    @Headers({"Content-Type: application/json"})
    @POST("security/validate-reset-code")
    Call<SuccessResponse<Void>> validateResetCode(@Body Credential credential, @Query("resetCode") String resetCode);
}

