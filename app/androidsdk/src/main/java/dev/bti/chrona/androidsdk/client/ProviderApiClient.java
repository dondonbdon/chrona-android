package dev.bti.chrona.androidsdk.client;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Instant;

import dev.bti.chrona.androidsdk.common.Config;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProviderApiClient {

    private static final String BASE_URL = String.format("http://%s/api/v1/", Config.GetInstance().getServerAddress());
    private static Retrofit retrofitAuthenticated;
    private static Retrofit retrofitPublic;
    private static Retrofit retrofitRefresh;

    /**
     * THE GENERIC CORE:
     * This private method handles all the boilerplate. You just pass it whatever
     * interceptors that specific Retrofit instance needs.
     */
    private static Retrofit buildRetrofit(Authenticator authenticator, Interceptor... interceptors) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        okhttp3.logging.HttpLoggingInterceptor logger = new okhttp3.logging.HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        clientBuilder.addInterceptor(logger);

        if (authenticator != null) {
            clientBuilder.authenticator(authenticator);
        }

        for (Interceptor interceptor : interceptors) {
            clientBuilder.addInterceptor(interceptor);
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                .create();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(clientBuilder.build())
                .build();
    }

    // ==========================================
    // PUBLIC ACCESSORS
    // ==========================================

    public static <T> T GetAuthenticatedInstance(Class<T> t, Context context, TokenProvider tokenProvider, String appVersion) {
        if (retrofitAuthenticated == null) {
            retrofitAuthenticated = buildRetrofit(
                    new TokenRefreshAuthenticator(context, tokenProvider, appVersion),
                    new StandardHeadersInterceptor(context, appVersion),
                    new AuthInterceptor(tokenProvider)
            );
        }
        return retrofitAuthenticated.create(t);
    }

    public static <T> T GetPublicInstance(Class<T> t, Context context, String appVersion) {
        if (retrofitPublic == null) {
            retrofitPublic = buildRetrofit(
                    null,
                    new StandardHeadersInterceptor(context, appVersion)
            );
        }
        return retrofitPublic.create(t);
    }

    public static <T> T GetRefreshInstance(Class<T> t, Context context, TokenProvider tokenProvider, String appVersion) {
        if (retrofitRefresh == null) {
            retrofitRefresh = buildRetrofit(
                    null, // ❌ NO authenticator
                    new StandardHeadersInterceptor(context, appVersion),
                    new AuthInterceptor(tokenProvider)
            );
        }
        return retrofitRefresh.create(t);
    }

    // ==========================================
    // RETROFIT INSTANCE GETTERS
    // ==========================================

    public static Retrofit getAuthenticatedRetrofit() {
        if (retrofitAuthenticated == null) {
            throw new IllegalStateException("Authenticated Retrofit is not initialized. Call GetAuthenticatedInstance first.");
        }
        return retrofitAuthenticated;
    }

    public static Retrofit getPublicRetrofit() {
        if (retrofitPublic == null) {
            throw new IllegalStateException("Public Retrofit is not initialized. Call GetPublicInstance first.");
        }
        return retrofitPublic;
    }

    public static Retrofit getRefreshRetrofit() {
        if (retrofitRefresh == null) {
            throw new IllegalStateException("Refresh Retrofit is not initialized. Call GetRefreshInstance first.");
        }
        return retrofitRefresh;
    }
}