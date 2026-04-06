package dev.bti.chrona.androidsdk.client;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final TokenProvider tokenProvider;

    public AuthInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder();

        String path = originalRequest.url().encodedPath();

        String token;
        if (path.contains("/auth/refresh")) {
            token = tokenProvider.getRefreshToken();
            System.out.println("Using REFRESH token");
        } else {
            token = tokenProvider.getAccessToken();
            System.out.println("Using ACCESS token");
        }

        System.out.println("PATH: " + path);
        System.out.println("TOKEN: " + token);

        if (token != null && !token.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + token);
        } else {
            System.out.println("⚠️ NO TOKEN ATTACHED");
        }

        return chain.proceed(requestBuilder.build());
    }
}