package com.example.fiadomanager.api;

import com.example.fiadomanager.data.model.product.NewOrdersheetProductRequest;
import com.example.fiadomanager.data.model.product.NewOrdersheetProductResponse;
import com.example.fiadomanager.data.model.ordersheet.OrderSheetResponse;
import com.example.fiadomanager.data.model.ordersheet.OrdersheetList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrdersheetApi {

    @GET("getOrderSheets/status/{Status}")
    Call<OrdersheetList> getListOrderSheet(@Path("Status") String status);

    @GET("/getOrderSheets/id/{id}")
    Call<OrderSheetResponse> getOrdersheetById(@Path("id") String id);

    @POST("/newOrderSheet")
    Call<NewOrdersheetProductResponse> newOrderSheet(@Body NewOrdersheetProductRequest newOrdersheetProductRequest);

    @GET("closedOrderSheets/{idOrderSheet}")
    Call<Boolean> closeOrderSheet(@Path("idOrderSheet") Long idOrderSheet);
}
