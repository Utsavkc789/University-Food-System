package com.vogella.android.universityfoodsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Rating extends AppCompatActivity {

    private Button Accept;
    private Button Decline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        Accept = findViewById(R.id.Accept);
        Decline = findViewById(R.id.Decline);

        Accept.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Rating.this, Accept_Activity.class));
                finish();
            }
        });

        Decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Rating.this, Activity_Decline.class));
                finish();
            }
        });
    }
}