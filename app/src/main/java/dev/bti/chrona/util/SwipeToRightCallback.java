package dev.bti.chrona.util;

import android.content.Context;
import android.graphics.Canvas;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToRightCallback extends ItemTouchHelper.SimpleCallback {

    public SwipeToRightCallback(SwipeListener swipeListener) {
        super(0, ItemTouchHelper.RIGHT);
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.RIGHT;
        return makeMovementFlags(swipeFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        float maxSwipeDistance = recyclerView.getWidth() * 0.1f;
        boolean isSwipeExceeded = false;

        if (Math.abs(dX) > maxSwipeDistance) {

            dX = (dX > 0) ? maxSwipeDistance : -maxSwipeDistance;
            isSwipeExceeded = true;
        } else {
            float resistanceFactor = 1.0f - (Math.abs(dX) / maxSwipeDistance);
            dX *= resistanceFactor;
        }

        viewHolder.itemView.setTranslationX(dX);

        if (isSwipeExceeded) {
            Vibrator vibrator = (Vibrator) recyclerView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setTranslationX(0);
    }

    public interface SwipeListener {
        void onSwipeRight(int position);
    }
}
