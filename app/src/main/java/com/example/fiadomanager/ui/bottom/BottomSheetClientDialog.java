package com.example.fiadomanager.ui.bottom;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.fiadomanager.MyDialogCloseListener;
import com.example.fiadomanager.R;
import com.example.fiadomanager.api.ClientApi;
import com.example.fiadomanager.api.RetrofitApi;
import com.example.fiadomanager.data.model.exception.ExceptionDTO;
import com.example.fiadomanager.data.model.client.NewClientRequest;
import com.example.fiadomanager.ui.home.fragment.Client;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONObject;

import java.util.Optional;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class BottomSheetClientDialog extends BottomSheetDialogFragment {

    BottomSheetClientDialog bottomSheetClientDialog;
    Button buttonCreateClient;
    EditText editTextPhone;
    EditText editName;
    String textPhone;
    String textName;
    long idClient;
    public void setBottomSheetClientDialog(BottomSheetClientDialog bottomSheetClientDialog) {
        this.bottomSheetClientDialog = bottomSheetClientDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_new_client, container, false);


        editTextPhone = (EditText) view.findViewById(R.id.editProductValue);
        editName = (EditText) view.findViewById(R.id.editTextDescription);


        if(null != textPhone){
            editTextPhone.setText(textPhone);
            editName.setText(textName);
        }
        buttonCreateClient = (Button) view.findViewById(R.id.btn_create_product);
        buttonCreateClient.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Boolean result = false;
                try {

                    if(!validatePhone(editTextPhone.getText().toString())){
                        Toast toast =  Toast.makeText(view.getContext(), "Telefone deve conter apenas numeros",
                                Toast.LENGTH_LONG);
                        toast.show();
                    }else{
                        ClientApi clientApi = RetrofitApi.getInstance().getClientApi();

                        NewClientRequest newClientRequest = new NewClientRequest();
                        newClientRequest.setName(editName.getText().toString());
                        newClientRequest.setCellphone(editTextPhone.getText().toString());
                        if(idClient != 0l){
                            newClientRequest.setIdClient(idClient);
                        }

                        Call<Boolean> call = clientApi.newClient(newClientRequest);
                        Response<Boolean> response = call.clone().execute();

                        if(response.errorBody() != null){
                            ResponseBody responseBody = response.errorBody();
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            ExceptionDTO exceptionDTO = new ExceptionDTO(jsonObject.get("httpStatus").toString(),jsonObject.get("errorMessage").toString());

                            Toast toast =  Toast.makeText(view.getContext(), exceptionDTO.getErrorMessage(),
                                    Toast.LENGTH_LONG);
                            toast.show();

                        }else{
                            result = response.body();
                            if(result){
                                FragmentManager fragment =getFragmentManager();
                                Optional<Fragment> clientFragment = fragment.getFragments().stream().filter(fragment1 -> fragment1 instanceof Client).findAny();
                                try {
                                    ((MyDialogCloseListener)clientFragment.get()).handleDialogClose(getDialog());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                dismiss();
                            }
                        }

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        FragmentManager fragment =getFragmentManager();
        Optional<Fragment> clientFragment = fragment.getFragments().stream().filter(fragment1 -> fragment1 instanceof Client).findAny();
        ((MyDialogCloseListener)clientFragment.get()).handleDialogClose(dialog);
        super.onCancel(dialog);
    }

    private boolean validatePhone (String phone){
        boolean result = true;
        if(!phone.matches("[0-9]+")){
            result = false;
        }

        return result;
    }


    public EditText getEditTextPhone() {
        return editTextPhone;
    }

    public void setEditTextPhone(EditText editTextPhone) {
        this.editTextPhone = editTextPhone;
    }

    public EditText getEditName() {
        return editName;
    }

    public void setEditName(EditText editName) {
        this.editName = editName;
    }

    public long getIdClient() {
        return idClient;
    }

    public void setIdClient(long idClient) {
        this.idClient = idClient;
    }

    public String getTextPhone() {
        return textPhone;
    }

    public void setTextPhone(String textPhone) {
        this.textPhone = textPhone;
    }

    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }
}
