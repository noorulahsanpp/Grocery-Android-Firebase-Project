package com.example.grocery.ui.orders;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocery.EditProducts;
import com.example.grocery.OrderDetails;
import com.example.grocery.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ViewOrderAdapter extends RecyclerView.Adapter<ViewOrderAdapter.ViewHolder> {

  static   List<String> orderArray = new ArrayList<>();
  static   List<String> dateArray = new ArrayList<>();
    public ViewOrderAdapter(List<String> orderarray, List<String> datearray) {
        this.orderArray= orderarray;
        this.dateArray = datearray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ordernoTv.setText(orderArray.get(position));
        holder.dateTv.setText(dateArray.get(position));
    }

    @Override
    public int getItemCount() {
        return orderArray.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView ordernoTv;
        TextView dateTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), OrderDetails.class);
                    intent.putExtra("ordernumber", orderArray.get(getAdapterPosition()));
                    view.getContext().startActivity(intent);
                }
            });

            ordernoTv = itemView.findViewById(R.id.orderno);
            dateTv = itemView.findViewById(R.id.date);
        }
    }
}


