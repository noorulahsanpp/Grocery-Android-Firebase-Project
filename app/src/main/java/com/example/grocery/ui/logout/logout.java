package com.example.grocery.ui.logout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.grocery.Login;
import com.example.grocery.R;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocery.home;
import com.google.firebase.auth.FirebaseAuth;
public class logout extends Fragment {

    private static final String TAG = "home";
    private FirebaseAuth mAuth;
    public static final String MyPREFERENCES = "MyPrefs";
    private Button signOutBT, addProduct, productDetailsBT, mapBT;
    private TextView userIdTV;
    private String userId;
    private Boolean exit;
    SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_home, container, false);
        signOutBT = root.findViewById(R.id.button3);
        // getSharedPreference();
        return root;


        //  mAuth = FirebaseAuth.getInstance();

    /*    if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(getContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        userId = mAuth.getCurrentUser().getUid();
        userIdTV.setText(userId);

        signOutBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getContext(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
               // finish();
                return;
            }
        });
    }

        public void getSharedPreference(){
            sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            userId = sharedPreferences.getString("userid", "");
        }*/

    }
}