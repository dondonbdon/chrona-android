package dev.bti.chrona.androidsdk.interfaces;

import dev.bti.chrona.androidsdk.common.ErrorResponse;

public interface OnFailureListener {
    void onFailure(ErrorResponse errorResponse);
}
