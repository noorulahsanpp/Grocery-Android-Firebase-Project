package com.example.grocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grocery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class EditProducts extends AppCompatActivity {
    private static final String TAG = "AddProduct";

    private EditText newET, priceET, quantityET, categoryET,nameEt;

    private Button editBT, saveBT, deleteBT;
    private ProgressDialog progressDialog;
    private String name, category, quantity, price,productname;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
            getSupportActionBar().hide();
            setContentView(R.layout.activity_edit_products);
            firebaseFirestore = FirebaseFirestore.getInstance();
            initWidgets();
            Intent intent = getIntent();
            productname = intent.getExtras().get("name").toString();
          getData(productname);
           setdata();



    }

    private void initWidgets() {
        Log.d(TAG, "initWidgets: Initialising widgets");
        newET = findViewById(R.id.newquantity);
        nameEt = findViewById(R.id.name);
        categoryET = findViewById(R.id.Category);
        priceET = findViewById(R.id.Price);
        quantityET = findViewById(R.id.Quantity);
        editBT = findViewById(R.id.Edit);
        saveBT = findViewById(R.id.SAVE);
        deleteBT = findViewById(R.id.Delete);



    }



  public void getData(String productname) {
       documentReference= firebaseFirestore.collection("stores").document("lnFz0deqnAJ6miENaL01").collection("products").document("p1");
       documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               DocumentSnapshot document = task.getResult();
               if (document.exists()) {
                   name = String.valueOf(document.get("name"));
                   category = String.valueOf(document.get("category"));
                   quantity = String.valueOf(document.get("quantity"));
                   price = String.valueOf(document.get("price"));

               } else {
                   Toast.makeText(getApplicationContext(), "Failed. Please try again.", Toast.LENGTH_LONG).show();
               }
           }
            });
    }
    public void setdata(){
        nameEt.setText(name);
        priceET.setText(price);
        quantityET.setText(quantity);
        categoryET.setText(category);

    }




}