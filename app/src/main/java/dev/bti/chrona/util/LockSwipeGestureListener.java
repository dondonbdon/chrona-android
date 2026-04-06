package dev.bti.chrona.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class LockSwipeGestureListener extends GestureDetector.SimpleOnGestureListener {

    private final Context context;
    private final boolean allowOnlyLeftSwipe;

    public LockSwipeGestureListener(Context context, boolean allowOnlyLeftSwipe) {
        this.context = context;
        this.allowOnlyLeftSwipe = allowOnlyLeftSwipe;
    }

    @Override
    public boolean onFling(MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        if (e1 != null) {
            float deltaX = e2.getX() - e1.getX();

            if (allowOnlyLeftSwipe && deltaX < -300) {
                Toast.makeText(context, "Swiped Left!", Toast.LENGTH_SHORT).show();
                return true;
            } else if (!allowOnlyLeftSwipe && deltaX > 300) {
                Toast.makeText(context, "Swiped Right!", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void setSwipeListener(View view, Context context, boolean allowOnlyLeftSwipe) {
        GestureDetector gestureDetector = new GestureDetector(context, new LockSwipeGestureListener(context, allowOnlyLeftSwipe));
        view.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });
    }
}


