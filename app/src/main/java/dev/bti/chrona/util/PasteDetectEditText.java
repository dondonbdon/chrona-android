package dev.bti.chrona.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.Objects;

public class PasteDetectEditText extends AppCompatEditText {
    private OnPasteListener pasteListener;

    public PasteDetectEditText(Context context) {
        super(context);
        init(context);
    }

    public PasteDetectEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PasteDetectEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (context instanceof OnPasteListener) {
            pasteListener = (OnPasteListener) context;
        } else {
            Log.w("PasteDetectEditText", "Parent context does not implement OnPasteListener");
        }
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        if (id == android.R.id.paste) {
            CharSequence clipboardText = getClipboardContent();
            if (clipboardText != null && pasteListener != null) {
                pasteListener.onPasteDetected(clipboardText.toString());
            }
        }
        return super.onTextContextMenuItem(id);
    }

    private CharSequence getClipboardContent() {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null && clipboard.hasPrimaryClip()) {
            return Objects.requireNonNull(clipboard.getPrimaryClip()).getItemAt(0).getText();
        }
        return null;
    }

    public interface OnPasteListener {
        void onPasteDetected(String pastedContent);
    }
}

