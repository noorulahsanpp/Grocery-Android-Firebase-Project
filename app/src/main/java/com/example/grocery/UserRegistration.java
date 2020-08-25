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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRegistration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "UserRegistration";
    private ImageView photo;
    private EditText password, phone, name, location;
    private Spinner category;
    private Button signUpBtn;
    private ProgressDialog progressDialog;
    private TextView register;


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
                startActivity(new Intent(getApplicationContext(),OTP.class));
            }
        });
    }

    private void initWidgets() {
        Log.d(TAG, "initWidgets: Initialising widgets");
        signUpBtn = findViewById(R.id.button4);
        register = findViewById(R.id.textView);
        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        photo = findViewById(R.id.image);
        category = findViewById(R.id.category);
    }

    public void setSpinner() {
        category.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<>();
        categories.add("Category");
        categories.add("General Store");
        categories.add("Electronics");
        categories.add("Home Appliances");
        categories.add("Fruits and vegetables");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);


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
}

