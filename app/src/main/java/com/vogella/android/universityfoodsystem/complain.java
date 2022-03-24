package com.vogella.android.universityfoodsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class complain extends AppCompatActivity {
    private RadioGroup radiogroup_for_food;
    private RadioGroup radiogroup_for_delivery;
    private RadioButton  radioButton_for_delivery;
    private RadioButton radioButton_for_food;
    private Button submit;
    private Button cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);

        radiogroup_for_food = findViewById(R.id.radioGroup_for_food);
        radiogroup_for_delivery = findViewById(R.id.radioGroup_for_delivery);
        submit = findViewById(R.id.submit);
        cancel = findViewById(R.id.cancel);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(complain.this,"Your feedback has been received!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(complain.this, Homescreen.class));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(complain.this, Homescreen.class));
            }
        });

    }
    public void checkButton(View v){
        int id = radiogroup_for_delivery.getCheckedRadioButtonId();
        int value = radiogroup_for_food.getCheckedRadioButtonId();

        radioButton_for_delivery = findViewById(id);
        radioButton_for_food = findViewById(value);

        String food_comment = (String) radioButton_for_food.getText();
        String delivery_comment = (String) radioButton_for_delivery.getText();
    }


}