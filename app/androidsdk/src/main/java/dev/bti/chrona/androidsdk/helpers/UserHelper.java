package dev.bti.chrona.androidsdk.helpers;


import android.content.Context;

import dev.bti.chrona.androidsdk.client.CallHandler;
import dev.bti.chrona.androidsdk.client.ProviderApiClient;
import dev.bti.chrona.androidsdk.client.TokenProvider;
import dev.bti.chrona.androidsdk.dto.UserDto;
import dev.bti.chrona.androidsdk.dto.UserRequestDto;
import dev.bti.chrona.androidsdk.service.UserService;
import retrofit2.Retrofit;

public class UserHelper {


    private final TokenProvider tokenProvider;
    private final UserService service;
    private final Retrofit retrofit;

    public UserHelper(Context context, TokenProvider tokenProvider, String appVersion) {
        this.tokenProvider = tokenProvider;
        this.service = ProviderApiClient.GetAuthenticatedInstance(UserService.class, context, tokenProvider, appVersion);
        this.retrofit = ProviderApiClient.getAuthenticatedRetrofit();
    }

    public CallHandler<UserDto> getUserProfile() {
        return new CallHandler<>(service.getUserProfile(tokenProvider.getUid()), retrofit);
    }

    public CallHandler<UserDto> getUserByUsername(String username) {
        return new CallHandler<>(service.getUserByUsername(username), retrofit);
    }

    public CallHandler<UserDto> updateUserProfile(UserRequestDto request) {
        return new CallHandler<>(service.updateUserProfile(tokenProvider.getUid(), request), retrofit);
    }

    public CallHandler<Void> deleteUser() {
        return new CallHandler<>(service.deleteUser(tokenProvider.getUid()), retrofit);
    }
}