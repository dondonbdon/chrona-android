package dev.bti.chrona;

import android.content.Context;
import android.util.Log;

import dev.bti.chrona.androidsdk.client.TokenProvider;
import dev.bti.chrona.androidsdk.common.SDKHelpers;
import dev.bti.chrona.common.Credentials;
import dev.bti.chrona.util.SecureStorage;
import lombok.Getter;

public class Application extends android.app.Application {
    @Getter
    private static SDKHelpers helpers;

    private static final String TAG = "ChronaApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        initCredentials();
        initSdkHelpers();
    }

    private void initCredentials() {
        Credentials.init(getApplicationContext());
    }

    private void initSdkHelpers() {

        TokenProvider tokenProvider = new TokenProvider() {
            @Override
            public String getAccessToken() {
                return SecureStorage.getAccessToken(getApplicationContext());
            }

            @Override
            public String getRefreshToken() {
                return SecureStorage.getAccessToken(getApplicationContext());
            }

            @Override
            public String getUid() {
                return SecureStorage.getUserId(getApplicationContext());
            }

            @Override
            public Context getContext() {
                return getApplicationContext();
            }
        };

        String appVersion = "1.0.0";


        helpers = SDKHelpers.GetInstance(getApplicationContext(), tokenProvider, appVersion);

        Log.i(TAG, "SDK Initialized. Current UID: " + SecureStorage.getUserId(getApplicationContext()));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}