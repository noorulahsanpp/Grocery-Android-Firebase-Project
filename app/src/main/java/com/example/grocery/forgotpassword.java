package com.example.grocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {

  private EditText emailEt;
  private Button send;
  private FirebaseAuth mAuth;
  private ProgressDialog progressdialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
    getSupportActionBar().hide();
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forgotpassword);
    popup();

    emailEt = findViewById(R.id.emailEt);
    send = findViewById(R.id.sendpswd);


    send.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String email = emailEt.getText().toString();
        forgotpswd(email);
      }
    });
  }

  private void forgotpswd(String email) {
    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        progressdialog.show();
        if (task.isSuccessful()) {
          Toast.makeText(getApplicationContext(), "Password send to your email", Toast.LENGTH_LONG).show();
        } else {
          Toast.makeText(getApplicationContext(), "Email not exist", Toast.LENGTH_LONG).show();
        }

      }
    });
  }

  private void popup() {
    DisplayMetrics dm = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(dm);

    int width = dm.widthPixels;
    int height = dm.heightPixels;

    getWindow().setLayout((int) (width * .9), (int) (height * .6));

    WindowManager.LayoutParams params = getWindow().getAttributes();
    params.gravity = Gravity.CENTER;
    params.x = 0;
    params.y = -20;
    getWindow().setAttributes(params);
  }
}