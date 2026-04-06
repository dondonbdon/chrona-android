package dev.bti.chrona.androidsdk.interfaces;

import dev.bti.chrona.androidsdk.common.Response;
import dev.bti.chrona.androidsdk.common.SuccessResponse;

public interface OnSuccessListener<T> {
    void onSuccess(SuccessResponse<T> result);
}
