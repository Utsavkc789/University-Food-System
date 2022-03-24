package com.vogella.android.universityfoodsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class OrderCart extends AppCompatActivity {

    OrderCartAdapter myAdapter;
    RecyclerView recyclerViewList;
    TextView itemTotalText, deliveryFeeText, taxText, totalText, itemTotalNum, checkoutButton, deliveryFeeNum, taxNum, totalNum;
    FirebaseFirestore db;
    ArrayList<OrderInfo> orderInfo;
    ProgressDialog progressDialog;
    double finalTotal;
    double taxRate = .0625;
    double deliveryFee = 2.99;
    String orderNumber, firstName, lastName, eMailAddress, restName, menuItemName;

    private static final String TAG = "YOUR-TAG-NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_cart);

        //Progress Dialog while restaurant info is being retrieved from database
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Retrieving Order Info...");
        progressDialog.show();

        recyclerViewList = findViewById(R.id.recyclerView);
        recyclerViewList.setHasFixedSize(true);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        orderInfo = new ArrayList<>();
        myAdapter = new OrderCartAdapter(OrderCart.this,orderInfo);
        recyclerViewList.setAdapter(myAdapter);

        initializeViews();
        GetOrderInformation();

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(OrderCart.this, OrderSummary.class);
                myIntent.putExtra("Total", finalTotal);
                //myIntent.putExtra("MenuItemName", menuItemName);
                myIntent.putExtra("OrderNumber", orderNumber);
                myIntent.putExtra("FirstName", firstName);
                myIntent.putExtra("LastName", lastName);
                myIntent.putExtra("Email", eMailAddress);
                myIntent.putExtra("Restaurant", restName);
                startActivity(myIntent);

            }
        });

        myAdapter.setItemUpdateListener(new ItemUpdateListener() {
            @Override
            public void onItemUpdate(double itemTotal) {
                addCartTotals();
            }
        });
    }

    private void initializeViews() {
        recyclerViewList = findViewById(R.id.recyclerView);
        itemTotalText = findViewById(R.id.itemTotalText);
        deliveryFeeText = findViewById(R.id.deliveryFeeText);
        taxText = findViewById(R.id.taxText);
        totalText = findViewById(R.id.totalText);
        itemTotalNum = findViewById(R.id.itemTotalNum);
        checkoutButton = findViewById(R.id.checkoutText);
        deliveryFeeNum = findViewById(R.id.deliveryFeeNum);
        taxNum = findViewById(R.id.taxNum);
        totalNum = findViewById(R.id.totalNum);
    }

    public void GetOrderInformation(){

        db.collection("DummyOrders").document("uhOUwoNth3M2IvHzdaoZ").collection("OrderItems")
                .orderBy("itemPrice", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();

                    for(QueryDocumentSnapshot document: task.getResult()) {
                        if (document.exists()) {
                            orderInfo.add(document.toObject(OrderInfo.class));
                            myAdapter.notifyDataSetChanged();
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }

                    addCartTotals();

                } else {
                    Log.d("get failed with ", String.valueOf(task.getException()));
                }
            }
        });

        //Get Additional database information
        db.collection("DummyOrders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for(QueryDocumentSnapshot document: task.getResult()) {
                        if (document.exists()) {
                            orderNumber = document.getString("orderNumber");
                            firstName = document.getString("firstName");
                            lastName = document.getString("lastName");
                            eMailAddress = document.getString("emailAddress");
                            restName = document.getString("restName");

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
    }

    private void addCartTotals() {

        double subTotal = 0;
        //double deliveryFee = 2.99;

        for (OrderInfo info : orderInfo){
            subTotal += info.getItemQuantity() * info.getItemPrice();
        }

        double tax = subTotal * taxRate;
        finalTotal = Math.round(100 * (subTotal + tax + deliveryFee)) / 100.0;

        taxNum.setText("$" + String.format("%.2f",(Math.round(100 * tax) / 100.0)));
        itemTotalNum.setText("$" + String.format("%.2f", Math.round(100 * subTotal) / 100.0));
        deliveryFeeNum.setText("$" + String.valueOf(deliveryFee));
        totalNum.setText("$" + String.format("%.2f",Math.round(100 * (subTotal + tax + deliveryFee)) / 100.0));
    }
}

