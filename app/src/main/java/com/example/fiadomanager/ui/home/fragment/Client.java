package com.example.fiadomanager.ui.home.fragment;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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

import com.example.fiadomanager.data.model.product.ProductResponse;
import com.example.fiadomanager.ui.bottom.BottomSheetClientDialog;
import com.example.fiadomanager.MyDialogCloseListener;
import com.example.fiadomanager.R;
import com.example.fiadomanager.ui.recycler.RecyclerClientAdapter;
import com.example.fiadomanager.api.ClientApi;
import com.example.fiadomanager.api.RetrofitApi;
import com.example.fiadomanager.data.model.exception.ExceptionDTO;
import com.example.fiadomanager.data.model.client.ClientList;
import com.example.fiadomanager.data.model.client.ClientResponse;
import com.example.fiadomanager.ui.recycler.RecyclerProductAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Client#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Client extends Fragment implements MyDialogCloseListener {

    RecyclerView recyclerView;
    RecyclerClientAdapter recyclerAdapter;

    ClientList clients;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Client.
     */
    // TODO: Rename and change types and number of parameters
    public static Client newInstance(String param1, String param2) {
        Client fragment = new Client();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Client() {
        // Required empty public constructor
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

        View view = inflater.inflate(R.layout.fragment_client, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_client);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        try {

            ClientApi clientApi = RetrofitApi.getInstance().getClientApi();

            Call<ClientList> callGetClients = clientApi.getClientList();

            Response<ClientList> response = callGetClients.clone().execute();

            if(response.errorBody() != null){
                ResponseBody responseBody = response.errorBody();
                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                ExceptionDTO exceptionDTO = new ExceptionDTO(jsonObject.get("httpStatus").toString(),jsonObject.get("errorMessage").toString());

                Toast toast =  Toast.makeText(view.getContext(), exceptionDTO.getErrorMessage(),
                        Toast.LENGTH_LONG);
                toast.show();

                clients = new ClientList(new ArrayList<>());

            }else{
                clients = response.body();
                recyclerAdapter = new RecyclerClientAdapter(clients.getClientList());
                recyclerView.setAdapter(recyclerAdapter);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        setHasOptionsMenu(true);

        Button buttonOpenNewClient = (Button) view.findViewById(R.id.btn_add_client);
        buttonOpenNewClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetClientDialog bottomSheetClientDialog = new BottomSheetClientDialog();
                bottomSheetClientDialog.setBottomSheetClientDialog(bottomSheetClientDialog);
                bottomSheetClientDialog.show(getFragmentManager(), "tag");
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
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
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

               return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

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
                    recyclerAdapter.notifyItemRemoved(position);
                    ClientApi clientApi = RetrofitApi.getInstance().getClientApi();

                    Call<Boolean> callDisableClient = clientApi.disableClient(clients.getClientList().get(position).getId());

                    try {
                        Boolean result = callDisableClient.clone().execute().body();

                        if(result){
                            Call<ClientList> call = clientApi.getClientList();
                            clients = call.clone().execute().body();
                            recyclerAdapter = new RecyclerClientAdapter(clients.getClientList());
                            recyclerView.setAdapter(recyclerAdapter);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case  ItemTouchHelper.RIGHT:
                    BottomSheetClientDialog bottomSheetClientDialog = new BottomSheetClientDialog();
                    ClientResponse client =  clients.getClientList().get(position);
                    bottomSheetClientDialog.setTextName(client.getName());
                    bottomSheetClientDialog.setTextPhone(client.getCellphone());
                    bottomSheetClientDialog.setIdClient(client.getId());
                    bottomSheetClientDialog.setBottomSheetClientDialog(bottomSheetClientDialog);
                    bottomSheetClientDialog.show(getFragmentManager(), "tag2");
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

            ClientApi clientApi = RetrofitApi.getInstance().getClientApi();

            Call<ClientList> callcallGetClientsCallBack = clientApi.getClientList();

            clients = callcallGetClientsCallBack.clone().execute().body();

            if(null == clients){
                recyclerAdapter = new RecyclerClientAdapter(new ArrayList<ClientResponse>());
            }else{
                recyclerAdapter = new RecyclerClientAdapter(clients.getClientList());
            }

            recyclerView.setAdapter(recyclerAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}