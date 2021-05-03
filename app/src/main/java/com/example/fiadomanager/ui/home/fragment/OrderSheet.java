package com.example.fiadomanager.ui.home.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.fiadomanager.ui.bottom.BottomSheetOrdersheetDialog;
import com.example.fiadomanager.R;
import com.example.fiadomanager.ui.recycler.RecyclerAdapter;
import com.example.fiadomanager.api.ClientApi;
import com.example.fiadomanager.api.OrdersheetApi;
import com.example.fiadomanager.api.RetrofitApi;
import com.example.fiadomanager.data.model.client.ClientList;
import com.example.fiadomanager.data.model.ordersheet.OrderSheetResponse;
import com.example.fiadomanager.data.model.ordersheet.OrdersheetList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderSheet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderSheet extends Fragment {
     RecyclerView recyclerView;
     RecyclerAdapter recyclerAdapter;

     List<OrderSheetResponse> orderList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderSheet() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderSheet.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderSheet newInstance(String param1, String param2) {
        OrderSheet fragment = new OrderSheet();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        orderList = new ArrayList<OrderSheetResponse>();
        View view = inflater.inflate(R.layout.fragment_order_sheet, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_ordersheet);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        OrdersheetApi ordersheetApi = RetrofitApi.getInstance().getOrdersheetApi();

        Call<OrdersheetList> call = ordersheetApi.getListOrderSheet("1");

        try {
            OrdersheetList order = call.clone().execute().body();
            recyclerAdapter = new RecyclerAdapter(order.getOrderSheets());
            recyclerView.setAdapter(recyclerAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setHasOptionsMenu(true);

        Button buttonOpenNewOrdersheet = (Button) view.findViewById(R.id.btn_add_product_ordersheet);
        buttonOpenNewOrdersheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClientApi clientApi = RetrofitApi.getInstance().getClientApi();

                Call<ClientList> callclients = clientApi.getClientList();

               try {
                   Response<ClientList> response = callclients.clone().execute();
                   if(response.errorBody() != null) {

                       Toast toast = Toast.makeText(view.getContext(), R.string.clients_null,
                               Toast.LENGTH_LONG);
                       toast.show();
                   }else{

                       BottomSheetOrdersheetDialog bottomSheetOrdersheetDialog = new BottomSheetOrdersheetDialog();
                       bottomSheetOrdersheetDialog.setBottomSheetOrdersheetDialog(bottomSheetOrdersheetDialog);
                       bottomSheetOrdersheetDialog.show(getFragmentManager(), "tag");
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        try {
            inflater.inflate(R.menu.main_menu_ordersheet, menu);

        }catch (Exception e){
            e.printStackTrace();
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        OrdersheetApi ordersheetApi = RetrofitApi.getInstance().getOrdersheetApi();



        switch(item.getItemId()){

            case R.id.action_paid:
                Call<OrdersheetList> callNotPaid = ordersheetApi.getListOrderSheet("1");
                OrdersheetList order1 = null;
                try {
                    order1 = callNotPaid.clone().execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                recyclerAdapter = new RecyclerAdapter(order1.getOrderSheets());
                recyclerView.setAdapter(recyclerAdapter);
                return true;
            case R.id.action_not_paid:
                Call<OrdersheetList> callPaid = ordersheetApi.getListOrderSheet("0");
                OrdersheetList order2 = null;
                try {
                    order2 = callPaid.clone().execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                recyclerAdapter = new RecyclerAdapter(order2.getOrderSheets());
                recyclerView.setAdapter(recyclerAdapter);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
}