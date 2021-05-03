package com.example.fiadomanager.ui.bottom;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.fiadomanager.R;
import com.example.fiadomanager.api.ClientApi;
import com.example.fiadomanager.api.OrdersheetApi;
import com.example.fiadomanager.api.RetrofitApi;
import com.example.fiadomanager.data.model.client.ClientList;
import com.example.fiadomanager.data.model.client.ClientResponse;
import com.example.fiadomanager.data.model.product.NewOrdersheetProductRequest;
import com.example.fiadomanager.data.model.product.NewOrdersheetProductResponse;
import com.example.fiadomanager.data.model.ordersheet.OrderSheetResponse;
import com.example.fiadomanager.ui.home.fragment.OrderSheetEdition;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import retrofit2.Call;

public class BottomSheetOrdersheetDialog extends BottomSheetDialogFragment {
   ClientList clientList = new ClientList(new ArrayList<>());
    OrderSheetResponse orderSheetResponse;

    BottomSheetOrdersheetDialog  bottomSheetOrdersheetDialog;

    public void setBottomSheetOrdersheetDialog(BottomSheetOrdersheetDialog bottomSheetOrdersheetDialog) {
        this.bottomSheetOrdersheetDialog = bottomSheetOrdersheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_new_ordersheet, container, false);
        Button buttonCreateOrdersheet;

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_client);
        //spinner.setOnItemSelectedListener(this);

        ClientApi clientApi = RetrofitApi.getInstance().getClientApi();

        Call<ClientList> call = clientApi.getClientList();

        try {
           clientList = call.clone().execute().body();

            ArrayAdapter<ClientResponse> adapter = new ArrayAdapter<ClientResponse>(view.getContext(),
                    android.R.layout.simple_spinner_dropdown_item, clientList.getClientList());
            spinner.setAdapter(adapter);

        } catch (IOException e) {
            e.printStackTrace();
        }

        buttonCreateOrdersheet = (Button) view.findViewById(R.id.btn_create_ordersheet_product);
        buttonCreateOrdersheet.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                Optional<ClientResponse> clientResponse = clientList.getClientList().stream().filter(clientResponseReturn -> clientResponseReturn.getName().equals(spinner.getSelectedItem().toString())).findAny();

                OrdersheetApi ordersheetApi = RetrofitApi.getInstance().getOrdersheetApi();

                NewOrdersheetProductRequest newOrdersheetProductRequest = new NewOrdersheetProductRequest();
                newOrdersheetProductRequest.setIdClient(clientResponse.get().getId());
                newOrdersheetProductRequest.setProducts(new ArrayList<>());

                Call<NewOrdersheetProductResponse> callNewOrder = ordersheetApi.newOrderSheet(newOrdersheetProductRequest);


                try {
                    NewOrdersheetProductResponse newOrdersheetProductResponse = new NewOrdersheetProductResponse();

                    newOrdersheetProductResponse =  callNewOrder.clone().execute().body();
                    System.out.println(newOrdersheetProductResponse.getIdOrderSheet());


                    Call<OrderSheetResponse>  callgetOrder = ordersheetApi.getOrdersheetById(newOrdersheetProductResponse.getIdOrderSheet().toString());
                    OrderSheetResponse orderSheetResponse = callgetOrder.clone().execute().body();

                    OrderSheetEdition orderSheetEdition = new OrderSheetEdition();
                    orderSheetEdition.setOrderSheetResponse(orderSheetResponse);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, orderSheetEdition).commit();

                    bottomSheetOrdersheetDialog.dismiss();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        });



        return view;



    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        System.out.println("chamou");

        super.onCancel(dialog);
    }

    public void setOrderSheetResponse(OrderSheetResponse orderSheetResponse) {
        this.orderSheetResponse = orderSheetResponse;
    }
}
