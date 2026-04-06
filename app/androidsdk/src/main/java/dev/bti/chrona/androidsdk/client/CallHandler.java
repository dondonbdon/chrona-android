package dev.bti.chrona.androidsdk.client;

import org.jetbrains.annotations.NotNull;
import java.lang.annotation.Annotation;
import java.io.IOException;

import dev.bti.chrona.androidsdk.common.ErrorResponse;
import dev.bti.chrona.androidsdk.common.SuccessResponse;
import dev.bti.chrona.androidsdk.interfaces.OnFailureListener;
import dev.bti.chrona.androidsdk.interfaces.OnSuccessListener;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CallHandler<T> {
    private final Call<SuccessResponse<T>> call;
    private final Retrofit retrofit;
    private OnSuccessListener<T> successListener;
    private OnFailureListener failureListener;

    public CallHandler(Call<SuccessResponse<T>> call, Retrofit retrofit) {
        this.call = call;
        this.retrofit = retrofit;
    }

    public CallHandler<T> addOnSuccessListener(OnSuccessListener<T> listener) {
        this.successListener = listener;
        return this;
    }

    public CallHandler<T> addOnFailureListener(OnFailureListener listener) {
        this.failureListener = listener;
        return this;
    }

    public void execute() {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<SuccessResponse<T>> call,
                                   @NotNull Response<SuccessResponse<T>> response) {

                if (response.isSuccessful()) {
                    if (successListener != null) {
                        successListener.onSuccess(response.body());
                    }
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(@NotNull Call<SuccessResponse<T>> call, @NotNull Throwable t) {
                if (failureListener != null) {
                    failureListener.onFailure(ErrorResponse.builder()
                            .success(false)
                            .message(t.getMessage())
                            .errorCode("NETWORK_FAILURE")
                            .build());
                }
            }
        });
    }

    private void handleErrorResponse(Response<SuccessResponse<T>> response) {
        if (failureListener == null) return;

        Converter<ResponseBody, ErrorResponse> converter =
                retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);

        try (ResponseBody errorBody = response.errorBody()) {
            if (errorBody != null) {
                ErrorResponse error = converter.convert(errorBody);
                failureListener.onFailure(error);
            } else {
                failureListener.onFailure(createGenericError(response.code()));
            }
        } catch (IOException e) {
            failureListener.onFailure(createGenericError(response.code()));
        }
    }

    private ErrorResponse createGenericError(int code) {
        return ErrorResponse.builder()
                .success(false)
                .message("Server returned error code: " + code)
                .errorCode("HTTP_" + code)
                .build();
    }
}