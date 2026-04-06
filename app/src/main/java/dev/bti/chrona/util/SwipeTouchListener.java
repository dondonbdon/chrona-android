package dev.bti.chrona.util;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

import androidx.fragment.app.Fragment;

public class SwipeTouchListener implements View.OnTouchListener {
    private final View fragmentView;
    private float initialY = 0f;

    public SwipeTouchListener(View fragmentView) {
        this.fragmentView = fragmentView;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialY = event.getRawY();
                return true;

            case MotionEvent.ACTION_MOVE:
                float deltaY = event.getRawY() - initialY;
                if (deltaY > 0) {
                    fragmentView.setTranslationY(deltaY);
                } else if (fragmentView.getTranslationY() > 0) {
                    fragmentView.setTranslationY(0);
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                handleRelease();
                return true;
        }
        return false;
    }

    private void handleRelease() {
        if (fragmentView.getTranslationY() > (float) fragmentView.getHeight() / 4) {
            fragmentView.animate()
                    .translationY(fragmentView.getHeight())
                    .setDuration(300)
                    .withEndAction(() -> {
                        Fragment fragment = (Fragment) fragmentView.getTag();
                        if (fragment != null) {
                            fragment.getParentFragmentManager().beginTransaction().remove(fragment).commit();
                        }
                    }).start();
        } else {
            fragmentView.animate().translationY(0).setDuration(300).start();
        }
    }
}

