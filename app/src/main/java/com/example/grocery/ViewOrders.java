package com.example.grocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.grocery.OrderDetails;
import com.example.grocery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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

public class ViewOrders extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    ListView vieworder;
    private static final String TAG = "Orders";
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String orderno,userId;
    private SharedPreferences sharedPreferences;

    private Timestamp timestamp;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    final List<HashMap<String,String>> listitems = new ArrayList<>();
    final List<String> orderarray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);
        firebaseFirestore = FirebaseFirestore.getInstance();
        getSharedPreference();
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
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Intent intent = new Intent(getApplicationContext(), OrderDetails.class);
               intent.putExtra("ordernumber",orderarray.get(i));
               startActivity(intent);

           }
        });

    }

    private void getorders(){


       final SimpleAdapter adapter = new SimpleAdapter(this,listitems,R.layout.order_list,new String[]{"FirstLine","SecondLine"},new int[]{R.id.orderno,R.id.date});
        collectionReference = firebaseFirestore.collection("stores").document(userId).collection("order");
        collectionReference.whereEqualTo("status", "order placed").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot :task.getResult()) {
                        orderno = collectionReference.document().getId();
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
                        orderarray.add(orderno);
                       vieworder.setAdapter(adapter);

                    }


                }else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }



            }});
    }
    public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "");
    }
}





