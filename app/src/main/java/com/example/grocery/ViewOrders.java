package com.example.grocery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class ViewOrders extends AppCompatActivity {

    ListView vieworder;
    private static final String TAG = "Orders";
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String orderno;
   String[] orderarray = new String[100];
    private Timestamp timestamp;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);
        firebaseFirestore = FirebaseFirestore.getInstance();
        initwidgets();
        getorders();
        listclick();



    }
    public void initwidgets(){
        vieworder= findViewById(R.id.orders);

    }

    private void listclick(){
        vieworder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String ordernumber = parent.getItemAtPosition(position).toString();
                Intent intent = new Intent(ViewOrders.this, OrderDetails.class);
                intent.putExtra("ordernumber",orderarray[Integer.parseInt(ordernumber)]);
                startActivity(intent);
            }
        });
    }

    private void getorders(){

        final List<HashMap<String,String>> listitems = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(this,listitems,R.layout.order_list,new String[]{"FirstLine","SecondLine"},new int[]{R.id.orderno,R.id.date});
        collectionReference = firebaseFirestore.collection("stores").document("lnFz0deqnAJ6miENaL01").collection("orders");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot :task.getResult()) {
                        orderno= documentSnapshot.get("orderno").toString();
                        timestamp = documentSnapshot.getTimestamp("date");
                        Date date = timestamp.toDate();
                        String date1 = simpleDateFormat.format(date);
                        Map<Object, Object> productdetails = new HashMap<>();
                        productdetails.put(orderno,date);
                        Iterator it = productdetails.entrySet().iterator();
                        HashMap<String, String> resultmap = new HashMap<>();
                        Map.Entry pair = (Map.Entry) it.next();
                        resultmap.put("FirstLine", pair.getKey().toString());
                        resultmap.put("SecondLine", pair.getValue().toString());
                        listitems.add(resultmap);
                        vieworder.setAdapter(adapter);
                    }
                    getorderno(listitems);
                }else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }



            }});
    }
    private void getorderno(List<HashMap<String, String>> listitems) {
        for (int i = 0; i < listitems.size(); i++) {
            orderarray[i] = String.valueOf(listitems.get(i));
        }
    }



}