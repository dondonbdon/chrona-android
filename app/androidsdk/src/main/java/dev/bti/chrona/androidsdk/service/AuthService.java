package dev.bti.chrona.androidsdk.service;

import java.util.List;

import dev.bti.chrona.androidsdk.common.SuccessResponse;
import dev.bti.chrona.androidsdk.dto.AuthDto;
import dev.bti.chrona.androidsdk.dto.Credential;
import dev.bti.chrona.androidsdk.dto.CredentialVerificationResult;
import dev.bti.chrona.androidsdk.dto.LoginRequestDto;
import dev.bti.chrona.androidsdk.dto.UserRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {

    @POST("auth/register")
    Call<SuccessResponse<AuthDto>> registerUser(@Body UserRequestDto request);

    @POST("auth/login")
    Call<SuccessResponse<AuthDto>> loginUser(@Body LoginRequestDto request);

    @POST("auth/refresh")
    Call<SuccessResponse<AuthDto>> refreshToken();

    @POST("auth/logout")
    Call<SuccessResponse<Void>> logout();

    @POST("auth/reset-password")
    Call<SuccessResponse<Void>> resetPassword(@Body Credential credential, @Query("newPassword") String newPassword);

    @POST("auth/verify-credentials")
    Call<SuccessResponse<List<CredentialVerificationResult>>> verifyCredentials(@Body List<Credential> credential);
}