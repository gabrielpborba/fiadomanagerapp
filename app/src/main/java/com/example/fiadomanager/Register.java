package com.example.fiadomanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fiadomanager.api.RetrofitApi;
import com.example.fiadomanager.api.UserApi;
import com.example.fiadomanager.data.model.user.NewUserRequest;
import com.example.fiadomanager.ui.login.LoginActivity;

import java.io.IOException;

import retrofit2.Call;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final Button registerButton = findViewById(R.id.btn_register);

        final EditText nameEditText = findViewById(R.id.edit_new_name);
        final EditText usernameEditText = findViewById(R.id.edit_new_username);
        final EditText passwordEditText = findViewById(R.id.edit_new_password);
        final EditText passwordRepeatEditText = findViewById(R.id.edit_new_password_repeat);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(usernameEditText.getText().toString().contains(" ")){
                    Toast.makeText(v.getContext(), "Username nao pode conter espacos em branco",
                            Toast.LENGTH_LONG).show();
                }
                else if(!passwordEditText.getText().toString().equals(passwordRepeatEditText.getText().toString())){
                    Toast.makeText(v.getContext(), "Senhas nao conferem",
                            Toast.LENGTH_LONG).show();
                }else{

                    NewUserRequest newUserRequest = new NewUserRequest();
                    newUserRequest.setName(nameEditText.getText().toString());
                    newUserRequest.setUsername(usernameEditText.getText().toString());
                    newUserRequest.setPassword(passwordEditText.getText().toString());

                    UserApi userApi = RetrofitApi.getInstance().getUserApi();

                    Call<Boolean> call = userApi.newUser(newUserRequest);

                    try {
                        Boolean newUser = call.clone().execute().body();
                       if(newUser){
                           Intent myIntent = new Intent(v.getContext(), LoginActivity.class);
                           startActivityForResult(myIntent, 0);
                       }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }
}