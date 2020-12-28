package com.example.grocery.ui.orders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocery.OrderDetails;
import com.example.grocery.R;
import com.example.grocery.ui.product.ProductAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ViewOrderFragment extends Fragment {
    public  final String MyPREFERENCES = "MyPrefs";

    private CollectionReference collectionReference;
    private FirebaseFirestore firebaseFirestore;
    private String date1,orderno,userId;
    private SharedPreferences sharedPreferences;
    static SimpleDateFormat simpleDateFormat;
    static RecyclerView vieworder;
    final List<String> orderarray = new ArrayList<>();
    final List<String> datearray = new ArrayList<>();
    ViewOrderAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_view_orders, container, false);
        vieworder = root.findViewById(R.id.orders);
        vieworder.setHasFixedSize(true);
        vieworder.setLayoutManager(new LinearLayoutManager(getContext()));

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        getSharedPreference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("stores").document(userId).collection("order");
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        orderarray.clear();
        datearray.clear();
        ViewOrderAdapter.dateArray.clear();
        ViewOrderAdapter.orderArray.clear();
        getorders();
    }

    public void getorders() {
        collectionReference.orderBy("date",Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                   orderno = document.getId();
                   orderarray.add(orderno);
                  Timestamp timestamp = document.getTimestamp("date");
                  Date date = timestamp.toDate();
                  date1 = simpleDateFormat.format(date);
                  datearray.add(date1);
                }
                adapter = new ViewOrderAdapter(orderarray,datearray);
                vieworder.setAdapter(adapter);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void getSharedPreference(){
        sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "");
    }

}






