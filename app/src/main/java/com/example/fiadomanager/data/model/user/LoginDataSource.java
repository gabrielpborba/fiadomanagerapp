package com.example.fiadomanager.data.model.user;

import com.example.fiadomanager.api.RetrofitApi;

import java.io.IOException;

import retrofit2.Call;


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    LoggedInUser loggedInUser;
    public Result<LoggedInUser> login(String username, String password) {


        try {
            UserRequest userRequest = new UserRequest();
            userRequest.setUsername(username);
            userRequest.setPassword(password);
            Call<UserResponse> call = RetrofitApi.getInstance().getUserApi().getUser(userRequest);
            UserResponse user = call.clone().execute().body();

            loggedInUser =  new LoggedInUser(
                    String.valueOf(user.getId()),
                    user.getUsername());

            return new Result.Success<>(loggedInUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

}