package dev.bti.chrona.androidsdk.helpers;

import android.content.Context;

import dev.bti.chrona.androidsdk.client.CallHandler;
import dev.bti.chrona.androidsdk.client.ProviderApiClient;
import dev.bti.chrona.androidsdk.dto.Credential;
import dev.bti.chrona.androidsdk.service.SecurityService;
import retrofit2.Retrofit;


public class SecurityHelper {


    private final SecurityService service;
    private final Retrofit retrofit;

    public SecurityHelper(Context context, String appVersion ) {
        this.service = ProviderApiClient.GetPublicInstance(SecurityService.class, context, appVersion);
        this.retrofit = ProviderApiClient.getPublicRetrofit();
    }

    public CallHandler<Void> requestResetCode(Credential credential) {
        return new CallHandler<>(service.requestResetCode(credential), retrofit);
    }

    public CallHandler<Void> validateResetCode(Credential credential, String resetCode) {
        return new CallHandler<>(service.validateResetCode(credential, resetCode), retrofit);
    }
}
