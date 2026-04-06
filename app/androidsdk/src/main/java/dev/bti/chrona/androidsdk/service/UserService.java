package dev.bti.chrona.androidsdk.service;

import dev.bti.chrona.androidsdk.common.SuccessResponse;
import dev.bti.chrona.androidsdk.dto.UserDto;
import dev.bti.chrona.androidsdk.dto.UserRequestDto;
import retrofit2.Call;
import retrofit2.http.*;

public interface UserService {

    @GET("users/{userId}")
    Call<SuccessResponse<UserDto>> getUserProfile(@Path("userId") String userId);

    @GET("users/username/{username}")
    Call<SuccessResponse<UserDto>> getUserByUsername(@Path("username") String username);

    @PUT("users/{userId}")
    Call<SuccessResponse<UserDto>> updateUserProfile(
            @Path("userId") String userId,
            @Body UserRequestDto request
    );

    @DELETE("users/{userId}")
    Call<SuccessResponse<Void>> deleteUser(@Path("userId") String userId);
}