package com.example.fiadomanager.api;

import com.example.fiadomanager.data.model.user.NewUserRequest;
import com.example.fiadomanager.data.model.user.UserRequest;
import com.example.fiadomanager.data.model.user.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {

    @POST("login")
    Call<UserResponse> getUser(@Body UserRequest userRequest);

    @POST("newUser")
    Call<Boolean> newUser(@Body NewUserRequest newUserRequest);
}
