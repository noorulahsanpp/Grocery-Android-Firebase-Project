package com.example.grocery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

public class EditProducts extends AppCompatActivity {
    private static final String TAG = "AddProduct";

    private EditText nameET, priceET, quantityET,categoryET;
    private Button editBT,saveBT,deleteBT;
    private ProgressDialog progressDialog;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_products);
        initWidgets();


    }

    private void initWidgets(){
        Log.d(TAG, "initWidgets: Initialising widgets");
        nameET = findViewById(R.id.Name);
        categoryET = findViewById(R.id.Category);
        priceET = findViewById(R.id.Price);
        quantityET = findViewById(R.id.Quantity);
        editBT = findViewById(R.id.Edit);
        saveBT = findViewById(R.id.SAVE);
        deleteBT = findViewById(R.id.Delete);
    }
}