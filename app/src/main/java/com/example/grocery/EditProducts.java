package com.example.grocery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import android.widget.ImageView;
import com.example.grocery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import android.widget.TextView;
public class EditProducts extends AppCompatActivity {
    private static final String TAG = "EditProduct";
    public static final String MyPREFERENCES = "MyPrefs" ;

    private TextView  priceET, quantityET, categoryET,nameEt;
    private EditText newEt;
    private Button editBT, saveBT, deleteBT;
    private ProgressDialog progressDialog;
    private ImageView productImage;
    private String name, category, quantity,productname, userId, imageUri, productId;
    private Double price;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
            getSupportActionBar().hide();
            setContentView(R.layout.activity_edit_products);
            firebaseFirestore = FirebaseFirestore.getInstance();
            initWidgets();
            getSharedPreference();
            Intent intent = getIntent();
            productname = intent.getExtras().get("name").toString();
          getData(productname);

          deleteBT.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  deleteProduct();
              }
          });

          saveBT.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  getData();
              }
          });

          editBT.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  priceET.setEnabled(true);
                  categoryET.setEnabled(true);
              }
          });


    }

    private void initWidgets() {
        Log.d(TAG, "initWidgets: Initialising widgets");
        newEt = findViewById(R.id.newquantity);
        nameEt = findViewById(R.id.name);
        categoryET = findViewById(R.id.Category);
        priceET = findViewById(R.id.Price);
        quantityET = findViewById(R.id.Quantity);
        editBT = findViewById(R.id.Edit);
        saveBT = findViewById(R.id.SAVE);
        deleteBT = findViewById(R.id.Delete);
        productImage = findViewById(R.id.image1);

    }

    public void deleteProduct(){
        firebaseFirestore.collection("stores").document(userId+"").collection("products").document(productId+"").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Product successfully deleted", Toast.LENGTH_LONG);
                startActivity(new Intent(EditProducts.this, MainActivity.class));
            }
        });
    }
    private void getData(String productname){
        firebaseFirestore.collection("stores").document(userId+"").collection("products").whereEqualTo("name", productname+"").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }
                for (QueryDocumentSnapshot document : value) {
                    productId = document.getId();
                    name = String.valueOf(document.get("name"));
                    category = String.valueOf(document.get("category"));
                    quantity = String.valueOf(document.get("quantity"));
                    price = (Double) document.get("price");
                    imageUri = String.valueOf(document.get("image"));
                }
                setData();
            }
        });

    }


    public void getData(){
        category = categoryET.getText().toString().trim();
        price = Double.parseDouble(String.valueOf(priceET.getText()));
        int newqty = 0;
                newqty= Integer.parseInt(newEt.getText().toString());
                int qty = Integer.parseInt(quantity)+newqty;
                quantity = String.valueOf(qty);
        firebaseFirestore.collection("stores").document(userId+"").collection("products").document(productId+"").update(
                "price", Double.parseDouble(String.valueOf(price)),
                "category", category+"",
                "quantity", Integer.parseInt(quantity)
        );
    }

    public void setData(){
        nameEt.setText(name);
        priceET.setText(price+"");
        quantityET.setText(quantity);
        categoryET.setText(category);
        newEt.setText("0");
        priceET.setEnabled(false);
        categoryET.setEnabled(false);
        quantityET.setEnabled(false);
        Picasso.get().load(imageUri).into(productImage);
    }

    public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "");
    }

}