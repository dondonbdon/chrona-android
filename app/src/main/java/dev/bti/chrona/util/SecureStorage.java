package dev.bti.chrona.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import com.auth0.android.jwt.JWT;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import dev.bti.chrona.androidsdk.dto.AuthDto;
import dev.bti.chrona.common.Credentials;

public class SecureStorage {
    private static final String PREFS_NAME = "secure_prefs";
    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String REFRESH_TOKEN_KEY = "refresh_token";
    private static final String USER_ID_KEY = "user_id";
    private static final String KEY_ALIAS = "BoundlessKeyAlias";
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private static SecretKey getOrCreateSecretKey() throws GeneralSecurityException, IOException {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE);
            keyGenerator.init(
                    new KeyGenParameterSpec.Builder(
                            KEY_ALIAS,
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
                    )
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setKeySize(256)
                            .build()
            );
            return keyGenerator.generateKey();
        }

        return ((KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null)).getSecretKey();
    }

    private static String encryptData(String data) throws GeneralSecurityException, IOException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateSecretKey());
        byte[] encryptionIv = cipher.getIV();
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeToString(encryptionIv, Base64.DEFAULT) + ":" +
                Base64.encodeToString(encryptedData, Base64.DEFAULT);
    }

    private static String decryptData(String encryptedData) throws GeneralSecurityException, IOException {
        String[] parts = encryptedData.split(":");
        byte[] iv = Base64.decode(parts[0], Base64.DEFAULT);
        byte[] encryptedBytes = Base64.decode(parts[1], Base64.DEFAULT);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateSecretKey(), spec);

        return new String(cipher.doFinal(encryptedBytes), StandardCharsets.UTF_8);
    }

    public static void saveAuthData(Context context, AuthDto authDto) {
        try {
            SharedPreferences prefs = getSharedPreferences(context);
            String encryptedAccess = encryptData(authDto.getAccessToken());
            String encryptedRefresh = encryptData(authDto.getRefreshToken());

            prefs.edit()
                    .putString(ACCESS_TOKEN_KEY, encryptedAccess)
                    .putString(REFRESH_TOKEN_KEY, encryptedRefresh)
                    .putString(USER_ID_KEY, authDto.getUserId())
                    .apply();

            Credentials.init(context);
        } catch (GeneralSecurityException | IOException e) {
            Log.e("SecureStorage", "Error saving auth data", e);
        }
    }

    public static String getAccessToken(Context context) {
        return getDecryptedString(context, ACCESS_TOKEN_KEY);
    }

    public static String getRefreshToken(Context context) {
        return getDecryptedString(context, REFRESH_TOKEN_KEY);
    }

    private static String getDecryptedString(Context context, String key) {
        try {
            String encrypted = getSharedPreferences(context).getString(key, null);
            if (encrypted != null) {
                return decryptData(encrypted);
            }
        } catch (GeneralSecurityException | IOException e) {
            Log.e("SecureStorage", "Error decrypting " + key, e);
        }
        return null;
    }

    public static String getUserId(Context context) {
        return getSharedPreferences(context).getString(USER_ID_KEY, null);
    }

    public static void clearData(Context context) {
        getSharedPreferences(context).edit().clear().apply();
    }

    /**
     * Validates ONLY the JWT Access Token.
     */
    public static boolean isAccessTokenValid(String token) {
        if (token == null || token.isEmpty()) return false;
        try {
            JWT jwt = new JWT(token);
            return !jwt.isExpired(10);
        } catch (Exception e) {
            // Safe catch for when UUIDs accidentally hit this method
            Log.e("SecureStorage", "Failed to parse JWT. Is this a UUID?", e);
            return false;
        }
    }
}