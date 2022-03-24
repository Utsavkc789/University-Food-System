package com.vogella.android.universityfoodsystem;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Locale;

public class Search extends AppCompatActivity {
    ListView SBN_listView;
    ArrayList<String> SBN_arrayList = new ArrayList<>();
    ArrayAdapter<String> SBN_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        EditText SBN_EditText = findViewById(R.id.SBN_EditText);

        SBN_listView = findViewById(R.id.SBN_listView);

        CollectionReference restaurants = db.collection("Restaurants");
        CollectionReference menuCollection = db.collection("MenuItem");

        SBN_adapter = new ArrayAdapter<>(Search.this,
                android.R.layout.simple_list_item_1, SBN_arrayList);
        SBN_listView.setAdapter(SBN_adapter);

        restaurants.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        String name = document.getString("Name");
                        SBN_arrayList.add(name);
                        SBN_adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        SBN_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        SBN_adapter.getItem(position), Toast.LENGTH_SHORT).show();
                String go_to = SBN_adapter.getItem(position).toString();
                Intent intent = new Intent(Search.this, MenuSelection.class);
                intent.putExtra("key_RNAME", go_to);
                startActivity(intent);
                // SWITCH INTENT TO RESTAURANT IF CLICKED
            }
        });

        Button SBN_filter_Button = (Button) findViewById(R.id.SBN_filter_Button);
        SBN_filter_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String rest_name = SBN_EditText.getText().toString();

                SBN_adapter.clear();
                SBN_arrayList.clear();
                restaurants.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                String name = document.getString("Name");

                                if((name.toLowerCase()).contains(rest_name.toLowerCase()))
                                {
                                    SBN_arrayList.add(name);
                                    SBN_adapter.notifyDataSetChanged(); // NOTIFY ADAPTER THAT ITEMS CHANGED
                                }
                            }
                        }
                    }
                });
            }
        });

        Button SBN_SBI_Button = (Button) findViewById(R.id.SBN_SBI_Button);
        SBN_SBI_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Search.this, SearchByItem.class);
                startActivity(intent);
            }

        });

        Button SBN_SBL_Button = (Button) findViewById(R.id.SBN_SBL_Button);
        SBN_SBL_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Search.this, SearchByLocation.class);
                startActivity(intent);
            }

        });

        Button SBN_homeButton = (Button) findViewById(R.id.search_homeButton);
        SBN_homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, Homescreen.class);
                startActivity(intent);
            }
        });
    }
}
