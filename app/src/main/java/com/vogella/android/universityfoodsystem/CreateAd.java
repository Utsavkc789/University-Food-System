package com.vogella.android.universityfoodsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateAd extends AppCompatActivity{

    String name;
    String restaurantName;
    double amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ad);
        Intent intent = getIntent();
        restaurantName = intent.getStringExtra("RestaurantName");
        Button bConfirm = findViewById(R.id.adConfirm);
        Button bCancel = findViewById(R.id.adCancel);
        EditText discount = findViewById(R.id.discountEdit);
        TextView info = findViewById(R.id.displayInfo);
        String textSet = "Enter a percentage to take off the customer's final order. The ad will be displayed"
                +"on the user's homescreen. When clicked, the customer will be taken to the restaurant "
                +"homepage. Their total order cost will be reduced by the percentage entered below";
        info.setText(textSet);

        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.checkDataEntered(discount);
                double discountAmt = Double.valueOf(discount.getText().toString());
                if(discountAmt > .9 || discountAmt <= 0)
                {
                    Toast toast = Toast.makeText(CreateAd.this, "Invalid discount amount. Please enter decimal number between 0 and .9", Toast.LENGTH_LONG);
                    toast.show();
                }
                else
                {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Calendar expirationThreshold = Calendar.getInstance();
                    expirationThreshold.add(Calendar.WEEK_OF_YEAR,-2);
                    db.collection("Restaurants")
                            .whereEqualTo("Name",restaurantName)
                            .whereLessThanOrEqualTo("AdCreationDateTime",expirationThreshold.getTimeInMillis())
                            .get().addOnCompleteListener(
                            new OnCompleteListener<QuerySnapshot>() {
                                private final String TAG = CreateAd.class.getName();

                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.getResult().isEmpty())
                                    {
                                        // invalid credentials
                                        Toast toast = Toast.makeText(CreateAd.this, "Ad already created.", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                    else
                                    {
                                        QuerySnapshot doc = task.getResult();
                                        name = doc.getDocuments().get(0).getId();
                                        DocumentReference updateAd = db.collection("Restaurants").document(name);
                                        updateAd.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document != null && document.contains("AdFees")) {
                                                         amt = document.getDouble("AdFees");
                                                    } else {
                                                        Log.d("LOGGER", "No such document");
                                                    }
                                                } else {
                                                    Log.d("LOGGER", "get failed with ", task.getException());
                                                }
                                            }
                                        });
                                        Map<String,Object> data = new HashMap<String,Object>();
                                        data.put("AdDiscount",discountAmt);
                                        data.put("AdFees", amt + 28);
                                        // Use this to detect when the ad has expired.
                                        // You do not need to change anything in the DB when the ad expires, just query for
                                        //   ads that have been created in the last two weeks.
                                        data.put("AdCreationDateTime", Calendar.getInstance().getTimeInMillis());
                                        DocumentReference updateAd1 = db.collection("Restaurants").document(name);
                                        updateAd1.update(data)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Log.d(TAG, "DocumentSnapshot written" );
                                                        Toast toast = Toast.makeText(CreateAd.this, "Ad Created", Toast.LENGTH_SHORT);
                                                        toast.show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.i(TAG, "Error adding document", e);
                                                        Toast toast = Toast.makeText(CreateAd.this, "Error creating ad, please try again later.", Toast.LENGTH_SHORT);
                                                        toast.show();
                                                    }
                                                });
                                        Intent intent = new Intent(CreateAd.this, RestaurantHomescreen.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                    );
                }

            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateAd.this);
                builder.setTitle("Return to Home Screen?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(CreateAd.this, RestaurantHomescreen.class);
                        intent.putExtra("RestaurantName", restaurantName);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        discount.getText().clear();
                    }
                });
                builder.show();
            }
        });
    }

}