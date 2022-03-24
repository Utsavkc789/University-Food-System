package com.vogella.android.universityfoodsystem;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ModifyMenu extends AppCompatActivity {
    ListView modifymenu_listView;
    ArrayList<String> modifymenu_arrayList = new ArrayList<>();
    ArrayAdapter<String> modifymenu_adapter;

    String rname;
    ArrayList<String> infoStuff = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifymenu);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            rname = extras.getString("RestaurantName");
            //The key argument here must match that used in the other activity
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        modifymenu_listView = findViewById(R.id.modifymenu_listView);

        CollectionReference restaurants = db.collection("MenuItem");

        modifymenu_adapter = new ArrayAdapter<>(ModifyMenu.this,
                android.R.layout.simple_list_item_1, modifymenu_arrayList);
        modifymenu_listView.setAdapter(modifymenu_adapter);

        TextView restaurantNameDisplay = (TextView) findViewById(R.id.modifymenu_textView_id);
        TextView restaurantLocDisplay = (TextView) findViewById(R.id.mod_loc_TextView);
        TextView restaurantTimeDisplay = (TextView) findViewById(R.id.mod_OC_TextView);

        db.collection("Restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                String curr_rest = document.getString("Name");

                                if(curr_rest.matches(rname))
                                {
                                    String rloc = document.getString("Location");
                                    String rOpen = document.getString("OpenTime");
                                    String rClose = document.getString("CloseTime");
                                    //rtime = document.getString("Time")

                                    infoStuff.add(rloc);
                                    infoStuff.add(rOpen);
                                    infoStuff.add(rClose);

                                    restaurantLocDisplay.setText("Location: " + infoStuff.get(0));
                                    restaurantTimeDisplay.setText("Open: " + infoStuff.get(1) + ", Close: " + infoStuff.get(2));
                                }
                            }
                        }
                    }
                });

        restaurantNameDisplay.setText("MODIFY FOR: " + rname);

        restaurants.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        String curr_rest = document.getString("Restaurant");

                        if(curr_rest.matches(rname))
                        {
                            String name = document.getString("Name");
                            modifymenu_arrayList.add(name);
                            modifymenu_adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

        modifymenu_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        modifymenu_adapter.getItem(position), Toast.LENGTH_SHORT).show();
                String menuItem_name = modifymenu_adapter.getItem(position).toString();
                Intent intent = new Intent(ModifyMenu.this, ItemLookup.class);
                Bundle extras = new Bundle();
                extras.putString("key_6RNAME", rname);
                extras.putString("key_6MINAME", menuItem_name);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        Button add_item_Button = (Button) findViewById(R.id.add_item_Button);
        add_item_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModifyMenu.this, AddItem.class);
                intent.putExtra("key_5RNAME", rname);
                startActivity(intent);
            }

        });

        Button mod_atts_Button = (Button) findViewById(R.id.mod_atts_Button);
        mod_atts_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModifyMenu.this, ModifyAttributes.class);
                intent.putExtra("key_7RNAME", rname);
                startActivity(intent);
            }

        });

        Button modifymenu_homeButton = (Button) findViewById(R.id.modifymenu_homeButton);
        modifymenu_homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ModifyMenu.this, RestaurantHomescreen.class);
                intent.putExtra("RestaurantName", rname);
                startActivity(intent);
            }
        });
    }
}
