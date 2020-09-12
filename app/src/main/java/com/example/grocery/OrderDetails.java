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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {

   private TextView name,phone,orderno;
    private static final String TAG = "Order Details";
   private String customer,phoneno,itemname,quantity,no,ordernumber;

   private Button order;
   private ListView orderlist;
    private String productname[]={"Asirwad Atta 5kg 1", "Eastern Garammasala 250g 1", "Lifebouy soap 2"};
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_details);
        firebaseFirestore = FirebaseFirestore.getInstance();
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


    }


    private void getdata( String ordernumber){
        final List<HashMap<String,String>> listitems = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(this,listitems,R.layout.items_list,new String[]{"FirstLine","SecondLine"},new int[]{R.id.productname,R.id.quantity});
    documentReference= firebaseFirestore.collection("stores").document("lnFz0deqnAJ6miENaL01").collection("orders").document(ordernumber);
    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
         @Override
         public void onComplete(@NonNull Task<DocumentSnapshot> task) {
             DocumentSnapshot document = task.getResult();
             if (document.exists()) {
                 customer = document.get("name").toString();
                 phoneno = document.get("phone").toString();
                 no = document.get("orderno").toString();
                 name.setText(customer);
                 phone.setText(phoneno);
                 orderno.setText(no);
                 documentReference.collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<QuerySnapshot> task) {
                         if (task.isSuccessful()) {
                             for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                 itemname = documentSnapshot.get("name").toString();
                                 quantity = documentSnapshot.get("quantity").toString();
                                 Map<Object, Object> productdetails = new HashMap<>();
                                 productdetails.put(itemname, quantity);
                                 Iterator it = productdetails.entrySet().iterator();
                                 HashMap<String, String> resultmap = new HashMap<>();
                                 Map.Entry pair = (Map.Entry) it.next();
                                 resultmap.put("FirstLine", pair.getKey().toString());
                                 resultmap.put("SecondLine", pair.getValue().toString());
                                 listitems.add(resultmap);
                                 orderlist.setAdapter(adapter);

                             }
                         }
                     }
                 });

             }
             else
             {
                 Toast.makeText(getApplicationContext(), "Doesn't Exist", Toast.LENGTH_LONG).show();
             }
         }
     });
    }

  }