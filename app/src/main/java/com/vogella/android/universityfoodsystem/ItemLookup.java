//should display the options based on information from database
package com.vogella.android.universityfoodsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ItemLookup extends AppCompatActivity{
    String restaurant_name;
    String menuItem_name;
    String menuItem_price;
    String menuItem_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlookup);

        TextView promo_TextView = (TextView) findViewById(R.id.promoLKdesc);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            menuItem_name = extras.getString("key_6MINAME");
            restaurant_name = extras.getString("key_6RNAME");
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        TextView itemName_TextView = (TextView) findViewById(R.id.itemLKname);
        TextView itemDescription_TextView = (TextView) findViewById(R.id.itemLKdesc);
        TextView itemPrice_TextView = (TextView) findViewById(R.id.itemLKprice);

        // Query Database for Item price and description
        CollectionReference restaurants = db.collection("MenuItem");
        restaurants.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        String curr_rest = document.getString("Restaurant");
                        String curr_item = document.getString("Name");
                        String has_promo = document.getString("Promo");

                        if(curr_rest.matches(restaurant_name) && curr_item.matches(menuItem_name))
                        {
                            menuItem_price = Double.toString(document.getDouble("Price"));
                            menuItem_description = document.getString("Description");

                            if(has_promo.matches("Yes"))
                            {
                                promo_TextView.setText("*Promotional/seasonal item!");
                            }
                            else
                            {
                                promo_TextView.setText("*Item is not promotional/seasonal.");
                            }

                            itemPrice_TextView.setText("Price: $" + menuItem_price);
                            itemDescription_TextView.setText(menuItem_description);
                        }
                    }
                }
            }
        });

        itemName_TextView.setText(menuItem_name);

        Button Lookup_Back_Button = (Button) findViewById(R.id.Lookup_Back_Button);
        Lookup_Back_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ItemLookup.this, ModifyMenu.class);
                intent.putExtra("RestaurantName", restaurant_name);
                startActivity(intent);
            }
        });

        Button delete_Button = (Button) findViewById(R.id.delete_this_Button);
        delete_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String delete_item_name = menuItem_name;

                // QUERY DATABASE FOR MENU ITEM AND DELETE
                restaurants.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                String curr_rest = document.getString("Restaurant");
                                String curr_item = document.getString("Name");

                                if(curr_rest.matches(restaurant_name) && delete_item_name.matches(curr_item))
                                {
                                    document.getReference().delete();
                                }
                            }
                        }
                    }
                });

                itemPrice_TextView.setText("");
                itemDescription_TextView.setText("ITEM HAS BEEN DELETED");
                promo_TextView.setText("Please Return to Menu");

                Toast.makeText(getApplicationContext(), "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
