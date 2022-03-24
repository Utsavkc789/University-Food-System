//should display the options based on information from database
package com.vogella.android.universityfoodsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class menuItem extends AppCompatActivity{
    String restaurant_name;
    String menuItem_name;
    String menuItem_price;
    String menuItem_description;

    FirebaseDatabase mRootRef = FirebaseDatabase.getInstance();
    DatabaseReference mUserRef = mRootRef.getReference("users");
    DatabaseReference mfav_item = mUserRef.child("Fav_item");
    DatabaseReference mfav_Res = mUserRef.child("Fav_res");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);

        TextView promo_TextView = (TextView) findViewById(R.id.promoDescription);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            menuItem_name = extras.getString("key_MINAME");
            restaurant_name = extras.getString("key_4RNAME");
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        TextView itemName_TextView = (TextView) findViewById(R.id.itemTitle);
        TextView itemDescription_TextView = (TextView) findViewById(R.id.itemDescription);
        TextView itemPrice_TextView = (TextView) findViewById(R.id.itemPrice);

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

        Button confirm = findViewById(R.id.confirm_Button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //extract choices and finalize order
                if(opt1 && opt2)
                {
                    Toast.makeText(getApplicationContext(), "Item Added to Order and Favorites List", Toast.LENGTH_SHORT).show();
                }
                else if(opt1)
                {
                    Toast.makeText(getApplicationContext(), "Item Added to Order", Toast.LENGTH_SHORT).show();
                    // do opt 1 stuff
                }
                else if(opt2)
                {
                    Toast.makeText(getApplicationContext(), "Item Added to Favorites List", Toast.LENGTH_SHORT).show();
                    // do opt 2 stuff
                }
            }
        });

        Button back_to_menu_Button = (Button) findViewById(R.id.back_to_menu_Button);
        back_to_menu_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(menuItem.this, MenuSelection.class);
                intent.putExtra("key_RNAME", restaurant_name);
                startActivity(intent);
            }
        });
    }

    // Booleans to denote which check boxes were checked
    Boolean opt1 = false;
    Boolean opt2 = false;

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_opt1:
                if (checked)
                    opt1 = true;
                if (!checked)
                    opt1 = false;
                break;
            case R.id.checkbox_opt2:
                if (checked) {
                    opt2 = true;
                    Favorite_items fav_item = new Favorite_items(menuItem_description,menuItem_name,menuItem_price);
                    mfav_item.child(menuItem_name).setValue(fav_item);
                    Toast.makeText(getApplicationContext(), "Item Added to Favorites List", Toast.LENGTH_SHORT).show();
                }
                if (!checked)
                    opt2 = false;
                break;

        }
    }
}
