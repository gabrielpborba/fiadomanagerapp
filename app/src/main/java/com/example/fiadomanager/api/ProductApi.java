package com.example.fiadomanager.api;

import com.example.fiadomanager.data.model.product.NewOrdersheetProductRequest;
import com.example.fiadomanager.data.model.product.NewOrdersheetProductResponse;
import com.example.fiadomanager.data.model.product.NewProductRequest;
import com.example.fiadomanager.data.model.product.NewProductResponse;
import com.example.fiadomanager.data.model.product.ProductList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductApi {

    @GET("getAllProducts")
    Call<ProductList> getAllProcuts();

    @POST("newOrderSheet")
    Call<NewOrdersheetProductResponse> newOrderSheet(@Body NewOrdersheetProductRequest newOrdersheetProductRequest);

    @DELETE("deleteProductFromAOrderSheet/{idOrderSheetProduct}")
    Call<Boolean> deleteProduct(@Path("idOrderSheetProduct") Long idOrderSheetProduct);

    @POST("newProduct")
    Call<NewProductResponse> newProduct(@Body NewProductRequest newProductRequest);

}
