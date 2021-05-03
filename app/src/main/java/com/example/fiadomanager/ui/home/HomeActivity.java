package com.example.fiadomanager.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fiadomanager.R;
import com.example.fiadomanager.ui.home.fragment.Client;
import com.example.fiadomanager.ui.home.fragment.Home;
import com.example.fiadomanager.ui.home.fragment.OrderSheet;
import com.example.fiadomanager.ui.home.fragment.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";
    BottomNavigationView bottomNavigationView;
    Home homeFragment = new Home();
    Client clientFragment = new Client();
    Product productFragment = new Product();
    OrderSheet orderSheetFragment = new OrderSheet();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, homeFragment).commit();
                return true;

            case R.id.navigation_client:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, clientFragment).commit();
                return true;

            case R.id.navigation_product:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, productFragment).commit();
                return true;

            case R.id.navigation_ordersheet:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, orderSheetFragment).commit();
                return true;
        }
        return false;
    }
}