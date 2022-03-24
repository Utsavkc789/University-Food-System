package com.vogella.android.universityfoodsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Homescreen extends AppCompatActivity {
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_homescreen);
        Button bSubmit = findViewById(R.id.linkToDelivery);
        Button logOut = findViewById(R.id.returnToLogin);
        Button adButton = findViewById(R.id.orderAd);
        Button searchButton = findViewById(R.id.linkToSearch);
        Button  Favorite_list = findViewById(R.id.Favorite_list);
        Button  orders = findViewById(R.id.OrderCart);
        Button  reviews = findViewById(R.id.rate);

        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //link to reviews page
                startActivity(new Intent(Homescreen.this, Rating.class));
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Homescreen.this, Search.class);
                startActivity(intent);
            }
        });

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homescreen.this, Delivery.class);
                intent.putExtra("orderID", "ueLQBlkIvlw1JtAHVPte");
                startActivity(intent);
            }
        });

        Favorite_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homescreen.this,Favoritelist.class));
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homescreen.this,OrderCart.class));
            }
        });
        adButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Calendar expirationThreshold = Calendar.getInstance();
                expirationThreshold.add(Calendar.WEEK_OF_YEAR,-2);
                db.collection("Restaurants")
                        .whereGreaterThanOrEqualTo("AdCreationDateTime",expirationThreshold.getTimeInMillis())
                        .get().addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.getResult().isEmpty())
                                {
                                    // invalid credentials
                                    Toast toast = Toast.makeText(Homescreen.this, "An Error Has Occurred!", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                else
                                {
                                    List<String> idList=new ArrayList<String>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        idList.add(document.getId());
                                    }
                                    id = idList.get(new Random().nextInt(idList.size()));
                                    DocumentReference getRestaurant = db.collection("Restaurants").document(id);
                                    getRestaurant.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document != null) {
                                                    AdData.adDiscount = document.getDouble("AdDiscount");
                                                    AdData.dateCreated = document.getDouble("AdCreationDateTime");
                                                    AdData.restaurantName = document.getString("Name");
                                                    AdData.restaurantID = id;
                                                    Intent intent = new Intent(Homescreen.this, MenuSelection.class);
                                                    intent.putExtra("key_RNAME", AdData.restaurantName);
                                                    startActivity(intent);
                                                } else {
                                                    Log.d("LOGGER", "No such document");
                                                }
                                            } else {
                                                Log.d("LOGGER", "get failed with ", task.getException());
                                            }
                                        }
                                    });
                                }
                            }
                        });
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Homescreen.this, MainActivity.class);
                intent.putExtra("RestaurantReset", 0);
                startActivity(intent);
            }
        });
    }
}
