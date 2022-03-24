package com.vogella.android.universityfoodsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import org.w3c.dom.Comment;

public class Accept_Activity extends AppCompatActivity {

    private Button submit;
    private Button cancel;
    private EditText comment;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);

        submit = findViewById(R.id.submit);
        cancel = findViewById(R.id.cancel);
        comment = findViewById(R.id.comment);
        ratingBar = findViewById(R.id.ratingBar);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String User_comment = comment.getText().toString();
                float rating_value = ratingBar.getRating();

                if(rating_value < 1 ){
                    Toast.makeText(Accept_Activity.this,"Should provide at least one star",Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(Accept_Activity.this,"Thank you for your feedback!",Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}