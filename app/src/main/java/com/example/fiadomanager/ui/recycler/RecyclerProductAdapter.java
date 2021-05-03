package com.example.fiadomanager.ui.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiadomanager.R;
import com.example.fiadomanager.data.model.product.ProductResponse;

import java.util.List;

public class RecyclerProductAdapter extends RecyclerView.Adapter<RecyclerProductAdapter.ViewHolder> {

    private static final String TAG = "ReciclerAdapter";
    List<ProductResponse> productList;
    public RecyclerProductAdapter(List<ProductResponse> productList){
        this.productList = productList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item_product, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.txt_product_description.setText(productList.get(position).getDescription().toString());
        holder.txt_product_value.setText(productList.get(position).getValue().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_product_description, txt_product_value;
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            txt_product_description= itemView.findViewById(R.id.txt_product_description);
            txt_product_value= itemView.findViewById(R.id.txt_product_value_row);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            System.out.println("sasasasa");

        }
    }


}
