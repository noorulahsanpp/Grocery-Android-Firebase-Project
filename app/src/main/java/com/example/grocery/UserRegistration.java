package com.example.grocery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class UserRegistration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "UserRegistration";
    private static final int REQUEST_LOCATION = 1;

    private ImageView photo;
    private EditText phoneET, nameET, locationET, ownerNAmeET;
    private Spinner categoryET;
    private Button signUpBtn;
    private ProgressDialog progressDialog;
    private TextView register;
    private String sName="", sLocation="", sCategory="", saveCurrentDate,saveCurrentTime,downloadImageUrl;
    private String phoneNo, userId, uName;


    private Uri imageUri;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference mStorageRef;
    private FirebaseUser user;

    private String latitude, longitude;
    private LocationManager locationManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_registration);

        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        progressDialog = new ProgressDialog(this);
        initWidgets();
        setSpinner();

        phoneNo = getIntent().getStringExtra("phoneNo");
        phoneET.setText(phoneNo);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        userId = mAuth.getUid();
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Registering your Store");
                progressDialog.setMessage("Please wait while we register your store");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                getData();
                if (validate(sName, sCategory, sLocation, uName)){
                    uploadImage();
                }
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

    }

    private void openGallery(){
        CropImage.activity().setAspectRatio(1, 1).start(UserRegistration.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            Picasso.get().load(imageUri).into(photo);
        }
    }

    private void uploadImage(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        String productRandomKey = saveCurrentDate+saveCurrentTime;

        final StorageReference filepath = mStorageRef.child("merchants/"+userId+"/storeimage/"+productRandomKey+".jpg");
        final UploadTask uploadTask = filepath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserRegistration.this, "Error : "+e.getMessage(), Toast.LENGTH_LONG);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UserRegistration.this, "Image successfully uploaded", Toast.LENGTH_LONG);

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
                            Toast.makeText(UserRegistration.this, "got imageuri successfully", Toast.LENGTH_LONG);
                            saveData();
                        }
                    }
                });
            }
        });
    }

    public void saveData(){
        try {
            DocumentReference product = firebaseFirestore.collection("stores").document(userId+"");
            Map<String, Object> productinfo = new HashMap<>();
            productinfo.put("storename", sName);
            productinfo.put("userid", userId);
            productinfo.put("category", sCategory);
            productinfo.put("location", sLocation);
            productinfo.put("phone", phoneNo);
            productinfo.put("username", uName);
            productinfo.put("storeimage", downloadImageUrl);
            product.set(productinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    startActivity(new Intent(UserRegistration.this, home.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserRegistration.this, "Something went wrong. Please try again later." , Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Log.d(TAG, "addProduct: "+e);
        }
    }

    public Boolean validate(String name, String category, String location, String uname){
        if (TextUtils.isEmpty(name)){
            nameET.setError("Input name");
            nameET.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(category)){
                   Toast.makeText(getApplicationContext(), "Please choose category", Toast.LENGTH_LONG);
                   return false;
               }
        else if(category.equals("Category")){
            Toast.makeText(getApplicationContext(), "Please choose category", Toast.LENGTH_LONG);
            return false;
        }
        else if (TextUtils.isEmpty(location)){
            locationET.setError("Input Location");
            locationET.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(uname)){
            nameET.setError("Input Owner name");
            nameET.requestFocus();
            return false;
        }
        return true;
    }

    private void initWidgets() {
        Log.d(TAG, "initWidgets: Initialising widgets");
        signUpBtn = findViewById(R.id.button4);
        register = findViewById(R.id.textView);
        nameET = findViewById(R.id.name);
        locationET = findViewById(R.id.location);
        phoneET = findViewById(R.id.phone);
        phoneET.setEnabled(false);
        photo = findViewById(R.id.image);
        categoryET = findViewById(R.id.category);
        ownerNAmeET = findViewById(R.id.ownername);
    }

    public void getData(){
        sName = nameET.getText().toString().trim();
        sLocation = locationET.getText().toString().trim();
        uName = ownerNAmeET.getText().toString().trim();
    }

    public void setSpinner() {
        categoryET.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<>();
        categories.add("Category");
        categories.add("General Store");
        categories.add("Electronics");
        categories.add("Home Appliances");
        categories.add("Fruits and vegetables");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryET.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        sCategory = parent.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

