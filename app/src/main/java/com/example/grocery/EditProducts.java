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

    private EditText nameET, priceET, quantityET, categoryET;
    private Button editBT, saveBT, deleteBT;
    private ProgressDialog progressDialog;
    private String name, category, quantity, price,productname;
    private FirebaseFirestore firebaseFirestore;
    private DocumentSnapshot documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_products);
        initWidgets();
        Intent intent = getIntent();
        productname = intent.getStringExtra("product_name");
  //     getData();




    }

    private void initWidgets() {
        Log.d(TAG, "initWidgets: Initialising widgets");
        nameET = findViewById(R.id.name);
        categoryET = findViewById(R.id.Category);
        priceET = findViewById(R.id.Price);
        quantityET = findViewById(R.id.Quantity);
        editBT = findViewById(R.id.Edit);
        saveBT = findViewById(R.id.SAVE);
        deleteBT = findViewById(R.id.Delete);



    }


   /* public void getData() {

            documentReference = firebaseFirestore.collection("stores").document("lnFz0deqnAJ6miENaL01").collection("products").document("NaX0uYfs7PlcBHws1djj").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    name = document.get("name").toString();
                    category = document.get("category").toString();
                    quantity = document.get("quantity").toString();
                    price = document.get("price").toString();
                    nameET.setText(name);
                    categoryET.setText(category);

                }
            });
    }*/

}