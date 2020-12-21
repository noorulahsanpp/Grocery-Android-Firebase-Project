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
import com.example.grocery.ui.orders.ViewOrderFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.grocery.R.id.itemimage;

public class OrderDetails extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static TextView tv_cus_name,tv_cus_phone,tvOrderno,tvTotal,tvPaymentstatus;
    private String customer,phoneno,ordernumber, userId,orderid;
    private Button orderBtn;
    private RecyclerView rvOrderlist;
    static ArrayList<String> images = new ArrayList<>();
    static ArrayList<String> prices = new ArrayList<>();
    public ArrayList<String> itemname = new ArrayList<>();
    public ArrayList<String> quantity = new ArrayList<>();
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private SharedPreferences sharedPreferences;
    private FirebaseFirestore firebaseFirestore;
    private Timestamp date;

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
        init();
        getdata(ordernumber);

    }

    private void init() {
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setcompletedorder();
                collectionReference = firebaseFirestore.collection("stores").document(userId).collection("order");
                collectionReference.document(ordernumber)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                Toast.makeText(getApplicationContext(), "Order Completed.", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

    public void setcompletedorder(){
    collectionReference = firebaseFirestore.collection("stores").document(userId).collection("completedorder");
    Map<String, Object> products = new HashMap<>();
    products.put("itemname", itemname);
    products.put("orderid",orderid);
    products.put("customerphone",phoneno);
    products.put("customername", customer);
    products.put("itemno", quantity);
    products.put("date", date);
    products.put("itemimage", images);
    products.put("itemprice", prices);
    products.put("status", "order ready");
    collectionReference.document().set(products);

}

    private void initwidgets(){
        tv_cus_name = findViewById(R.id.customer);
        tv_cus_phone = findViewById(R.id.phoneno);
        orderBtn = findViewById(R.id.Orderrdy);
        rvOrderlist = findViewById(R.id.orders);
        tvOrderno = findViewById(R.id.orderno);
        rvOrderlist.setHasFixedSize(true);
        rvOrderlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tvPaymentstatus = findViewById(R.id.paystatus);
        tvTotal = findViewById(R.id.total);
        tvOrderno.setText(ordernumber);

    }


    private void getdata(final String ordernumber) {
        documentReference = firebaseFirestore.collection("stores").document(userId).collection("order").document(ordernumber);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String status = document.get("status").toString();
                        if (status.equals("order placed")) {
                            orderid=ordernumber;
                            itemname =(ArrayList<String>) document.get("name");
                            customer= (String) document.get("customername");
                            phoneno = (String) document.get("phone");
                            quantity =(ArrayList<String>) document.get("itemno");
                            images =(ArrayList<String>) document.get("image");
                            prices =(ArrayList<String>) document.get("price");
                            date = document.getTimestamp("date");

                        }
                    }
                }
                tv_cus_phone.setText(phoneno);
                tv_cus_name.setText(customer);
                OrderAdapter adapter = new OrderAdapter(itemname,quantity,images,prices);
                rvOrderlist.setAdapter(adapter);
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
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}