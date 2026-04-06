package dev.bti.chrona.androidsdk.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class Common {
    public static String getMimeType(File file) {
        String fileName = file.getName();
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".mp4")) {
            return "video/mp4";
        } else if (fileName.endsWith(".avi")) {
            return "video/x-msvideo";
        }

        return "application/octet-stream";
    }

    private static File convertBitmapToFile(Bitmap bitmap, Context context) {
        File file = new File(context.getCacheDir(), "image_" + System.currentTimeMillis() + ".jpg");

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (IOException e) {
            Logger.getAnonymousLogger().severe(e.getMessage());
        }

        return file;
    }

    public static File convertUriToFile(Uri uri, Context context) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            return convertBitmapToFile(BitmapFactory.decodeStream(inputStream), context);
        } catch (IOException e) {
            Logger.getAnonymousLogger().severe(e.getMessage());
            return null;
        }
    }

    public static boolean isAlphanumericRegex(String str) {
        return str != null && str.matches("[a-zA-Z0-9]+");
    }
}
