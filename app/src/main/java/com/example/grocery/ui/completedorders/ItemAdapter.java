package com.example.grocery.ui.completedorders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocery.R;
import com.example.grocery.ui.product.ProductAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends FirestoreRecyclerAdapter<Item,ItemAdapter.ItemViewHolder> {

    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> prices = new ArrayList<>();
    private ArrayList<String> itemname = new ArrayList<>();
    private ArrayList<String> quantity = new ArrayList<>();
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private String userid;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    SubItemAdapter subItemAdapter;

    public ItemAdapter(FirestoreRecyclerOptions<Item> options, String userId) {

        super(options);
        this.userid = userId;
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("stores").document(userid).collection("completedorder");
    }

    @Override
    protected void onBindViewHolder(@NonNull final ItemViewHolder itemViewHolder, int position, @NonNull final Item item) {

        itemViewHolder.tvItemTitle.setText(item.getCustomername());
        itemViewHolder.phone.setText(item.getPhone());
        String orderid = item.getOrderid();
        collectionReference.whereEqualTo("orderid",orderid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    itemname = (ArrayList<String>) document.get("name");
                    quantity = (ArrayList<String>) document.get("itemno");
                    images = (ArrayList<String>) document.get("image");
                    prices = (ArrayList<String>) document.get("price");
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(itemViewHolder.rvSubItem.getContext(), LinearLayoutManager.HORIZONTAL, false);
                itemViewHolder.rvSubItem.setLayoutManager(layoutManager);
                subItemAdapter = new SubItemAdapter(itemname,quantity,images,prices);
                itemViewHolder.rvSubItem.setAdapter(subItemAdapter);
            }
        });
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemTitle,phone;
        private RecyclerView rvSubItem;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.tv_item_title);
            phone = itemView.findViewById(R.id.tv_item_phone);
            rvSubItem = itemView.findViewById(R.id.rv_sub_item);


        }
    }

}