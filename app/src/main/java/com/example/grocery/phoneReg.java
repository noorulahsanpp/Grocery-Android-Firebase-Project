package com.example.grocery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class phoneReg extends AppCompatActivity {

    private EditText phoneET;
    private Button registerBT;
    private String sPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_reg);

        phoneET = findViewById(R.id.editTextPhone);
        registerBT = findViewById(R.id.button2);

        registerBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sPhone = "+91"+phoneET.getText().toString().trim();
                Intent intent = new Intent(getApplicationContext(),OTP.class);
                intent.putExtra("phoneNo", sPhone);
                startActivity(intent);
            }
        });

    }
}