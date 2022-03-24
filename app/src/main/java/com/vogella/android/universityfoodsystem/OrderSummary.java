package com.vogella.android.universityfoodsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderSummary extends AppCompatActivity {

    OrderSummaryAdapter myAdapter;
    RecyclerView recyclerViewList;
    TextView summaryOrderNumberText,summaryFirstNameText,summaryLastNameText,summaryEmailText,summaryRestNameText,summaryOrderTotalNum;;
    TextView cancelButton, paymentButton;
    FirebaseFirestore db;
    ArrayList<OrderInfo> orderInfo;
    String summaryOrderNumber,summaryFirstName,summaryLastName,summaryEmail,summaryRestaurant;
    double summaryTotal;
    private static final String TAG = "YOUR-TAG-NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        recyclerViewList = findViewById(R.id.summaryRecyclerView);
        recyclerViewList.setHasFixedSize(true);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        orderInfo = new ArrayList<>();
        myAdapter = new OrderSummaryAdapter(OrderSummary.this,orderInfo);
        recyclerViewList.setAdapter(myAdapter);

        Intent intent = getIntent();

        initializeViews();
        GetOrderInformation();
        setIntentValues(intent);



        //Payment Button navigates to Payment Activity when clicked
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(OrderSummary.this, Delivery.class);
                myIntent.putExtra("orderID", "ueLQBlkIvlw1JtAHVPte");
                startActivity(myIntent);
            }
        });

        //Cancel Order Button navigates back to restaurant selection
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(OrderSummary.this, Search.class);
                startActivity(myIntent);
            }
        });
    }

    private void initializeViews() {

        recyclerViewList = findViewById(R.id.summaryRecyclerView);
        summaryOrderNumberText = findViewById(R.id.summaryOrderNumberText);
        summaryFirstNameText = findViewById(R.id.summaryFirstNameText);
        summaryLastNameText = findViewById(R.id.summaryLastNameText);
        summaryEmailText = findViewById(R.id.summaryEmailText);
        summaryRestNameText = findViewById(R.id.summaryRestNameText);
        summaryOrderTotalNum = findViewById(R.id.summaryOrderTotalNum);
        cancelButton = findViewById(R.id.cancelOrderButton);
        paymentButton = findViewById(R.id.paymentButton);
    }

    public void GetOrderInformation(){

        //Get subcollection database info
        db.collection("DummyOrders").document("uhOUwoNth3M2IvHzdaoZ").collection("OrderItems")
                .orderBy("itemPrice", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for(QueryDocumentSnapshot document: task.getResult()) {
                        if (document.exists()) {
                            orderInfo.add(document.toObject(OrderInfo.class));

                            myAdapter.notifyDataSetChanged();
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d("get failed with ", String.valueOf(task.getException()));
                }
            }
        });

        //Get Additional database information
        /*
        db.collection("DummyOrders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for(QueryDocumentSnapshot document: task.getResult()) {
                        if (document.exists()) {
                            summaryOrderNumberText.setText(document.getString("orderNumber"));
                            summaryFirstNameText.setText(document.getString("firstName"));
                            summaryLastNameText.setText(document.getString("lastName"));
                            summaryEmailText.setText(document.getString("emailAddress"));
                            summaryRestNameText.setText(document.getString("restName"));

                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d("get failed with ", String.valueOf(task.getException()));
                }
            }
        });
        */

    }

    public void setIntentValues(Intent intent){

        summaryTotal = intent.getDoubleExtra("Total",0);
        summaryOrderNumber = intent.getStringExtra("OrderNumber");
        summaryFirstName = intent.getStringExtra("FirstName");
        summaryLastName = intent.getStringExtra("LastName");
        summaryEmail = intent.getStringExtra("Email");
        summaryRestaurant = intent.getStringExtra("Restaurant");

        summaryOrderTotalNum.setText(String.valueOf(summaryTotal));
        summaryOrderNumberText.setText(summaryOrderNumber);
        summaryFirstNameText.setText(summaryFirstName);
        summaryLastNameText.setText(summaryLastName);
        summaryEmailText.setText(summaryEmail);
        summaryRestNameText.setText(summaryRestaurant);

    }
}