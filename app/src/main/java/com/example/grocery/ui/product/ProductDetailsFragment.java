package com.example.grocery.ui.product;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocery.R;
import com.example.grocery.add_product;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProductDetailsFragment extends Fragment {


  private static final String TAG = "product_details2";
  public static final String MyPREFERENCES = "MyPrefs";

  SharedPreferences sharedPreferences;
  private RecyclerView recyclerView;
  private com.google.android.material.floatingactionbutton.FloatingActionButton add;
  private String userId;
  private FirebaseFirestore firebaseFirestore;
  private CollectionReference collectionReference;
  private ProductAdapter adapter;


  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.product_details, container, false);

    add = root.findViewById(R.id.button);
    recyclerView = root.findViewById(R.id.recyclerview);

    getSharedPreference();
    firebaseFirestore = FirebaseFirestore.getInstance();
    collectionReference = firebaseFirestore.collection("stores").document(userId+"").collection("products");
    getproducts();

    add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getContext(), add_product.class));
      }
    });
    return root;
  }

  public void getSharedPreference(){
    sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    userId = sharedPreferences.getString("userid", "");
  }

  private void getproducts(){

    Query query =collectionReference.orderBy("name", Query.Direction.ASCENDING);
    FirestoreRecyclerOptions<Products> options = new FirestoreRecyclerOptions.Builder<Products>()
            .setQuery(query, Products.class)
            .build();

    adapter = new ProductAdapter(options);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(adapter);

  }


  @Override
  public void onStart() {
    super.onStart();
    adapter.startListening();
  }

  @Override
  public void onStop() {
    super.onStop();
    adapter.stopListening();
  }

}

