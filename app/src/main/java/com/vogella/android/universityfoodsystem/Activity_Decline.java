package com.vogella.android.universityfoodsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Activity_Decline extends AppCompatActivity {
    private Button complaint;
    private Button no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decline);

        complaint = findViewById(R.id.complaint);
        no = findViewById(R.id.no);

        complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Decline.this, complain.class));
                finish();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Decline.this, Homescreen.class));

            }
        });

    }
}