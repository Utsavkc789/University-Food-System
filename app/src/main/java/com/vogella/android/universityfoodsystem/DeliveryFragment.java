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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class DeliveryFragment extends Fragment  {
    String rest;
    String openTime, closeTime;
    Date date;
    Date dateCompareOne;
    Date dateCompareTwo;
    SimpleDateFormat inputParser = new SimpleDateFormat("HH:mm", Locale.US);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View v = inflater.inflate(R.layout.fragment_delivery, container, false);
        Button bSubmit = v.findViewById(R.id.deliverySubmit);
        Button bCancel = v.findViewById(R.id.deliveryCancel);
        EditText streetTxt = v.findViewById(R.id.deliveryID1);
        EditText cityTxt = v.findViewById(R.id.deliveryID2);
        EditText zipTxt = v.findViewById(R.id.editZip);
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        TextView orderSum = (TextView) v.findViewById(R.id.deliveryInfo);
        String display2 = "Spicy Chicken Biscuit with no pickles";
        orderSum.setText(display2);
        TextView instr = (TextView) v.findViewById(R.id.deliveryInfo2);
        String display = "Enter a valid delivery address to estimate delivery time";
        instr.setText(display);

        String orderID = getArguments().getString("orderID");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //DocumentReference getRestaurant = db.collection("UserOrder").document(orderID);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            //TODO: read database and update that delivery method is delivery
            @Override
            public void onClick(View view) {
                String[] names = {"Street Address","City","Zip Code"};
                EditText[] fields = {streetTxt,cityTxt,zipTxt};
                String[] values = new String[3];
                for(int i = 0; i < fields.length; i++)
                {
                    values[i] = fields[i].getText().toString();
                    if(i == 2)
                        // Removing all non-digit characters from the zip-code.
                        // This is useful in case there are hyphens or
                        //   whitespace for some reason.
                        values[2] = values[2].replaceAll("[^\\d]","");
                    if(values[i].length() == 0 || i == 2 && values[2].length() != 5)
                    {
                        Toast.makeText(getActivity().getApplicationContext(), "Invalid " + names[i] + ".", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                DocumentReference doc = FirebaseFirestore.getInstance().collection("UserOrder").document(orderID);
                doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // Adding 10% to the cost of the order.
                        doc.update("Cost",documentSnapshot.getDouble("Cost")*1.1);
                        DocumentReference getRestaurant = db.collection("UserOrder").document(orderID);
                        getRestaurant.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null) {
                                        rest = document.getString("Restaurant");
                                        DocumentReference getTimes = db.collection("Restaurants").document(rest);

                                        getTimes.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                                if (task2.isSuccessful()) {
                                                    DocumentSnapshot document2 = task2.getResult();
                                                    if (document2 != null) {
                                                        openTime = (String) document2.get("OpenTime");
                                                        closeTime = (String) document2.get("CloseTime");
                                                        date = parseDate(hour + ":" + minute);
                                                        dateCompareOne = parseDate(openTime);
                                                        dateCompareTwo = parseDate(closeTime);

                                                        if ( dateCompareOne.before( date ) && dateCompareTwo.after(date)) {
                                                            cal.add(Calendar.MINUTE, 30);
                                                            SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                            builder.setTitle("Delivery Time Estimate");
                                                            String mess = "Your delivery time will be "+format.format(cal.getTime());
                                                            builder.setMessage(mess);
                                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface arg0, int arg1) {
                                                                    // Since values[2] only contains numeric characters and is of length 7, the parse will succeed.
                                                                    DeliveryMessageHandler.HandleDeliveryRequest(values[0],values[1],Integer.parseInt(values[2]),orderID);
                                                                    Intent intent = new Intent(getActivity(), Payment.class);
                                                                    intent.putExtra("orderID", orderID);
                                                                    startActivity(intent);
                                                                }
                                                            });
                                                            builder.show();
                                                        }
                                                        else
                                                        {
                                                            cal.setTime(dateCompareOne);
                                                            cal.add(Calendar.MINUTE, 30);
                                                            SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                            builder.setTitle("Delivery After Hours");
                                                            String mess = "The restaurant is currently closed. Your delivery time will be "+format.format(cal.getTime());
                                                            builder.setMessage(mess);
                                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface arg0, int arg1) {
                                                                    // Since values[2] only contains numeric characters and is of length 7, the parse will succeed.
                                                                    DeliveryMessageHandler.HandleDeliveryRequest(values[0],values[1],Integer.parseInt(values[2]),orderID);
                                                                    Intent intent = new Intent(getActivity(), Payment.class);
                                                                    intent.putExtra("orderID", orderID);
                                                                    startActivity(intent);
                                                                }
                                                            });
                                                            builder.show();
                                                        }

                                                    } else {
                                                        Log.d("LOGGER", "No such document");

                                                    }
                                                } else {
                                                    Log.d("LOGGER", "get failed with ", task.getException());
                                                }
                                            }
                                        });
                                    } else {
                                        Log.d("LOGGER", "No such document");
                                    }
                                } else {
                                    Log.d("LOGGER", "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                });
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
                        streetTxt.getText().clear();
                        cityTxt.getText().clear();
                        zipTxt.getText().clear();
                    }
                });
                builder.show();
            }
        });

        return v;
    }
    private Date parseDate(String date) {

        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }

    public static DeliveryFragment newInstance(String text, String orderID) {

        DeliveryFragment f1 = new DeliveryFragment();
        Bundle b1 = new Bundle();
        b1.putString("msg1", "Street Address: ");
        b1.putString("orderID",orderID);

        f1.setArguments(b1);

        return f1;
    }
}