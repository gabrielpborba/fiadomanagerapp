package com.example.fiadomanager.ui.bottom;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.fiadomanager.MyDialogCloseListener;
import com.example.fiadomanager.R;
import com.example.fiadomanager.api.ProductApi;
import com.example.fiadomanager.api.RetrofitApi;
import com.example.fiadomanager.data.model.product.NewProductRequest;
import com.example.fiadomanager.data.model.product.NewProductResponse;
import com.example.fiadomanager.ui.home.fragment.Product;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Optional;

import retrofit2.Call;

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
            editValue.setText(textValue);
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
                    String teste = "100.11";
                    String newv = value.replaceFirst("^ *", "").replace(" ", "").trim();
                    newProductRequest.setValue(Long.parseLong(teste));
                if(idProduct > 0l){
                    newProductRequest.setIdProduct(idProduct);
                }
                Call<NewProductResponse> callProduct = productApi.newProduct(newProductRequest);


                newProductResponse = callProduct.execute().body();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(newProductResponse.getIdProduct() > 0l){
                    FragmentManager fragment =getFragmentManager();
                    Optional<Fragment> productFragment = fragment.getFragments().stream().filter(fragment1 -> fragment1 instanceof Product).findAny();
                    try {
                        ((MyDialogCloseListener)productFragment.get()).handleDialogClose(getDialog());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    dismiss();
                }else{

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
