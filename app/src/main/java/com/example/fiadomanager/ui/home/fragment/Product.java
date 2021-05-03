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
import android.widget.Toast;

import com.example.fiadomanager.ui.bottom.BottomSheetNewProductDialog;
import com.example.fiadomanager.MyDialogCloseListener;
import com.example.fiadomanager.R;
import com.example.fiadomanager.ui.recycler.RecyclerProductAdapter;
import com.example.fiadomanager.api.ProductApi;
import com.example.fiadomanager.api.RetrofitApi;
import com.example.fiadomanager.data.model.exception.ExceptionDTO;
import com.example.fiadomanager.data.model.product.ProductList;
import com.example.fiadomanager.data.model.product.ProductResponse;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Product#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Product extends Fragment implements MyDialogCloseListener{

    RecyclerView recyclerView;
    RecyclerProductAdapter recyclerAdapter;

    ProductList products;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Product() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Product.
     */
    // TODO: Rename and change types and number of parameters
    public static Product newInstance(String param1, String param2) {
        Product fragment = new Product();
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
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_product);

        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            ProductApi productApi = RetrofitApi.getInstance().getProductApi();

            Call<ProductList> call = productApi.getAllProcuts();

            Response<ProductList> response = call.clone().execute();

            if(response.errorBody() != null){
                ResponseBody responseBody = response.errorBody();
                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                ExceptionDTO exceptionDTO = new ExceptionDTO(jsonObject.get("httpStatus").toString(),jsonObject.get("errorMessage").toString());

                Toast toast =  Toast.makeText(view.getContext(), exceptionDTO.getErrorMessage(),
                        Toast.LENGTH_LONG);
                toast.show();

                products = new ProductList(new ArrayList<>());

            }else{
                products = response.body();
                recyclerAdapter = new RecyclerProductAdapter(products.getProducts());
                recyclerView.setAdapter(recyclerAdapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        setHasOptionsMenu(true);

        Button buttonOpenNewProduct = (Button) view.findViewById(R.id.btn_add_procut);
        buttonOpenNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetNewProductDialog bottomSheetProductDialog = new BottomSheetNewProductDialog();
                bottomSheetProductDialog.setBottomSheetNewProductDialog(bottomSheetProductDialog);
                bottomSheetProductDialog.show(getFragmentManager(), "tag");
            }
        });



        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            switch (direction){
                case  ItemTouchHelper.LEFT:
                    recyclerAdapter.notifyItemRemoved(position);
                    break;
                case  ItemTouchHelper.RIGHT:
                    BottomSheetNewProductDialog bottomSheetNewProductDialog = new BottomSheetNewProductDialog();
                    ProductResponse product =  products.getProducts().get(position);
                    bottomSheetNewProductDialog.setTextDescription(product.getDescription());
                    String value = product.getValue().toString();
                    bottomSheetNewProductDialog.setTextValue(value.replace("R$", "").replace("  ", ""));
                    bottomSheetNewProductDialog.setIdProduct(product.getId());
                    bottomSheetNewProductDialog.setBottomSheetClientDialog(bottomSheetNewProductDialog);
                    bottomSheetNewProductDialog.show(getFragmentManager(), "tagProduct");

                    break;
            }
        }
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.Silver))
                    .addSwipeRightActionIcon(R.drawable.ic_edit_client)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        try {
            ProductApi productApi = RetrofitApi.getInstance().getProductApi();

            Call<ProductList> call = productApi.getAllProcuts();

            products = call.clone().execute().body();
            if(null == products){
                recyclerAdapter = new RecyclerProductAdapter(new ArrayList<ProductResponse>());
            }else{
                recyclerAdapter = new RecyclerProductAdapter(products.getProducts());
            }

            recyclerView.setAdapter(recyclerAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}