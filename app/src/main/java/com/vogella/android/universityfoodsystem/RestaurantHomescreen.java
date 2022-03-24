package com.vogella.android.universityfoodsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RestaurantHomescreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_homescreen);

        Intent intent = getIntent();
        String name = intent.getStringExtra("RestaurantName");
        Button menu = (Button) findViewById(R.id.modifyMenu);
        Button ad = (Button) findViewById(R.id.rentAd);
        Button logout = (Button) findViewById(R.id.R_LOGOUT_Button);
        TextView tv = findViewById(R.id.textName);
        String info = name + " Home Screen";
        tv.setText(info);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantHomescreen.this, ModifyMenu.class);
                intent.putExtra("RestaurantName", name);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantHomescreen.this, MainActivity.class);
                startActivity(intent);
            }
        });
        ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //link to reviews page
                Intent intent = new Intent(RestaurantHomescreen.this, CreateAd.class);
                intent.putExtra("RestaurantName", name);
                startActivity(intent);
            }
        });
    }
}