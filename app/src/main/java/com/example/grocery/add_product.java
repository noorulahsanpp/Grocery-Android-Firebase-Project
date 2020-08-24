package com.example.grocery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class add_product extends AppCompatActivity {
    private static final String TAG = "AddProduct";

    private String pName = "", pCategory = "", pOther = "";
    private float pPrice = 0;
    private int pQuantity = 0;
    private EditText nameET, categoryET, priceET, quantityET, otherET;
    private Button addBT;
    private ProgressDialog progressDialog;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_product);

        initWidgets();

        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

        addBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                if (validate(pName, pPrice, pCategory, pOther, pQuantity)){
                    addProduct();
                }

            }
        });

//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(openGallery, 1000 );
//            }
//        });
    }

    public void addProduct(){
        try {
            DocumentReference product = firebaseFirestore.collection("stores").document("A").collection("products").document();
            Map<String, Object> productinfo = new HashMap<>();
            productinfo.put("name", pName);
            productinfo.put("quantity", pQuantity);
            productinfo.put("category", pCategory);
            productinfo.put("other", pOther);
            productinfo.put("price", pPrice);
            product.set(productinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    startActivity(new Intent(add_product.this, product_details.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(add_product.this, "Something went wrong. Please try again later." , Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Log.d(TAG, "addProduct: "+e);
        }
    }

//    private void uploadImageToFirebase(Uri imageUri)
//    {
//        final StorageReference fileReference = storageReference.child("users/"+ userID+"/profile_picture/profile.jpg");
//        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        setProfilePicture(uri);
//                    }
//                });
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(Profile.this, "Try again", Toast.LENGTH_SHORT);
//                    }
//                });
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == 1000)
//        {
//            if(resultCode == Activity.RESULT_OK)
//            {
//                Uri imageUri = data.getData();
//                uploadImageToFirebase(imageUri);
//            }
//        }
//    }


    public void getData(){
        pName = nameET.getText().toString().trim();
        pCategory = categoryET.getText().toString().trim();
        pOther = otherET.getText().toString().trim();
        pQuantity = Integer.parseInt(quantityET.getText().toString().trim());
        pPrice = Float.parseFloat(priceET.getText().toString().trim());
    }

    public Boolean validate(String name, float price, String category, String other, int quantity){
        if (TextUtils.isEmpty(name)){
            nameET.setError("Input name");
            nameET.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(price+"")){
            priceET.setError("Input price");
            priceET.requestFocus();
            return false;
        }
        else if(price<=0){
            priceET.setError("Inavlid Input");
            priceET.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(category)){
            categoryET.setError("Invalid Input");
            categoryET.requestFocus();
            return false;
        }
//        else if(TextUtils.isEmpty(other)){
//            otherET.setError("Invalid Input");
//            otherET.requestFocus();
//            return false;
//        }
        else if (TextUtils.isEmpty(quantity+"")){
            priceET.setError("Input price");
            priceET.requestFocus();
            return false;
        }
        else if(quantity<=0){
            priceET.setError("Inavlid Input");
            priceET.requestFocus();
            return false;
        }
        return true;
    }
    private void initWidgets(){
        Log.d(TAG, "initWidgets: Initialising widgets");
        nameET = findViewById(R.id.Name);
        categoryET = findViewById(R.id.Category);
        priceET = findViewById(R.id.Price);
        quantityET = findViewById(R.id.Quantity);
        otherET = findViewById(R.id.Other);
        addBT = findViewById(R.id.button);
    }
}