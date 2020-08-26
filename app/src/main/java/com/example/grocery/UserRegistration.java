package com.example.grocery;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UserRegistration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "UserRegistration";
    private ImageView photo;
    private EditText passwordET, phoneET, nameET, locationET, ownerNAmeET;
    private Spinner categoryET;
    private Button signUpBtn;
    private ProgressDialog progressDialog;
    private TextView register;
    private String sName="", sLocation="", sCategory="", sPassword="";
    private String phoneNo, userId, uName;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_registration);

        progressDialog = new ProgressDialog(this);
        initWidgets();
        setSpinner();
        init();

        phoneNo = getIntent().getStringExtra("phoneNo");
        phoneET.setText(phoneNo);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();
    }

    private void init(){
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                if (validate(sName, sCategory, sLocation, uName)){
                   saveData();
                }
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

