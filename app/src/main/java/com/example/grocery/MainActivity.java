package com.example.grocery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;


import com.example.grocery.ui.orders.ViewOrderFragment;
import com.example.grocery.ui.product.ProductDetailsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import android.widget.ImageView;
import android.widget.TextView;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private SharedPreferences sharedPreferences;
    private static final String TAG = "product_details2";
    public static final String MyPREFERENCES = "MyPrefs" ;

    private String userId,name,image,details;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private ImageView Navimage;
    private TextView storename;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null)
        {
            Intent intent = new Intent(MainActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        firebaseFirestore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();


        setSharedPreferences();

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
     navigationView = findViewById(R.id.nav_view);
        setStoreDetails();
    firebaseFirestore.collection("stores").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
          @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                      name = document.get("storename").toString()+("\n")+document.get("category").toString()+("\n")+document.get("location").toString()+(" ")+document.get("phone").toString();;
                    image = document.get("storeimage").toString();
                   storename.setText(name);
                   Picasso.get().load(image).into(Navimage);
          }

           }
       });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_product, R.id.nav_order,R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);


//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//
//            @Override
//            public boolean onNavigationItemSelected(MenuItem item) {
//                if (item.getItemId() == R.id.nav_logout) {
//                    logout();
//                    drawer.closeDrawers(); // close nav bar
//                }
//               else if(item.getItemId() == R.id.nav_product){
//
//
//                }
//                else if(item.getItemId() == R.id.nav_order){
//
//
//                }
//                return false;
//            }   });

}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setSharedPreferences(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userid", userId);
        editor.commit();
    }

    private void logout(){
        mAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        return;
    }

    private void setStoreDetails(){
        storename = navigationView.getHeaderView(0).findViewById(R.id.nav_shopname);
        Navimage = navigationView.getHeaderView(0).findViewById(R.id.nav_Image);
    }
}