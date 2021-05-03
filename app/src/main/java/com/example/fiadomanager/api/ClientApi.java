package com.example.fiadomanager.api;

import com.example.fiadomanager.data.model.client.ClientList;
import com.example.fiadomanager.data.model.client.NewClientRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ClientApi {

    @GET("getClients")
    Call<ClientList> getClientList();

    @POST("newClient")
    Call<Boolean> newClient(@Body NewClientRequest newClientRequest);

    @GET("disableClient/{idClient}")
    Call<Boolean> disableClient(@Path("idClient") Long idClient);
}
