package com.example.fiadomanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import com.example.fiadomanager.ui.home.HomeActivity;
import com.example.fiadomanager.ui.home.fragment.Home;
import com.example.fiadomanager.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String username = "username";
    public static final String fileName = "login";
    public static final String password = "password";
    Home homeFragment = new Home();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sharedPreferences = getApplicationContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(username)){
            Intent myIntent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivityForResult(myIntent, 0);
        }

        Button login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
            }

        });

        Button register = (Button) findViewById(R.id.btnRegister);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Register.class);
                startActivityForResult(myIntent, 0);
            }

        });
    }
}