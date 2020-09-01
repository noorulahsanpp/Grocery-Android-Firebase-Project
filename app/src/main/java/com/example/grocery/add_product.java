package com.example.grocery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class add_product extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "AddProduct";

    public static final String MyPREFERENCES = "MyPrefs" ;
    private static final int GalleryPick = 1000;

    private Uri imageUri;


    private String pName = "", pCategory = "", pOther = "",saveCurrentDate,saveCurrentTime,downloadImageUrl;
    private ImageView image;
    private float pPrice = 0;
    private int pQuantity = 0;
    private EditText nameET, priceET, quantityET;
    private Button addBT;
    private ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    private String userId;

    private FirebaseFirestore firebaseFirestore;
    private StorageReference mStorageRef;
    private Spinner categorySp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_product);
        initWidgets();
        setSpinner();
        getSharedPreference();

        firebaseFirestore = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        addBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Adding Product");
                progressDialog.setMessage("Please wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                getData();
                if (validate(pName, pPrice, pQuantity)){
                    addProductInformation();
                }
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    private void openGallery(){
        CropImage.activity().setAspectRatio(1, 1).start(add_product.this);
//        Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(openGallery, GalleryPick );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data !=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            Picasso.get().load(imageUri).into(image);
        }
//        if(requestCode == 1000)
//        {
//            if(resultCode == Activity.RESULT_OK)
//            {
//                imageUri = data.getData();
//                Picasso.get().load(imageUri).into(image);
//            }
//        }
    }

    public void setSpinner(){
        categorySp.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<>();
        categories.add("Category");
        categories.add("Groceries");
        categories.add("Electronics");
        categories.add("Home Appliances");
        categories.add("Fruits and vegetables");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySp.setAdapter(adapter);
    }

    public void addProduct(){
        try {
            DocumentReference product = firebaseFirestore.collection("stores").document(userId+"").collection("products").document();
            Map<String, Object> productinfo = new HashMap<>();
            productinfo.put("name", pName);
            productinfo.put("quantity", pQuantity);
            productinfo.put("category", pCategory);
            productinfo.put("date", saveCurrentDate);
            productinfo.put("price", pPrice);
            productinfo.put("image", downloadImageUrl);
            product.set(productinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    startActivity(new Intent(add_product.this, product_details.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(add_product.this, "Something went wrong. Please try again later." , Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Log.d(TAG, "addProduct: "+e);
        }
    }

    private void addProductInformation(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        String productRandomKey = saveCurrentDate+saveCurrentTime;

        final StorageReference filepath = mStorageRef.child("merchants/"+userId+"/prodcuts/"+productRandomKey+".jpg");
        final UploadTask uploadTask = filepath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(add_product.this, "Error : "+e.getMessage(), Toast.LENGTH_LONG);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(add_product.this, "Image successfully uploaded", Toast.LENGTH_LONG);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(add_product.this, "got imageuri successfully", Toast.LENGTH_LONG);
                            addProduct();
                        }
                    }
                });
            }
        });
    }

    public void getData(){
        pName = nameET.getText().toString().trim();
        pQuantity = Integer.parseInt(quantityET.getText().toString().trim());
        pPrice = Float.parseFloat(priceET.getText().toString().trim());
    }

    public Boolean validate(String name, float price, int quantity){
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
//        else if(TextUtils.isEmpty(category)){
 //           categorySp.setError("Invalid Input");
 //           categorySp.requestFocus();
 //           return false;
 //       }

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
//        else if(imageUri==null){
//            Toast.makeText(this, "Product image is mandatory.",Toast.LENGTH_LONG);
//            return false;
//        }

        return true;
    }
    private void initWidgets(){
        Log.d(TAG, "initWidgets: Initialising widgets");
        nameET = findViewById(R.id.Name);
        categorySp = findViewById(R.id.Category);
        priceET = findViewById(R.id.Price);
        quantityET = findViewById(R.id.Quantity);
        addBT = findViewById(R.id.button);
        image = findViewById(R.id.image);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        String item = parent.getItemAtPosition(i).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void getSharedPreference() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "");
    }
}