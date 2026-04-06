package dev.bti.chrona.androidsdk.client;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.UUID;

public class DeviceIdUtil {

    private static final String PREFS_NAME = "chrona_device_prefs";
    private static final String DEVICE_ID_KEY = "install_device_id";

    private static String cachedDeviceId = null;

    /**
     * Retrieves the unique device ID for this installation.
     * If one does not exist, it generates it, saves it to disk, and returns it.
     */
    public static synchronized String getDeviceId(Context context) {
        if (cachedDeviceId != null) {
            return cachedDeviceId;
        }

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        cachedDeviceId = prefs.getString(DEVICE_ID_KEY, null);


        if (cachedDeviceId == null) {
            cachedDeviceId = UUID.randomUUID().toString();
            prefs.edit().putString(DEVICE_ID_KEY, cachedDeviceId).apply();
        }

        return cachedDeviceId;
    }
}