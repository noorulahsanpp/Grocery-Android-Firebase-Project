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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UserRegistration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "UserRegistration";
    private ImageView photo;
    private EditText passwordET, phoneET, nameET, locationET;
    private Spinner categoryET;
    private Button signUpBtn;
    private ProgressDialog progressDialog;
    private TextView register;
    private String sName="", sLocation="", sCategory="", sPassword="";
    private int sPhone=0;

    private FirebaseAuth mAuth;




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



    }

    private void init(){
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                if (validate(sName, sPhone, sCategory, sLocation, sPassword)){
                    Intent intent = new Intent(getApplicationContext(),OTP.class);
                    intent.putExtra("phoneNo", sPhone);
                    startActivity(intent);
                }

            }
        });
    }

    public void sendOTP(int phoneNumber) {


    }


    public Boolean validate(String name, int phone, String category, String location, String password){
        if (TextUtils.isEmpty(name)){
            nameET.setError("Input name");
            nameET.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(phone+"")){
            phoneET.setError("Input Input");
            phoneET.requestFocus();
            return false;
        }
        else if(Integer.toString(phone).length()<=10){
            phoneET.setError("Inavlid Input");
            phoneET.requestFocus();
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
        if (TextUtils.isEmpty(password)){
            passwordET.setError("Input Password");
            passwordET.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(location)){
            passwordET.setError("Input Location");
            passwordET.requestFocus();
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
        passwordET = findViewById(R.id.password);
        phoneET = findViewById(R.id.phone);
        photo = findViewById(R.id.image);
        categoryET = findViewById(R.id.category);
    }

    public void getData(){
        sName = nameET.getText().toString().trim();
        sLocation = locationET.getText().toString().trim();
        sPhone = Integer.parseInt(phoneET.getText().toString().trim());
        sPassword = passwordET.getText().toString().trim();

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

