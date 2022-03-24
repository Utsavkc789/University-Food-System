package com.vogella.android.universityfoodsystem;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.Locale;

public class MealPlanPayment extends Fragment {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_meal_plan_payment, container, false);
        Button bMealSwipes = v.findViewById(R.id.buttonMealSwipes);
        Button bDiningDollars = v.findViewById(R.id.buttonDiningDollar);
        Button bCancel = v.findViewById(R.id.buttonCancel);
        EditText studentID = v.findViewById(R.id.editID1);
        TextView costSet = v.findViewById(R.id.displayCostMP);
        TextView orderSum = v.findViewById(R.id.orderSumMP);
        TextView instr = v.findViewById(R.id.instructionsMP);
        //pass ID with intent
        //read cost from database
        //should receive student's ID to check valid
        double cost = Double.parseDouble(getArguments().getString("cost"));
        String id = getArguments().getString("restID");
        String orderID = getArguments().getString("orderID");
        String display;
        if(id.equals(AdData.restaurantID))
        {
            display = "$"+df.format(cost*(1-AdData.adDiscount));
        }
        else
        {
            display = "$"+df.format(cost);
        }
        String display2 = "Spicy Chicken Biscuit with no pickles";
        String display3 = "Enter 10 digit student ID to complete order";
        orderSum.setText(display2);
        costSet.setText(display);
        instr.setText(display3);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        bMealSwipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = studentID.getText().toString().replaceAll("[^\\d]","");
                if(ID.length() == 10)
                {
                    //get specific order
                    //update order status to received
                    final String TAG = Payment.class.getName();
                    DocumentReference updateStatus = db.collection("UserOrder").document(orderID);
                    updateStatus.update("Status", 1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d(TAG, "order status updated" );
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Payment Confirmed!");
                                    builder.setMessage("Thank you so much for your business!");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Intent intent = new Intent(getActivity(), Homescreen.class);
                                            startActivity(intent);
                                        }
                                    });
                                    builder.show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i(TAG, "Error adding document", e);
                                }
                            });
                    updateStatus.update("Cost", cost*(1-AdData.adDiscount))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d(TAG, "order status updated" );
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i(TAG, "Error adding document", e);
                                }
                            });

                }
                else
                {
                    Toast toast = Toast.makeText(getActivity(), "Invalid Student ID", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        bDiningDollars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = studentID.getText().toString().replaceAll("[^\\d]","");
                if(ID.length() == 10)
                {
                    final String TAG = Payment.class.getName();
                    DocumentReference updateStatus = db.collection("UserOrder").document(orderID);
                    updateStatus.update("Status", 1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d(TAG, "order status updated" );
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Payment Confirmed!");
                                    builder.setMessage("Thank you so much for your business!");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Intent intent = new Intent(getActivity(), Homescreen.class);
                                            startActivity(intent);
                                        }
                                    });
                                    builder.show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i(TAG, "Error adding document", e);
                                }
                            });
                    updateStatus.update("Cost", cost*(1-AdData.adDiscount))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d(TAG, "order status updated" );
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i(TAG, "Error adding document", e);
                                }
                            });
                }
                else
                {
                    Toast toast = Toast.makeText(getActivity(), "Invalid Student ID", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Return to Home Screen?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(getContext(), Homescreen.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        studentID.getText().clear();
                    }
                });
                builder.show();
            }
        });

        return v;
    }

    public static MealPlanPayment newInstance(String text, double cost, String orderID, String restID) {

        MealPlanPayment f = new MealPlanPayment();
        Bundle b = new Bundle();
        b.putString("cost", Double.toString(cost));
        b.putString("orderID",orderID);
        b.putString("restID",restID);
        f.setArguments(b);

        return f;
    }
}