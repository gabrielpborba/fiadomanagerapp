package com.example.fiadomanager.ui.home.fragment;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fiadomanager.ui.bottom.BottomSheetProductDialog;
import com.example.fiadomanager.MyDialogCloseListener;
import com.example.fiadomanager.R;
import com.example.fiadomanager.ui.recycler.RecyclerProductOrdersheetAdapter;
import com.example.fiadomanager.api.OrdersheetApi;
import com.example.fiadomanager.api.ProductApi;
import com.example.fiadomanager.api.RetrofitApi;
import com.example.fiadomanager.data.model.ordersheet.OrderSheetResponse;
import com.example.fiadomanager.data.model.product.ProductList;
import com.example.fiadomanager.data.model.product.ProductResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderSheetEdition#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderSheetEdition extends Fragment implements MyDialogCloseListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView txt_name_client_ordersheet;
    TextView txt_total_value_ordersheet;
    TextView txt_cellphone_client_ordersheet;
    TextView txt_date_ordersheet;
    RecyclerView recyclerView;
    RecyclerProductOrdersheetAdapter recyclerAdapter;


    private OrderSheetResponse orderSheetResponse;

    public OrderSheetResponse getOrderSheetResponse() {
        return orderSheetResponse;
    }

    public void setOrderSheetResponse(OrderSheetResponse orderSheetResponse) {
        this.orderSheetResponse = orderSheetResponse;
    }


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderSheetEdition() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderSheetEdition.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderSheetEdition newInstance(String param1, String param2) {
        OrderSheetEdition fragment = new OrderSheetEdition();
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

        //System.out.println(teste);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

        View v =  inflater.inflate(R.layout.fragment_order_sheet_edition, container, false);



        Button buttonOpenBottomProducts = (Button) v.findViewById(R.id.btn_add_product_ordersheet);
        buttonOpenBottomProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                try {

                    ProductApi productApi = RetrofitApi.getInstance().getProductApi();

                    Call<ProductList> call = productApi.getAllProcuts();

                    Response<ProductList> response = call.clone().execute();

                    if(response.errorBody() != null){
                        Toast toast = Toast.makeText(v.getContext(), R.string.products_null,
                                Toast.LENGTH_LONG);
                        toast.show();


                    }else{
                        BottomSheetProductDialog bottomSheetProductDialog = new BottomSheetProductDialog();
                        bottomSheetProductDialog.setIdClient(orderSheetResponse.getClient().getId());
                        bottomSheetProductDialog.setIdOrdersheet(orderSheetResponse.getId());
                        bottomSheetProductDialog.setOrderSheetResponse(orderSheetResponse);
                        bottomSheetProductDialog.show(getFragmentManager(), "tag");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        Button buttonCloseOrdersheet = (Button) v.findViewById(R.id.btn_close_ordersheet);
        buttonCloseOrdersheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrdersheetApi ordersheetApi = RetrofitApi.getInstance().getOrdersheetApi();

                Call<Boolean> callCloseOrdersheet = ordersheetApi.closeOrderSheet(orderSheetResponse.getId());

                try {
                    Boolean result = callCloseOrdersheet.clone().execute().body();
                    if(result){
                        buttonOpenBottomProducts.setEnabled(false);
                        buttonOpenBottomProducts.setBackgroundColor(buttonOpenBottomProducts.getContext().getResources().getColor(R.color.DarkGray));
                        buttonCloseOrdersheet.setEnabled(false);
                        buttonCloseOrdersheet.setBackgroundColor(buttonCloseOrdersheet.getContext().getResources().getColor(R.color.DarkGray));
                    }else{

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        txt_name_client_ordersheet =  (TextView) v.findViewById(R.id.txt_name_client_ordersheet);
        txt_name_client_ordersheet.setText(orderSheetResponse.getClient().getName());

        txt_total_value_ordersheet = (TextView) v.findViewById(R.id.txt_total_value_ordersheet);
        txt_total_value_ordersheet.setText(orderSheetResponse.getTotalValue());

        txt_cellphone_client_ordersheet = (TextView) v.findViewById(R.id.txt_cellphone_client_ordersheet);
        txt_cellphone_client_ordersheet.setText(orderSheetResponse.getClient().getCellphone());

        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        txt_date_ordersheet = (TextView) v.findViewById(R.id.txt_date_ordersheet);
        txt_date_ordersheet.setText(fmt.format(orderSheetResponse.getDateCreate()).toString());

        try {
            recyclerView = (RecyclerView) v.findViewById(R.id.recycle_view_ordersheet_edition);

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            recyclerAdapter = new RecyclerProductOrdersheetAdapter(orderSheetResponse.getProducts());
            recyclerView.setAdapter(recyclerAdapter);

        }catch (Exception e){
         e.printStackTrace();
        }


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        if(orderSheetResponse.getStatus() == 0){
            buttonOpenBottomProducts.setEnabled(false);
            buttonOpenBottomProducts.setBackgroundColor(buttonOpenBottomProducts.getContext().getResources().getColor(R.color.DarkGray));
            buttonCloseOrdersheet.setEnabled(false);
            buttonCloseOrdersheet.setBackgroundColor(buttonCloseOrdersheet.getContext().getResources().getColor(R.color.DarkGray));
        }
        return v;
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        OrderSheetEdition orderSheetEdition = new OrderSheetEdition();
        OrdersheetApi ordersheetApi = RetrofitApi.getInstance().getOrdersheetApi();

        Call<OrderSheetResponse> call = ordersheetApi.getOrdersheetById(orderSheetResponse.getId().toString());

        try {
            OrderSheetResponse order = call.clone().execute().body();
            orderSheetEdition.setOrderSheetResponse(orderSheetResponse);
            orderSheetResponse.setProducts(order.getProducts());
            recyclerAdapter = new RecyclerProductOrdersheetAdapter(order.getProducts());
            txt_total_value_ordersheet.setText(order.getTotalValue());
            recyclerView.setAdapter(recyclerAdapter);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    ProductResponse deleteProduct = null;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            switch (direction){
                case  ItemTouchHelper.LEFT:
                    deleteProduct =  orderSheetResponse.getProducts().get(position);
                    orderSheetResponse.getProducts().remove(position);
                    recyclerAdapter.notifyItemRemoved(position);

                    //todo delete product
                    try {
                    ProductApi productApi = RetrofitApi.getInstance().getProductApi();
                    Call<Boolean> call3 = productApi.deleteProduct(deleteProduct.getIdOrderSheetProduct());


                        Boolean result =   call3.clone().execute().body();

                        OrderSheetEdition orderSheetEdition = new OrderSheetEdition();
                        OrdersheetApi ordersheetApi = RetrofitApi.getInstance().getOrdersheetApi();

                        Call<OrderSheetResponse> call4 = ordersheetApi.getOrdersheetById(orderSheetResponse.getId().toString());

                        try {
                            OrderSheetResponse order = call4.clone().execute().body();
                            orderSheetEdition.setOrderSheetResponse(order);
                            recyclerAdapter = new RecyclerProductOrdersheetAdapter(order.getProducts());
                            txt_total_value_ordersheet.setText(order.getTotalValue());
                            recyclerView.setAdapter(recyclerAdapter);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case  ItemTouchHelper.RIGHT:

                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


}

