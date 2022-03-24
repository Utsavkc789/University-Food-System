package com.vogella.android.universityfoodsystem;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PickupFragment extends Fragment  {
    String rest;
    String openTime, closeTime;
    Date date;
    Date dateCompareOne;
    Date dateCompareTwo;
    int add;
    SimpleDateFormat inputParser = new SimpleDateFormat("HH:mm", Locale.US);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pickup, container, false);
        Button bSubmit = v.findViewById(R.id.pickupSubmit);
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        TextView tv = (TextView) v.findViewById(R.id.pickupView2);
        TextView orderSum = (TextView) v.findViewById(R.id.deliveryInfo3);
        String display2 = "Spicy Chicken Biscuit with no pickles";
        orderSum.setText(display2);
        TextView instr = (TextView) v.findViewById(R.id.deliveryInfo5);
        String display = "Select a pickup location to estimate delivery time";
        instr.setText(display);
        String orderID = getArguments().getString("orderID");

        Spinner spinnerOpen=v.findViewById(R.id.spinnerPickup);
        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(getContext(), R.array.buildings, android.R.layout.simple_spinner_item);

        spinnerOpen.setAdapter(adapter1);
        spinnerOpen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
                switch(pos)
                {
                    case 0: add = 5;
                        break;
                    case 1:add = 5;
                        break;
                    case 2:add = -5;
                        break;
                    default:add = 0;
                }
                if(dateCompareOne != null && dateCompareTwo != null)
                {
                    if ( dateCompareOne.before( date ) && dateCompareTwo.after(date)) {
                        cal.add(Calendar.MINUTE, add);
                        tv.setText(format.format(cal.getTime()));
                    }
                    else
                    {
                        cal.setTime(dateCompareOne);
                        cal.add(Calendar.MINUTE, add);
                        tv.setText(format.format(cal.getTime()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                                            tv.setText(format.format(cal.getTime()));
                                        }
                                        else
                                        {
                                            cal.setTime(dateCompareOne);
                                            cal.add(Calendar.MINUTE, 30);
                                            SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
                                            tv.setText(format.format(cal.getTime()));
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

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Payment.class);
                intent.putExtra("orderID", orderID);
                startActivity(intent);

            }
        });


        return v;
    }
    private Date parseDate(String date) {

        try { return inputParser.parse(date); }
        catch (java.text.ParseException e) { return new Date(0); }
    }

    public static PickupFragment newInstance(String text,String orderID) {

        PickupFragment f2 = new PickupFragment();
        Bundle b2 = new Bundle();
        b2.putString("msg2", "Street Address:");
        b2.putString("orderID",orderID);

        f2.setArguments(b2);

        return f2;
    }
}