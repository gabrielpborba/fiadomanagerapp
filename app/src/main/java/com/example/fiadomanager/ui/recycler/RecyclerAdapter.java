package com.example.fiadomanager.ui.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiadomanager.R;
import com.example.fiadomanager.data.model.ordersheet.OrderSheetResponse;
import com.example.fiadomanager.ui.home.fragment.OrderSheetEdition;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String TAG = "RecyclerAdapter";
    List<OrderSheetResponse> ordersheetLists;

    public RecyclerAdapter(List<OrderSheetResponse> ordersheetLists){
        this.ordersheetLists = ordersheetLists;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        try {
            holder.txt_dt_ordersheet.setText(fmt.format(ordersheetLists.get(position).getDateCreate()));
            holder.txt_name.setText(ordersheetLists.get(position).getClient().getName().toString());
            holder.txt_value.setText(ordersheetLists.get(position).getTotalValue().toString());
        }catch (Exception e){
            e.printStackTrace();
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                OrderSheetEdition orderSheetEdition = new OrderSheetEdition();
                orderSheetEdition.setOrderSheetResponse(ordersheetLists.get(position));
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, orderSheetEdition).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersheetLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_dt_ordersheet, txt_value, txt_name;
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            txt_dt_ordersheet= itemView.findViewById(R.id.txt_dt_ordersheet);
            txt_name= itemView.findViewById(R.id.txt_name);
            txt_value = itemView.findViewById(R.id.txt_value);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {


        }
    }


}
