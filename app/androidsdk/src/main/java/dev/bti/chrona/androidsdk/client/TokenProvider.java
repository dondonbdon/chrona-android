package dev.bti.chrona.androidsdk.client;

import android.content.Context;

public interface TokenProvider {
    String getAccessToken();
    String getRefreshToken();
    String getUid();
    Context getContext();
}
