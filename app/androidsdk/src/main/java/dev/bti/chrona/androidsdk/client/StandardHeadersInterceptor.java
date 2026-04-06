package dev.bti.chrona.androidsdk.client;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.UUID;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class StandardHeadersInterceptor implements Interceptor {

    private final Context context;
    private final String appVersionCode;

    public StandardHeadersInterceptor(Context context, String appVersionCode) {
        this.context = context.getApplicationContext();
        this.appVersionCode = appVersionCode;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("X-App-Version", appVersionCode)
                .header("X-Device-ID", DeviceIdUtil.getDeviceId(context))
                .header("X-Correlation-ID", UUID.randomUUID().toString());

        String method = original.method();
        if (method.equals("POST") || method.equals("PUT") || method.equals("PATCH")) {
            if (original.header("Idempotency-Key") == null) {
                requestBuilder.header("Idempotency-Key", UUID.randomUUID().toString());
            }
        }

        return chain.proceed(requestBuilder.build());
    }
}