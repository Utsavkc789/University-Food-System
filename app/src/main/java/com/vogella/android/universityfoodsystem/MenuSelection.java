package com.vogella.android.universityfoodsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class MenuSelection extends AppCompatActivity {
    ListView listView;
    ArrayList<String> menu_arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String rname;
    ArrayList<String> infoStuff = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuselection);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            rname = extras.getString("key_RNAME");
            //The key argument here must match that used in the other activity
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        listView = findViewById(R.id.menu_listView);

        TextView restaurantNameDisplay = (TextView) findViewById(R.id.restaurantname_textView_id);
        TextView restaurantLocDisplay = (TextView) findViewById(R.id.resto_loc_TextView);
        TextView restaurantTimeDisplay = (TextView) findViewById(R.id.open_close_TextView);


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

        // Here we set the restaurant name, location, and time to what the user chose
        restaurantNameDisplay.setText(rname);

        CollectionReference restaurants = db.collection("MenuItem");

        adapter = new ArrayAdapter<>(MenuSelection.this,
                android.R.layout.simple_list_item_1, menu_arrayList);
        listView.setAdapter(adapter);

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
                            menu_arrayList.add(name);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        adapter.getItem(position), Toast.LENGTH_SHORT).show();
                // menuItemName is the item name clicked
                String menuItem_name = adapter.getItem(position).toString();
                Intent intent = new Intent(MenuSelection.this, menuItem.class);
                Bundle extras = new Bundle();
                extras.putString("key_4RNAME", rname);
                extras.putString("key_MINAME", menuItem_name);
                intent.putExtras(extras);
                startActivity(intent);
                // NEED TO SWITCH TO MENUITEM ACTIVITY AFTER CLICKING
            }
        });

        // IF BUTTON IS CLICKED. CLEAR ARRAY AND QUERY DATABASE FOR ITEMS THAT MATCH
        Button breakfastButton = (Button) findViewById(R.id.breakfastButton);
        breakfastButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                adapter.clear();
                menu_arrayList.clear();
                // QUERY DATABASE FOR BREAKFAST ITEMS AND ADD TO ARRAY
                restaurants.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                String curr_rest = document.getString("Restaurant");
                                String curr_category = document.getString("Category");

                                if(curr_rest.matches(rname) && curr_category.matches("Breakfast"))
                                {
                                    String name = document.getString("Name");
                                    menu_arrayList.add(name);
                                    adapter.notifyDataSetChanged(); // NOTIFY ADAPTER THAT ITEMS CHANGED
                                }
                            }
                        }
                    }
                });
            }
        });

        Button lunchButton = (Button) findViewById(R.id.lunchButton);
        lunchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                adapter.clear();
                menu_arrayList.clear();
                // QUERY DATABASE FOR BREAKFAST ITEMS AND ADD TO ARRAY
                restaurants.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                String curr_rest = document.getString("Restaurant");
                                String curr_category = document.getString("Category");

                                if(curr_rest.matches(rname) && curr_category.matches("Lunch"))
                                {
                                    String name = document.getString("Name");
                                    menu_arrayList.add(name);
                                    adapter.notifyDataSetChanged(); // NOTIFY ADAPTER THAT ITEMS CHANGED
                                }
                            }
                        }
                    }
                });
            }
        });

        Button dinnerButton = (Button) findViewById(R.id.dinnerButton);
        dinnerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                adapter.clear();
                menu_arrayList.clear();
                // QUERY DATABASE FOR BREAKFAST ITEMS AND ADD TO ARRAY
                restaurants.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                String curr_rest = document.getString("Restaurant");
                                String curr_category = document.getString("Category");

                                if(curr_rest.matches(rname) && curr_category.matches("Dinner"))
                                {
                                    String name = document.getString("Name");
                                    menu_arrayList.add(name);
                                    adapter.notifyDataSetChanged(); // NOTIFY ADAPTER THAT ITEMS CHANGED
                                }
                            }
                        }
                    }
                });
            }
        });

        Button homeButton = (Button) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuSelection.this, Homescreen.class);
                startActivity(intent);
            }
        });

        Button MS_BTS_Button = (Button) findViewById(R.id.MS_BTS_Button);
        MS_BTS_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuSelection.this, Search.class);
                startActivity(intent);
            }
        });

        Button show_all_Button = (Button) findViewById(R.id.show_all_Button);
        show_all_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                adapter.clear();
                menu_arrayList.clear();
                // QUERY DATABASE FOR ALL ITEMS AGAIN
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
                                    menu_arrayList.add(name);
                                    adapter.notifyDataSetChanged(); // NOTIFY ADAPTER THAT ITEMS CHANGED
                                }
                            }
                        }
                    }
                });
            }
        });

        Button promo_Button = (Button) findViewById(R.id.promo_Button);
        promo_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                adapter.clear();
                menu_arrayList.clear();
                // QUERY DATABASE FOR PROMO AND SEASONAL ITEMS
                restaurants.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                String curr_rest = document.getString("Restaurant");
                                String has_promo = document.getString("Promo");

                                if(curr_rest.matches(rname) && has_promo.matches("Yes"))
                                {
                                    String name = document.getString("Name");
                                    menu_arrayList.add(name);
                                    adapter.notifyDataSetChanged(); // NOTIFY ADAPTER THAT ITEMS CHANGED
                                }
                            }
                        }
                    }
                });
            }
        });
    }
}
