package com.example.grocery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class add_product extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_product);

    }
}