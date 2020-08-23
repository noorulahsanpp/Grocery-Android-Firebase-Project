package com.example.grocery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends AppCompatActivity {

    EditText e1,e2,e3,e4;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void register(View view){

        Intent i=new Intent(this,Registration1.class);
        Bundle b=new Bundle();
        e1=(EditText)findViewById(R.id.Name);
        e2=(EditText)findViewById(R.id.location);
        e3=(EditText)findViewById(R.id.Phone);
        e4=(EditText)findViewById(R.id.Password);;


        String s1=e1.getText().toString();
        String s2=e2.getText().toString();
        String s3=e3.getText().toString();
        String s4=e4.getText().toString();

        b.putString("msg1",s1);
        b.putString("msg2",s2);
        b.putString("msg3",s3);
        b.putString("msg4",s4);
        i.putExtras(b);
        startActivity(i);
        Toast t=Toast.makeText(getApplicationContext(),"registered",Toast.LENGTH_LONG);
        t.show();
    }
}