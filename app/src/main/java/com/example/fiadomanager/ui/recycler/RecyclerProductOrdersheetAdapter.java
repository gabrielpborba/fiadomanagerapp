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

public class RecyclerProductOrdersheetAdapter extends RecyclerView.Adapter<RecyclerProductOrdersheetAdapter.ViewHolder> {

    private static final String TAG = "RecyclerAdapter";
    List<ProductResponse> productResponse;

    public RecyclerProductOrdersheetAdapter(List<ProductResponse> productResponse){
        this.productResponse = productResponse;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item_product_ordersheet, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {

            holder.txt_product_value.setText(productResponse.get(position).getValue().toString());
            holder.txt_product_name.setText(productResponse.get(position).getDescription().toString());
            holder.txt_quantity.setText(productResponse.get(position).getQuantity().toString());
        }catch (Exception e){
            e.printStackTrace();
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* AppCompatActivity activity = (AppCompatActivity)v.getContext();
                OrderSheetEdition orderSheetEdition = new OrderSheetEdition();
                orderSheetEdition.setOrderSheetResponse(ordersheetLists.get(position));
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, orderSheetEdition).commit();*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return productResponse.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_product_value, txt_product_name, txt_quantity;
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            txt_product_value= itemView.findViewById(R.id.txt_product_value);
            txt_product_name= itemView.findViewById(R.id.txt_product_name);
            txt_quantity = itemView.findViewById(R.id.txt_quantity);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {


        }
    }


}
