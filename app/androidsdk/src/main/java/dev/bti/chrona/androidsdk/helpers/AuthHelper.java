package dev.bti.chrona.androidsdk.helpers;


import android.content.Context;

import java.util.List;

import dev.bti.chrona.androidsdk.client.CallHandler;
import dev.bti.chrona.androidsdk.client.ProviderApiClient;
import dev.bti.chrona.androidsdk.client.TokenProvider;
import dev.bti.chrona.androidsdk.dto.AuthDto;
import dev.bti.chrona.androidsdk.dto.Credential;
import dev.bti.chrona.androidsdk.dto.CredentialVerificationResult;
import dev.bti.chrona.androidsdk.dto.LoginRequestDto;
import dev.bti.chrona.androidsdk.dto.UserRequestDto;
import dev.bti.chrona.androidsdk.service.AuthService;
import retrofit2.Retrofit;

public class AuthHelper {

    private final AuthService publicService;
    private final AuthService refreshService;

    private final Retrofit publicRetrofit;
    private final Retrofit refreshRetrofit;

    public AuthHelper(Context context, TokenProvider tokenProvider, String appVersion) {
        this.publicService = ProviderApiClient.GetPublicInstance(AuthService.class, context, appVersion);
        this.refreshService = ProviderApiClient.GetRefreshInstance(AuthService.class, context, tokenProvider, appVersion);

        this.publicRetrofit = ProviderApiClient.getPublicRetrofit();
        this.refreshRetrofit = ProviderApiClient.getRefreshRetrofit();
    }
    public CallHandler<AuthDto> register(UserRequestDto request) {
        return new CallHandler<>(publicService.registerUser(request), publicRetrofit);
    }

    public CallHandler<AuthDto> login(Credential credential, String password) {
        return new CallHandler<>(publicService.loginUser(new LoginRequestDto(credential.getCredential(), password, credential.getCredentialType())), publicRetrofit);
    }

    public CallHandler<AuthDto> refreshToken() {
        return new CallHandler<>(refreshService.refreshToken(), refreshRetrofit);
    }

    public CallHandler<Void> logout() {
        return new CallHandler<>(refreshService.logout(), refreshRetrofit);
    }

    public CallHandler<Void> resetPassword(Credential credential, String newPassword) {
        return new CallHandler<>(publicService.resetPassword(credential, newPassword), publicRetrofit);
    }

    public CallHandler<List<CredentialVerificationResult>> verifyCredentials(List<Credential> credentials) {
        return new CallHandler<>(publicService.verifyCredentials(credentials), publicRetrofit);
    }
}