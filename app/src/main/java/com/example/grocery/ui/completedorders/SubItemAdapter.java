package com.example.grocery.ui.completedorders;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocery.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder> {

    static ArrayList<String> subImage = new ArrayList<>();
    static ArrayList<String> subPrice = new ArrayList<>();
    public static ArrayList<String> subName = new ArrayList<>();
    public static  ArrayList<String> subquantity = new ArrayList<>();

    public SubItemAdapter(ArrayList<String> itemName, ArrayList<String> itemQuantity, ArrayList<String> itemImages, ArrayList<String> itemPrices) {
        subName = itemName;
        subPrice = itemPrices;
        subquantity = itemQuantity;
        subImage = itemImages;
    }

    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_sub_item, viewGroup, false);
        return new SubItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SubItemViewHolder holder, int position) {
        holder.productnameTv.setText(subName.get(position)+ "\n\n" + "₹"+ subPrice.get(position) + "\n" + "Qty: " +subquantity.get(position));
        String imageUrl = subImage.get(position);
        Picasso.get().load(imageUrl).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return subName.size();
    }


    static class SubItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView productnameTv;

        SubItemViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.itemimage);
            productnameTv = itemView.findViewById(R.id.productname);
        }
    }
}