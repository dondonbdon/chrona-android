package dev.bti.chrona.androidsdk.client;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import dev.bti.chrona.androidsdk.common.SDKHelpers;
import dev.bti.chrona.androidsdk.helpers.AuthHelper;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenRefreshAuthenticator implements Authenticator {

    private final Context context;
    private final TokenProvider tokenProvider;
    private final String appVersion;

    public TokenRefreshAuthenticator(Context context, TokenProvider tokenProvider, String appVersion) {
        this.context = context.getApplicationContext();
        this.tokenProvider = tokenProvider;
        this.appVersion = appVersion;
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NonNull Response response) {
        if (responseCount(response) >= 2) {
            return null;
        }

        synchronized (this) {
            String failedToken = response.request().header("Authorization");
            String currentToken = "Bearer " + tokenProvider.getAccessToken();

            if (failedToken != null && !failedToken.equals(currentToken)) {
                return response.request().newBuilder()
                        .header("Authorization", currentToken)
                        .build();
            }

            CountDownLatch latch = new CountDownLatch(1);
            final String[] newAccessToken = {null};


            AuthHelper authHelper = SDKHelpers.GetInstance(context, tokenProvider, appVersion).getAuthHelper();

            authHelper.refreshToken()
                    .addOnSuccessListener(result -> {
                        newAccessToken[0] = result.getPayload().getAccessToken();
                        latch.countDown();
                    })
                    .addOnFailureListener(errorResponse -> {
                        authHelper.logout().execute();
                        latch.countDown();
                    })
                    .execute();

            try {
                latch.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                authHelper.logout().execute();
                return null;
            }

            if (newAccessToken[0] != null) {
                return response.request().newBuilder()
                        .header("Authorization", "Bearer " + newAccessToken[0])
                        .build();
            } else {
                return null;
            }
        }
    }

    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
}