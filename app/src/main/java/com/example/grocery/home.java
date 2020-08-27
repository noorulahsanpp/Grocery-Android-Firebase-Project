package com.example.grocery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class home extends AppCompatActivity {
    private static final String TAG = "home";
    private FirebaseAuth mAuth;

    private Button signOutBT, addProduct;
    private TextView userIdTV;

    private String userId = "Feqpwrb7W9frPEUjXO6Y97EXujC3";

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initWidgets();
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()==null)
        {
            Intent intent = new Intent(home.this, phoneReg.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        userId = mAuth.getCurrentUser().getUid();
        userIdTV.setText(userId);
        setSharedPreferences();

        signOutBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(home.this, phoneReg.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return;
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, add_product.class));
            }
        });
    }

    public void initWidgets(){
        signOutBT = findViewById(R.id.button3);
        userIdTV = findViewById(R.id.textView2);
        addProduct = findViewById(R.id.button5);
    }
    public void setSharedPreferences(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userid", userId);
        editor.commit();
    }
}