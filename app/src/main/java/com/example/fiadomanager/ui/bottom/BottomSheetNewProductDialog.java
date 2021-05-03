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
import com.example.fiadomanager.api.ProductApi;
import com.example.fiadomanager.api.RetrofitApi;
import com.example.fiadomanager.data.model.exception.ExceptionDTO;
import com.example.fiadomanager.data.model.product.NewProductRequest;
import com.example.fiadomanager.data.model.product.NewProductResponse;
import com.example.fiadomanager.ui.home.fragment.Product;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONObject;

import java.util.Optional;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class BottomSheetNewProductDialog extends BottomSheetDialogFragment {

    BottomSheetNewProductDialog bottomSheetNewProductDialog;
    Button buttonCreateProduct;
    EditText editDescription;
    EditText editValue;
    String textDescription;
    String textValue;
    long idProduct;


    public void setBottomSheetClientDialog(BottomSheetNewProductDialog bottomSheetNewProductDialog) {
        this.bottomSheetNewProductDialog = bottomSheetNewProductDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_new_product, container, false);


        editDescription = (EditText) view.findViewById(R.id.editTextDescription);
        editValue = (EditText) view.findViewById(R.id.editProductValue);


        if(null != textValue){
            editDescription.setText(textDescription);
            editValue.setText(textValue.trim());
        }
        buttonCreateProduct = (Button) view.findViewById(R.id.btn_create_product);
        buttonCreateProduct.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Long result = 0l;
                NewProductResponse newProductResponse = new NewProductResponse();
                try {
                ProductApi productApi = RetrofitApi.getInstance().getProductApi();
                NewProductRequest newProductRequest = new NewProductRequest();
                    newProductRequest.setDescription(editDescription.getText().toString());

                    String value = editValue.getText().toString();
                    newProductRequest.setValue(value.replace(" ", ""));
                if(idProduct > 0l){
                    newProductRequest.setIdProduct(idProduct);
                }

                Call<NewProductResponse> callProduct = productApi.newProduct(newProductRequest);

                Response<NewProductResponse> response = callProduct.clone().execute();

                if(response.errorBody() != null) {

                    ResponseBody responseBody = response.errorBody();
                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                    ExceptionDTO exceptionDTO = new ExceptionDTO(jsonObject.get("httpStatus").toString(),jsonObject.get("errorMessage").toString());

                    Toast toast =  Toast.makeText(view.getContext(), exceptionDTO.getErrorMessage(),
                            Toast.LENGTH_LONG);
                    toast.show();
                } else{

                    FragmentManager fragment =getFragmentManager();
                    Optional<Fragment> productFragment = fragment.getFragments().stream().filter(fragment1 -> fragment1 instanceof Product).findAny();
                    try {
                        ((MyDialogCloseListener)productFragment.get()).handleDialogClose(getDialog());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    dismiss();

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
        Optional<Fragment> fragmentProduct = fragment.getFragments().stream().filter(fragment1 -> fragment1 instanceof Product).findAny();
        ((MyDialogCloseListener)fragmentProduct.get()).handleDialogClose(dialog);
    }


    public BottomSheetNewProductDialog getBottomSheetNewProductDialog() {
        return bottomSheetNewProductDialog;
    }

    public void setBottomSheetNewProductDialog(BottomSheetNewProductDialog bottomSheetNewProductDialog) {
        this.bottomSheetNewProductDialog = bottomSheetNewProductDialog;
    }

    public Button getButtonCreateProduct() {
        return buttonCreateProduct;
    }

    public void setButtonCreateProduct(Button buttonCreateProduct) {
        this.buttonCreateProduct = buttonCreateProduct;
    }

    public EditText getEditDescription() {
        return editDescription;
    }

    public void setEditDescription(EditText editDescription) {
        this.editDescription = editDescription;
    }

    public EditText getEditValue() {
        return editValue;
    }

    public void setEditValue(EditText editValue) {
        this.editValue = editValue;
    }

    public String getTextDescription() {
        return textDescription;
    }

    public void setTextDescription(String textDescription) {
        this.textDescription = textDescription;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(long idProduct) {
        this.idProduct = idProduct;
    }
}
