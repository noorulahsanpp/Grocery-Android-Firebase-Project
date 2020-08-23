package com.example.grocery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class product_details extends AppCompatActivity {
    private static final String TAG = "Notification";
    private static final int ACTIVITY_NUM = 1;

    private ListView listView;
    private Button add;
    private String name[]={"Sabola", "Asirvad Atta", "Eastern","Lifebouy","Nirapara"};
    private String price[]={"Rs290/-","Rs300/-","Rs20/-","Rs10/-","Rs70/-"};
    int images[] = {R.drawable.sabola ,R.drawable.ashirwad, R.drawable.eastern,R.drawable.lifebouy,R.drawable.nirapara};
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.product_details);
        listView = findViewById(R.id.listview);
        add = findViewById(R.id.button);
        MyAdapter adapter = new MyAdapter(this, name, price, images);
        listView.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), add_product.class));
            }
        });

    }

        class MyAdapter extends ArrayAdapter<String> implements com.example.grocery.MyAdapter {

            Context context;
            String rTitle[];
            String rDescription[];
            int rImgs[];

            MyAdapter (Context c, String title[], String description[], int imgs[]) {
                super(c, R.layout.list_items,title);
                this.context = c;
                this.rTitle = title;
                this.rDescription = description;
                this.rImgs = imgs;

            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View row = layoutInflater.inflate(R.layout.list_items, parent, false);
                ImageView images = row.findViewById(R.id.image);
                TextView myTitle = row.findViewById(R.id.topic);
                TextView myDescription = row.findViewById(R.id.price);

                // now set our resources on views
                images.setImageResource(rImgs[position]);
                myTitle.setText(rTitle[position]);
                myDescription.setText(rDescription[position]);




                return row;
            }
        }
    }
