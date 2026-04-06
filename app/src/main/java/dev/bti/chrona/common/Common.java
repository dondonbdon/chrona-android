package dev.bti.chrona.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

import dev.bti.chrona.androidsdk.dto.UserDto;
import dev.bti.chrona.constants.ParseNameMode;

public class Common {
    public static boolean isSentByUser(String senderId) {
        return Boolean.TRUE.equals(Credentials.GetInstance().UID.equals(senderId));
    }

    public static class Validators {
        private static final String NAME_REGEX = "^[a-zA-Zà-žÀ-Ž'\\-\\s]{2,50}$";

        public static boolean isValidEmail(String email) {
            return email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }

        public static boolean isValidName(String name) {
            if (name == null) {
                return false;
            }

            return name.matches(NAME_REGEX);
        }

        public static boolean isValidPhoneNumber(String phoneNumber, String regionCode) {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            try {
                Phonenumber.PhoneNumber number = phoneUtil.parse(phoneNumber, regionCode);
                return !phoneUtil.isValidNumber(number);
            } catch (NumberParseException e) {
                return true;
            }
        }
    }

    public static String parseName(UserDto userProfile, ParseNameMode mode) {
        return switch (mode) {
            case FIRST_NAME -> userProfile.getFirstName();
            case LAST_NAME -> userProfile.getLastName();
            default ->
                    String.format("%s %c", userProfile.getFirstName(), userProfile.getLastName().toUpperCase().charAt(0));
        };
    }

    public static String parseTimestamp(Instant instant) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        java.time.Duration duration = java.time.Duration.between(then, now);

        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            if (days == 1) {
                return "1 day ago";
            }
            return days + " days ago";
        } else if (hours > 0) {
            if (hours == 1) {
                return "1 hour ago";
            }
            return hours + " hours ago";
        } else if (minutes > 0) {
            if (minutes == 1) {
                return "1 min ago";
            }
            return minutes + " mins ago";
        }
        return "just now";
    }

    public static String parseTimestampForSeparator(Instant instant) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        if (then.isAfter(now.minusWeeks(1))) {
            DayOfWeek dayOfWeek = then.getDayOfWeek();
            return dayOfWeek.toString();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        return then.format(formatter);
    }

    public static <K, V> V getValueAtPosition(Map<K, V> map, int position) {
        if (position < 0 || position >= map.size()) {
            throw new IndexOutOfBoundsException("Position out of bounds");
        }

        int currentIndex = 0;
        for (V value : map.values()) {
            if (currentIndex == position) {
                return value;
            }
            currentIndex++;
        }
        return null;
    }

    public static <K, V> K getKeyAtPosition(Map<K, V> map, int position) {
        if (position < 0 || position >= map.size()) {
            throw new IndexOutOfBoundsException("Position out of bounds");
        }

        int currentIndex = 0;
        for (K key : map.keySet()) {
            if (currentIndex == position) {
                return key;
            }
            currentIndex++;
        }

        return null;
    }

    public static <K, V> Integer getPositionOfReaction(Map<K, V> map, String reaction) {
        if (reaction == null) {
            return null;
        }

        int currentIndex = 0;
        for (V value : map.values()) {
            if (reaction.equals(value.toString())) {
                return currentIndex;
            }
            currentIndex++;
        }

        return null;
    }

    public static void attachSnapHelperWithListener(
            RecyclerView recyclerView,
            SnapHelper snapHelper,
            OnSnapPositionChangeListener listener) {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastSnapPosition = RecyclerView.NO_POSITION;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View snappedView = snapHelper.findSnapView(recyclerView.getLayoutManager());

                    if (snappedView != null) {
                        int newSnapPosition = Objects.requireNonNull(recyclerView.getLayoutManager()).getPosition(snappedView);

                        if (newSnapPosition != lastSnapPosition) {
                            lastSnapPosition = newSnapPosition;

                            listener.onSnapPositionChange(newSnapPosition);
                        }
                    }
                }
            }
        });
    }

    public interface OnSnapPositionChangeListener {
        void onSnapPositionChange(int position);
    }

    public static String localiseUrl(String url) {
        return url.replace("localhost", "10.0.2.2");
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void setVisibilityOfPasswordToggleListener(AppCompatEditText editText) {
        editText.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_UP) {
//                int drawableEnd = 2;
//                if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[drawableEnd].getBounds().width())) {
//                    if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
//                        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0);
//                    } else {
//                        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
//                    }
//                    editText.setSelection(Objects.requireNonNull(editText.getText()).length());
//                    return true;
//                }
//            }
            return false;
        });
    }

    public static boolean isUrl(Uri uri) {
        return uri != null && (Objects.requireNonNull(uri.getScheme()).equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https"));
    }

    public static Uri getUriFromDrawable(Context context, @DrawableRes int drawableRes) {
        try {
            Drawable drawable = ContextCompat.getDrawable(context, drawableRes);
            if (drawable == null) return null;

            Bitmap bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888
            );

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            File file = new File(context.getCacheDir(), "drawable_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        } catch (IOException e) {
            Logger.getLogger("Common").severe("Error converting drawable to URI: " + e.getMessage());
            return null;
        }
    }

    public static long stringToUniqueId(String input) {
        UUID uuid = UUID.nameUUIDFromBytes(input.getBytes());
        return uuid.getMostSignificantBits();
    }

}
