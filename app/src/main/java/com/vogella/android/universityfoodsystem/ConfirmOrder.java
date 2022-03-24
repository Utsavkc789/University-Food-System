package com.vogella.android.universityfoodsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ConfirmOrder extends AppCompatActivity {
    int orderCost = 0;
    TextView noOrders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Intent intent = getIntent();
        int userID = intent.getIntExtra("UserID", -1);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        LinearLayout LL = findViewById(R.id.orderLinLayout);
        Button submit = findViewById(R.id.submitOrder);
        Button cancel = findViewById(R.id.cancelOrder);
        //read number of items
        int numItems;
        db.collection("UserOrder")
                .whereEqualTo("UserID",userID).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    private final String TAG = CreateAd.class.getName();;

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().isEmpty())
                        {
                            // invalid credentials
                            Toast toast = Toast.makeText(ConfirmOrder.this, "An error has occurred", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else
                        {
                            //get the items and display them
                            DocumentReference dr = db.collection("UserOrder").document(String.valueOf(userID));
                            dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document1 = task.getResult();
                                        if (document1 != null) {
                                            List<String> orderItems = (List<String>) document1.get("OrderItems");
                                            int numItems = orderItems.size();
                                            //for each item in order, read data at reference in list
                                            if(numItems == 0)
                                            {
                                                noOrders = new TextView(ConfirmOrder.this);
                                                noOrders.setText("No items selected!");
                                                LL.addView(noOrders);
                                            }
                                            else
                                            {
                                                String itemPath;
                                                int backgroundColor = ContextCompat.getColor(ConfirmOrder.this, R.color.black);
                                                int textColor = ContextCompat.getColor(ConfirmOrder.this, R.color.white);
                                                for (int i = 0; i < numItems; i++) {
                                                    itemPath = orderItems.get(i).substring(orderItems.get(i).lastIndexOf('/'));
                                                    DocumentReference dr = db.collection("MenuItem").document(itemPath);
                                                    dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentSnapshot document2 = task.getResult();
                                                                if (document2 != null) {
                                                                    CardView card = new CardView(ConfirmOrder.this);
                                                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                                                    );
                                                                    card.setLayoutParams(params);
                                                                    card.setRadius(9);
                                                                    card.setContentPadding(15, 15, 15, 15);

                                                                    card.setCardBackgroundColor(backgroundColor);
                                                                    card.setMaxCardElevation(15);
                                                                    card.setCardElevation(9);
                                                                    //get item name from DB
                                                                    TextView itemName = new TextView(ConfirmOrder.this);
                                                                    itemName.setLayoutParams(params);
                                                                    String name = document2.getString("Name");
                                                                    itemName.setText(name);
                                                                    itemName.setTextColor(textColor);
                                                                    card.addView(itemName);

                                                                    //get description from DB
                                                                    TextView description = new TextView(ConfirmOrder.this);
                                                                    description.setLayoutParams(params);
                                                                    String itemDescription = document2.getString("Description");
                                                                    description.setText(itemDescription);
                                                                    description.setTextColor(textColor);
                                                                    card.addView(description);

                                                                    //get cost from DB
                                                                    TextView cost = new TextView(ConfirmOrder.this);
                                                                    cost.setLayoutParams(params);
                                                                    String itemCost = document2.get("Price").toString();
                                                                    orderCost += Double.valueOf(itemCost);
                                                                    cost.setText(itemCost);
                                                                    cost.setTextColor(textColor);
                                                                    card.addView(cost);

                                                                    LL.addView(card);
                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
        );

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmOrder.this, Payment.class);
                intent.putExtra("OrderCost", String.valueOf(orderCost));
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cancel pending order
            }
        });
    }
}