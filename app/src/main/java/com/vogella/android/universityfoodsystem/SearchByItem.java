package com.vogella.android.universityfoodsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchByItem extends AppCompatActivity {
    ListView SITEM_listView;
    ArrayList<String> SITEM_arrayList = new ArrayList<>();
    ArrayAdapter<String> SITEM_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbyitem);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        EditText SITEM_EditText = findViewById(R.id.SITEM_EditText);

        SITEM_listView = findViewById(R.id.SITEM_listView);

        CollectionReference restaurants = db.collection("Restaurants");
        CollectionReference menuCollection = db.collection("MenuItem");

        SITEM_adapter = new ArrayAdapter<>(SearchByItem.this,
                android.R.layout.simple_list_item_1, SITEM_arrayList);
        SITEM_listView.setAdapter(SITEM_adapter);

        restaurants.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        String name = document.getString("Name");
                        SITEM_arrayList.add(name);
                        SITEM_adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        SITEM_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        SITEM_adapter.getItem(position), Toast.LENGTH_SHORT).show();
                String go_to = SITEM_adapter.getItem(position).toString();
                Intent intent = new Intent(SearchByItem.this, MenuSelection.class);
                intent.putExtra("key_RNAME", go_to);
                startActivity(intent);
                // SWITCH INTENT TO RESTAURANT IF CLICKED
            }
        });

        Button SITEM_filter_Button = (Button) findViewById(R.id.SITEM_filter_Button);
        SITEM_filter_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String item_name = SITEM_EditText.getText().toString();

                SITEM_adapter.clear();
                SITEM_arrayList.clear();
                menuCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                String item = document.getString("Name");
                                String name = document.getString("Restaurant");

                                if((item.toLowerCase()).contains(item_name.toLowerCase()) && !(SITEM_arrayList.contains(name)))
                                {
                                    SITEM_arrayList.add(name);
                                    SITEM_adapter.notifyDataSetChanged(); // NOTIFY ADAPTER THAT ITEMS CHANGED
                                }
                            }
                        }
                    }
                });
            }
        });

        Button SITEM_SBN_Button = (Button) findViewById(R.id.SITEM_SBN_Button);
        SITEM_SBN_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchByItem.this, Search.class);
                startActivity(intent);
            }

        });

        Button SITEM_SBL_Button = (Button) findViewById(R.id.SITEM_SBL_Button);
        SITEM_SBL_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchByItem.this, SearchByLocation.class);
                startActivity(intent);
            }

        });

        Button SITEM_homeButton = (Button) findViewById(R.id.search_by_item_homeButton);
        SITEM_homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SearchByItem.this, Homescreen.class);
                startActivity(intent);
            }
        });
    }
}

