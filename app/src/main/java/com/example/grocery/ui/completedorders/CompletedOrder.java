package com.example.grocery.ui.completedorders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.grocery.OrderAdapter;
import com.example.grocery.R;
import com.example.grocery.ui.product.ProductAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompletedOrder extends Fragment {
    public static final String MyPREFERENCES = "MyPrefs";
    TextView noOrderTv;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private ItemAdapter itemAdapter;
    String userId;
    RecyclerView rvItem;
    SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_completed_order, container, false);

        rvItem = root.findViewById(R.id.rv_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvItem.setLayoutManager(layoutManager);
        noOrderTv = root.findViewById(R.id.noOrders);
        noOrderTv.setVisibility(View.INVISIBLE);
        getSharedPreference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("stores").document(userId).collection("completedorder");
        getdata();

        return root;
    }

    public void getSharedPreference(){
        sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "");
    }

    private void getdata(){
        Query query = collectionReference.orderBy("date", Query.Direction.DESCENDING);
        query.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                noOrderTv.setVisibility(View.VISIBLE);
            }
        });
        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();
        itemAdapter = new ItemAdapter(options,userId);
        rvItem.setAdapter(itemAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        itemAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        itemAdapter.stopListening();
    }
}