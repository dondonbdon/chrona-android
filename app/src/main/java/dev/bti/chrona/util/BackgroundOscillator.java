package dev.bti.chrona.util;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import dev.bti.chrona.R;

public class BackgroundOscillator {
    private static final int OSCILLATION_COUNT = 6;
    private static final int INTERVAL_MS = 500;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private View currentAnimatingView;
    private static Runnable currentRunnable;
    private Drawable glow;

    public void startOscillating(View view) {
        if (currentAnimatingView != null && currentAnimatingView != view) {
            stopOscillating();
        }

        currentAnimatingView = view;

        glow = new GlowDrawable(ContextCompat.getColor(currentAnimatingView.getContext(), R.color.m1),
                ContextCompat.getColor(currentAnimatingView.getContext(), R.color.m2),
                5f, 20f);

        currentRunnable = new Runnable() {
            int count = 0;
            boolean isGlowVisible = false;

            @Override
            public void run() {
                if (count >= OSCILLATION_COUNT || currentAnimatingView != view) {
                    view.setBackground(null);
                    currentAnimatingView = null;
                    currentRunnable = null;
                    glow = null;
                    return;
                }

                view.setBackground(isGlowVisible ? null : glow);
                isGlowVisible = !isGlowVisible;
                count++;
                handler.postDelayed(this, INTERVAL_MS);
            }
        };

        handler.post(currentRunnable);
    }

    public void stopOscillating() {
        if (currentRunnable != null && currentAnimatingView != null) {
            handler.removeCallbacks(currentRunnable);
            currentAnimatingView.setBackground(null);
            currentAnimatingView = null;
            currentRunnable = null;
            glow = null;
        }
    }


    public static class GlowDrawable extends Drawable {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint glowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final RectF rect = new RectF();

        public GlowDrawable(int strokeColor, int glowColor, float strokeWidth, float glowSize) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(strokeColor);
            paint.setStrokeWidth(strokeWidth);

            glowPaint.setStyle(Paint.Style.STROKE);
            glowPaint.setColor(glowColor);
            glowPaint.setStrokeWidth(strokeWidth + glowSize);
            glowPaint.setMaskFilter(new BlurMaskFilter(glowSize, BlurMaskFilter.Blur.OUTER));
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            rect.set(getBounds());
            float halfStroke = paint.getStrokeWidth() / 2;

            canvas.drawRoundRect(rect, 20, 20, glowPaint);
            canvas.drawRoundRect(rect, 20, 20, paint);
        }

        @Override
        public void setAlpha(int alpha) {
            paint.setAlpha(alpha);
            glowPaint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
            paint.setColorFilter(colorFilter);
            glowPaint.setColorFilter(colorFilter);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }

}