package com.example.grocery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.grocery.R.id.itemimage;

public class OrderDetails extends AppCompatActivity {
 OrderAdapter adapter;
   public static TextView name,phone,orderno,total,paymentstatus;
    private static final String TAG = "Order Details";
   private String customer,phoneno,no,ordernumber, userId;
    public static final String MyPREFERENCES = "MyPrefs" ;
   private Button order;
   private RecyclerView orderlist;
    static ArrayList<String> images = new ArrayList<>();
    static ArrayList<String> prices = new ArrayList<>();
    public static ArrayList<String> itemname = new ArrayList<>();
    public static  ArrayList<String> quantity = new ArrayList<>();
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        setTitle("Order Details");
        firebaseFirestore = FirebaseFirestore.getInstance();
        getSharedPreference();
        Intent intent = getIntent();
       ordernumber = intent.getExtras().get("ordernumber").toString();
        initwidgets();
        getdata(ordernumber);



    }
    private void initwidgets(){
        name = findViewById(R.id.customer);
        phone = findViewById(R.id.phoneno);
        order = findViewById(R.id.Orderrdy);
        orderlist = findViewById(R.id.orders);
        orderno = findViewById(R.id.orderno);
        orderlist.setHasFixedSize(true);
        orderlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        paymentstatus = findViewById(R.id.paystatus);
        total = findViewById(R.id.total);
        orderno.setText(ordernumber);
    }


    private void getdata( String ordernumber) {
        documentReference = firebaseFirestore.collection("stores").document(userId).collection("order").document(ordernumber);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String status = document.get("status").toString();
                        if (status.equals("order placed")) {
                            itemname =(ArrayList<String>) document.get("name");
                            quantity =(ArrayList<String>) document.get("itemno");
                            images =(ArrayList<String>) document.get("image");
                            prices =(ArrayList<String>) document.get("price");

                        }
                    }
                }
                OrderAdapter adapter = new OrderAdapter(itemname,quantity,images,prices);

                orderlist.setAdapter(adapter);
            }
        });

    }

   public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "");
    }

    @Override
    public void onStart() {
        super.onStart();
        //   adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
      //  adapter.stopListening();
    }
}