package com.example.grocery.ui.product;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.content.Intent;
import com.example.grocery.EditProducts;

import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
;import java.util.Collections;
import java.util.List;
import com.example.grocery.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class ProductAdapter extends FirestoreRecyclerAdapter<Products, ProductAdapter.ProductHolder>{

    List<String> itemname = new ArrayList<>();


    public ProductAdapter(@NonNull FirestoreRecyclerOptions<Products> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductHolder holder, final int position, @NonNull final Products products) {
        holder.price.setText(products.getPrice());
        String productname = products.getName();
       holder.topic.setText(productname);
    itemname.add(productname);
        String imageUrl = products.getImages();
        Picasso.get().load(imageUrl).into(holder.image);
    }



    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_list, parent, false);

        return new ProductHolder(view);
    }


    class ProductHolder extends RecyclerView.ViewHolder {
        TextView topic;
        TextView price;
        ImageView image;

            public ProductHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(),EditProducts.class);
                    i.putExtra("name",itemname.get(getAdapterPosition()));
                    view.getContext().startActivity(i);
                }
            });

            topic = itemView.findViewById(R.id.itemname);
            price = itemView.findViewById(R.id.itemprice);
            image = itemView.findViewById(R.id.itemimage);


        }
    }

}
