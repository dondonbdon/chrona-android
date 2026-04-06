package dev.bti.chrona.common;

import android.content.Context;

import dev.bti.chrona.util.SecureStorage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Credentials {

    private static volatile Credentials credentials;
    public String ACCESS_TOKEN;
    public String REFRESH_TOKEN;
    public String UID;

    public static void init(Context context) {
        if (credentials == null) {
            synchronized (Credentials.class) {
                if (credentials == null) {
                    String userId = SecureStorage.getUserId(context);
                    String accessToken = SecureStorage.getAccessToken(context);
                    String refreshToken = SecureStorage.getRefreshToken(context);
                    credentials = new Credentials(accessToken, refreshToken, userId);
                }
            }
        }
    }

    public static Credentials GetInstance() {
        return credentials;
    }

    /**
     * Updates the tokens in memory. Call this when your Authenticator
     * successfully fetches a new access token using the refresh token.
     */
    public void updateTokens(String newAccessToken, String newRefreshToken) {
        this.ACCESS_TOKEN = newAccessToken;
        this.REFRESH_TOKEN = newRefreshToken;
    }

    /**
     * A user is considered logged in if they have a UID and either:
     * 1. A valid Access Token OR
     * 2. A Refresh Token (which can be used to get a new Access Token)
     */
    public boolean isLoggedIn() {
        return UID != null && (isAccessTokenValid() || REFRESH_TOKEN != null);
    }

    public void logout(Context context) {
        SecureStorage.clearData(context);
        credentials = null;
    }

    public boolean isAccessTokenValid() {
        return SecureStorage.isAccessTokenValid(ACCESS_TOKEN);
    }
}