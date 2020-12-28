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
import androidx.recyclerview.widget.GridLayoutManager;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends FirestoreRecyclerAdapter<Item,ItemAdapter.ItemViewHolder> {

    private ArrayList<String> itemImages = new ArrayList<>();
    private ArrayList<String> itemPrices = new ArrayList<>();
    private ArrayList<String> itemName = new ArrayList<>();
    private ArrayList<String> itemQuantity = new ArrayList<>();
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
        itemName = item.getItemname();
        itemQuantity = item.getItemno();
        itemImages = item.getItemimage();
        itemPrices = item.getItemprice();
        String date;
        Date date1;
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
        date1 = item.getDate();
        date = simpleDateFormat.format(date1);
        itemViewHolder.tvDate.setText(date);

        itemViewHolder.tvItemTitle.setText(item.getCustomername());
        itemViewHolder.tvPhone.setText(item.getCustomerphone());
        float total =  0;
        for(int i = 0; i< itemQuantity.size() ;i++ ){
            total = total +Integer.parseInt(itemQuantity.get(i))*Float.parseFloat(itemPrices.get(i));
        }
        itemViewHolder.tvTotal.setText("₹" +total);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(itemViewHolder.rvSubItem.getContext(),2,GridLayoutManager.VERTICAL,false);
        itemViewHolder.rvSubItem.setLayoutManager(gridLayoutManager);
        subItemAdapter = new SubItemAdapter(itemName,itemQuantity,itemImages,itemPrices);
                itemViewHolder.rvSubItem.setAdapter(subItemAdapter);
//        itemViewHolder
//                .rvSubItem
//                .setRecycledViewPool(viewPool);

    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemTitle,tvPhone,tvTotal,tvDate;
        private RecyclerView rvSubItem;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.tv_item_title);
            tvPhone = itemView.findViewById(R.id.tv_item_phone);
            rvSubItem = itemView.findViewById(R.id.rv_sub_item);
            tvTotal = itemView.findViewById(R.id.total);
            tvDate = itemView.findViewById(R.id.date);

        }
    }

}