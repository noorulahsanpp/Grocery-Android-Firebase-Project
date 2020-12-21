package com.example.grocery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocery.ui.product.ProductAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {
    ArrayList<String> image ;
    ArrayList<String> price ;
    ArrayList<String> num;
    ArrayList<String> name ;
    float totalprice = 0;
    public OrderAdapter(ArrayList<String> itemname, ArrayList<String> quantity, ArrayList<String> images, ArrayList<String> prices) {
        name = itemname;
        price = prices;
        num = quantity;
        image = images;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_list, parent, false);

        return new OrderAdapter.OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderHolder holder, int position) {

        holder.tvProductname.setText(name.get(position));
        holder.tvPrice.setText("₹"+price.get(position));
        String amount = price.get(position);
        String imageUrl = image.get(position);
         Picasso.get().load(imageUrl).into(holder.ivItemimage);
        String itemnum = num.get(position);
        holder.tvQuantity.setText(itemnum);
        totalprice = totalprice +( Float.parseFloat(amount)* Integer.parseInt(itemnum));
        OrderDetails.tvTotal.setText("₹"+totalprice);
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder {
        ImageView ivItemimage;
        TextView tvProductname,tvQuantity,tvPrice;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);

            ivItemimage = itemView.findViewById(R.id.itemimage);
            tvProductname = itemView.findViewById(R.id.productname);
            tvQuantity = itemView.findViewById(R.id.quantity);
            tvPrice = itemView.findViewById(R.id.itemprice);
        }
    }
}

