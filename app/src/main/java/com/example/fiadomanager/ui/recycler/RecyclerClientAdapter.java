package com.example.fiadomanager.ui.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiadomanager.R;
import com.example.fiadomanager.data.model.client.ClientResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecyclerClientAdapter extends RecyclerView.Adapter<RecyclerClientAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "RecyclerAdapter";
    List<ClientResponse> clientList;
    List<ClientResponse> clientListAll;
    public RecyclerClientAdapter(List<ClientResponse> clientList){
        this.clientList = clientList;
        this.clientListAll = new ArrayList<>(clientList);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item_client, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.txt_name_client.setText(clientList.get(position).getName().toString());
        holder.txt_cellphone_client.setText(clientList.get(position).getCellphone().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<ClientResponse> filteredList = new ArrayList<>();

            if(clientList != null){
                if(charSequence.toString().isEmpty()){
                    filteredList.addAll(clientListAll);
                }else{
                    for (ClientResponse clientResponse: clientListAll){
                        if(clientResponse.getName().toLowerCase().contains(charSequence.toString().toLowerCase()) || clientResponse.getCellphone().contains(charSequence.toString().toLowerCase())){
                            filteredList.add(clientResponse);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clientList.clear();
            clientList.addAll((Collection<? extends ClientResponse>) results.values);
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_name_client, txt_cellphone_client;
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            txt_name_client= itemView.findViewById(R.id.txt_product_description);
            txt_cellphone_client= itemView.findViewById(R.id.txt_product_value_row);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            System.out.println("sasasasa");

        }
    }


}
