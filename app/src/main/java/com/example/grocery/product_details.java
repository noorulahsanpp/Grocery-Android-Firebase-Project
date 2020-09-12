package com.example.grocery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.grocery.ui.product.ProductAdapter;
import com.example.grocery.ui.product.Products;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
public class product_details extends AppCompatActivity {
    private static final String TAG = "product_details";
    public static final String MyPREFERENCES = "MyPrefs" ;

    private RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    private Button add;
    private String productname, price, userId;
    private String images;

       private FirebaseFirestore firebaseFirestore;

    private CollectionReference collectionReference ;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("stores").document("lnFz0deqnAJ6miENaL01").collection("products");
        initwidgets();

            add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), add_product.class));
            }
        });
        getproducts();


    }

    public void initwidgets(){

        add = findViewById(R.id.button);
    }


    private void getproducts() {

        Query query =collectionReference.orderBy("name", Query.Direction.ASCENDING);
               FirestoreRecyclerOptions<Products> options = new FirestoreRecyclerOptions.Builder<Products>()
                .setQuery(query, Products.class)
                .build();

      //  adapter = new ProductAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}


