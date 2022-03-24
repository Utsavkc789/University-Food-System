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

public class SearchByLocation extends AppCompatActivity {
    ListView location_listView;
    ArrayList<String> location_arrayList = new ArrayList<>();
    ArrayAdapter<String> location_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbylocation);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        EditText location_EditText = findViewById(R.id.location_EditText);

        location_listView = findViewById(R.id.location_listView);

        CollectionReference restaurants = db.collection("Restaurants");

        location_adapter = new ArrayAdapter<>(SearchByLocation.this,
                android.R.layout.simple_list_item_1, location_arrayList);
        location_listView.setAdapter(location_adapter);

        restaurants.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        String name = document.getString("Name");
                        location_arrayList.add(name);
                        location_adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        location_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        location_adapter.getItem(position), Toast.LENGTH_SHORT).show();
                String go_to = location_adapter.getItem(position).toString();
                Intent intent = new Intent(SearchByLocation.this, MenuSelection.class);
                intent.putExtra("key_RNAME", go_to);
                startActivity(intent);
                // SWITCH INTENT TO RESTAURANT IF CLICKED
            }
        });

        Button filter_Button = (Button) findViewById(R.id.filter_Button);
        filter_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String location_name = location_EditText.getText().toString();

                location_adapter.clear();
                location_arrayList.clear();
                restaurants.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                String loc = document.getString("Location");
                                String name = document.getString("Name");

                                if((loc.toLowerCase()).matches(location_name.toLowerCase()))
                                {
                                    location_arrayList.add(name);
                                    location_adapter.notifyDataSetChanged(); // NOTIFY ADAPTER THAT ITEMS CHANGED
                                }
                            }
                        }
                    }
                });
            }
        });

        Button search_by_name_Button = (Button) findViewById(R.id.search_by_name_Button);
        search_by_name_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchByLocation.this, Search.class);
                startActivity(intent);
            }

        });

        Button search_by_item_Button = (Button) findViewById(R.id.search_by_item_Button);
        search_by_item_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchByLocation.this, SearchByItem.class);
                startActivity(intent);
            }

        });

        Button search_by_location_homeButton = (Button) findViewById(R.id.search_by_location_homeButton);
        search_by_location_homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SearchByLocation.this, Homescreen.class);
                startActivity(intent);
            }
        });
    }
}
