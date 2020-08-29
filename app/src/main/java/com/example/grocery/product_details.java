package com.example.grocery;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class product_details extends AppCompatActivity {
    private static final String TAG = "product_details";
    public static final String MyPREFERENCES = "MyPrefs" ;

    SharedPreferences sharedPreferences;
    private ListView listView;
    private Button add;
    private String productname, price, userId;
    int images[] = {R.drawable.sabola ,R.drawable.ashirwad, R.drawable.eastern,R.drawable.lifebouy,R.drawable.nirapara};
    private CollectionReference collectionReference;
    private FirebaseFirestore firebaseFirestore;
    final List<HashMap<String,String>> listitems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.product_details);
        getSharedPreference();

        firebaseFirestore = FirebaseFirestore.getInstance();
        initwidgets();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), add_product.class));
            }
        });
        getproducts();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String productname = parent.getItemAtPosition(position).toString().trim();
                Intent intent = new Intent(product_details.this, EditProducts.class);
                intent.putExtra("productname",productname);
                startActivity(intent);
            }
        });




    }

    public void initwidgets(){
        listView = findViewById(R.id.listview);
        add = findViewById(R.id.button);
    }


    private void getproducts(){


        final SimpleAdapter adapter = new SimpleAdapter(this,listitems,R.layout.products_list,new String[]{"FirstLine","SecondLine"},new int[]{R.id.topic,R.id.price});
        collectionReference = firebaseFirestore.collection("stores").document(userId+"").collection("products");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot :task.getResult()) {
                        productname = documentSnapshot.get("name").toString();
                        price = "Rs"+documentSnapshot.get("price").toString()+"/-";
                        Map<Object, Object> productdetails = new HashMap<>();
                        productdetails.put(productname, price);
                        Iterator it = productdetails.entrySet().iterator();
                        HashMap<String, String> resultmap = new HashMap<>();
                        Map.Entry pair = (Map.Entry) it.next();
                        resultmap.put("FirstLine", pair.getKey().toString());
                        resultmap.put("SecondLine", pair.getValue().toString());
                        listitems.add(resultmap);
                        listView.setAdapter(adapter);
                    }
                }else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
            }
        }});
    }
    public void getSharedPreference() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "");
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(product_details.this, home.class));
    }

}




