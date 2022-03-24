package com.vogella.android.universityfoodsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreditPayment extends Fragment  {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View v = inflater.inflate(R.layout.fragment_credit_payment, container, false);
        Button bSubmit = v.findViewById(R.id.buttonSubmit);
        Button bCancel = v.findViewById(R.id.buttonCancel);
        EditText creditNum = v.findViewById(R.id.editID1);
        EditText pinNum = v.findViewById(R.id.editID2);
        EditText exp = v.findViewById(R.id.editTextDate);
        TextView costSet = v.findViewById(R.id.displayCost);
        TextView orderSum = v.findViewById(R.id.orderSum);
        TextView instr = v.findViewById(R.id.instructionsCredit);
        double cost = Double.parseDouble(getArguments().getString("cost"));
        String orderID = getArguments().getString("orderID");
        String id = getArguments().getString("restID");
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
        String display3 = "Enter credit card information (16 digit card number, 3 digit pin number, expiration date) "
                +"to complete order";
        orderSum.setText(display2);
        costSet.setText(display);
        instr.setText(display3);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if card is valid
                String creditNumber = creditNum.getText().toString().replaceAll("[^\\d]","");
                String pinNumber = pinNum.getText().toString().replaceAll("[^\\d]","");
                String expDate = exp.getText().toString().replaceAll("[^\\d]","");
                //check that credit number is 16 digits, pin is 3 digits, exp date is MM/YYYY
                if(creditNumber.length() != 16 || pinNumber.length() != 3 || expDate.length() != 6)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Invalid Card", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //get specific order
                    //update order status to received
                    final String TAG = Payment.class.getName();;
                    DocumentReference updateStatus = db.collection("UserOrder").document(orderID);
                    updateStatus.update("Status", 1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d(TAG, "DocumentSnapshot written" );
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
                        creditNum.getText().clear();
                        pinNum.getText().clear();
                        exp.getText().clear();
                    }
                });
                builder.show();
            }
        });

        return v;
    }

    public static CreditPayment newInstance(String text, double cost, String orderID, String restID) {

        CreditPayment f = new CreditPayment();
        Bundle b = new Bundle();
        b.putString("cost", Double.toString(cost));
        b.putString("orderID",orderID);
        b.putString("restID",restID);
        f.setArguments(b);

        return f;
    }
}