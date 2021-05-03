package com.example.fiadomanager.ui.bottom;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.fiadomanager.MyDialogCloseListener;
import com.example.fiadomanager.R;
import com.example.fiadomanager.api.ProductApi;
import com.example.fiadomanager.api.RetrofitApi;
import com.example.fiadomanager.data.model.product.NewOrdersheetProductRequest;
import com.example.fiadomanager.data.model.product.NewOrdersheetProductResponse;
import com.example.fiadomanager.data.model.ordersheet.OrderSheetResponse;
import com.example.fiadomanager.data.model.product.ProductList;
import com.example.fiadomanager.data.model.product.ProductRequest;
import com.example.fiadomanager.data.model.product.ProductResponse;
import com.example.fiadomanager.ui.home.fragment.OrderSheetEdition;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import retrofit2.Call;

public class BottomSheetProductDialog extends BottomSheetDialogFragment {
    ProductList products = new ProductList(new ArrayList<>());
    Long idClient;
    Long idOrdersheet;
    OrderSheetResponse orderSheetResponse;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_products_layout, container, false);

        Button buttonIncrease;
        Button buttonDecrease;
        Button buttonAddItem;
        EditText number;


        List<String> courses = new ArrayList<>();
        buttonIncrease =  (Button) view.findViewById(R.id.increase);
        buttonDecrease =  (Button) view.findViewById(R.id.decrease);
        number = (EditText) view.findViewById(R.id.qtd_number);

        buttonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    int soma = 0;
                    int input = Integer.parseInt(number.getText().toString()) + 1;

                    soma = soma + input;
                    number.setText(Integer.toString(soma));
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
                buttonDecrease.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int soma = 0;
                        int input = Integer.parseInt(number.getText().toString()) - 1;

                        soma = soma + input;
                        if(soma < 0){
                            soma = 0;
                        }
                        number.setText(Integer.toString(soma));
                    }
                });

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_client);

        ProductApi productApi = RetrofitApi.getInstance().getProductApi();

        Call<ProductList> call = productApi.getAllProcuts();

        try {
           products = call.clone().execute().body();

            ArrayAdapter<ProductResponse> adapter = new ArrayAdapter<ProductResponse>(view.getContext(),
                    android.R.layout.simple_spinner_dropdown_item, products.getProducts());
            spinner.setAdapter(adapter);

        } catch (IOException e) {
            e.printStackTrace();
        }

        buttonAddItem = (Button) view.findViewById(R.id.btn_create_product);
        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {


                if(number.getText().toString().equals("0")){

                    Toast toast =  Toast.makeText(view.getContext(),  R.string.product_add_zero,
                            Toast.LENGTH_LONG);
                    toast.show();
                }else{
                    Optional<ProductResponse> productResponse = products.getProducts().stream().filter(productResponseReturn -> productResponseReturn.getDescription().equals(spinner.getSelectedItem().toString())).findAny();

                    List<ProductRequest> productRequests = new ArrayList<>();
                    ProductRequest productRequest = new ProductRequest();
                    productRequest.setQuantity(Integer.parseInt(number.getText().toString()));
                    productRequest.setIdProduct(productResponse.get().getId());
                    productRequests.add(productRequest);


                    NewOrdersheetProductRequest newOrdersheetProductRequest = new NewOrdersheetProductRequest();
                    newOrdersheetProductRequest.setIdClient(idClient);
                    newOrdersheetProductRequest.setIdOrderSheet(idOrdersheet);
                    newOrdersheetProductRequest.setProducts(productRequests);
                    Call<NewOrdersheetProductResponse> callnewOrdersheet = productApi.newOrderSheet(newOrdersheetProductRequest);

                    try {
                        NewOrdersheetProductResponse newOrdersheetProductResponse = new NewOrdersheetProductResponse();

                        newOrdersheetProductResponse = callnewOrdersheet.clone().execute().body();

                        Toast toast =  Toast.makeText(view.getContext(),  R.string.product_add_success,
                                Toast.LENGTH_LONG);
                        toast.show();
                        number.setText("0");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                }

        });

        return view;

    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public void setIdOrdersheet(Long idOrdersheet) {
        this.idOrdersheet = idOrdersheet;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        FragmentManager fragment =getFragmentManager();
        Optional<Fragment> order = fragment.getFragments().stream().filter(fragment1 -> fragment1 instanceof OrderSheetEdition).findAny();
        ((MyDialogCloseListener)order.get()).handleDialogClose(dialog);
        super.onCancel(dialog);
    }

    public void setOrderSheetResponse(OrderSheetResponse orderSheetResponse) {
        this.orderSheetResponse = orderSheetResponse;
    }
}
