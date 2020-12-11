package com.example.grocery.ui.orders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.grocery.OrderDetails;
import com.example.grocery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ViewOrderFragment extends Fragment {

    public static final String MyPREFERENCES = "MyPrefs";
    ListView vieworder;
    private static final String TAG = "Orders";
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userId,orderno;
    private SharedPreferences sharedPreferences;

    private Timestamp timestamp;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    final List<HashMap<String, String>> listitems = new ArrayList<>();
    final List<String> orderarray = new ArrayList<String>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_view_orders, container, false);
        getSharedPreference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        vieworder = root.findViewById(R.id.orders);
        getorders(userId);
        listclick();

        return root;
    }


    private void listclick() {

        vieworder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), OrderDetails.class);
                intent.putExtra("ordernumber", orderarray.get(i));
                startActivity(intent);

            }
        });

    }

    private void getorders(String userId) {


        final SimpleAdapter adapter = new SimpleAdapter(getContext(), listitems, R.layout.order_list, new String[]{"FirstLine", "SecondLine"}, new int[]{R.id.orderno, R.id.date});
        collectionReference = firebaseFirestore.collection("stores").document(userId).collection("order");
        collectionReference.whereEqualTo("status", "order placed").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        orderno = document.getId();
                        timestamp = document.getTimestamp("date");
                           Date date = timestamp.toDate();
                        String date1 = simpleDateFormat.format(date);
                        Map<Object, Object> productdetails = new HashMap<>();
                        productdetails.put(orderno, date1);
                        Iterator it = productdetails.entrySet().iterator();
                        HashMap<String, String> resultmap = new HashMap<>();
                        Map.Entry pair = (Map.Entry) it.next();
                        resultmap.put("FirstLine", pair.getKey().toString());
                        resultmap.put("SecondLine", pair.getValue().toString());
                        listitems.add(resultmap);
                        orderarray.add(orderno);
                        vieworder.setAdapter(adapter);

                    }
             }

        });
    }
    public void getSharedPreference(){
        sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "");
    }
}






