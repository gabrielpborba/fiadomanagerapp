package com.example.fiadomanager.api;

import com.example.fiadomanager.ui.home.fragment.Client;
import com.example.fiadomanager.ui.home.fragment.OrderSheet;
import com.example.fiadomanager.ui.home.fragment.Product;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApi {
    private static RetrofitApi instance = null;
    public static final String BASE_URL = "https://fiadomanagerapp.herokuapp.com/";

    // Keep your services here, build them in buildRetrofit method later
    private UserApi userApi;
    private OrdersheetApi ordersheetApi;
    private ClientApi clientApi;
    private ProductApi productApi;

    public static RetrofitApi getInstance() {
        if (instance == null) {
            instance = new RetrofitApi();
        }

        return instance;
    }

    // Build retrofit once when creating a single instance
    private RetrofitApi() {
        // Implement a method to build your retrofit
        buildRetrofit(BASE_URL);
    }

    private void buildRetrofit(String baseUrl) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Build your services once
        this.userApi = retrofit.create(UserApi.class);
        this.ordersheetApi = retrofit.create(OrdersheetApi.class);
        this.clientApi = retrofit.create(ClientApi.class);
        this.productApi = retrofit.create(ProductApi.class);

    }

    public UserApi getUserApi() {
        return this.userApi;
    }
    public OrdersheetApi getOrdersheetApi() { return ordersheetApi; }

    public ClientApi getClientApi() {
        return clientApi;
    }

    public ProductApi getProductApi() {
        return productApi;
    }
}