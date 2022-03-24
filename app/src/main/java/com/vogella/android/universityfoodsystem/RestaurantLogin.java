package com.vogella.android.universityfoodsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class RestaurantLogin extends AppCompatActivity {
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_login);
        Intent intent = getIntent();
        Button bSubmit = findViewById(R.id.buttonConf);
        Button bCancel = findViewById(R.id.buttonCanc);
        Button bRegister = findViewById(R.id.buttonReg);
        Button bReset = findViewById(R.id.buttonForgotPassRest);
        EditText restName = findViewById(R.id.restEntered);
        EditText restPass = findViewById(R.id.passRestEntered);

        bSubmit.setOnClickListener(new View.OnClickListener() {
            int count = 0;
            @Override
            public void onClick(View view) {
                //check if card is valid
                if(restPass.isInEditMode() || restName.isInEditMode())
                {
                    Toast toast = Toast.makeText(RestaurantLogin.this, "Please finish finalize entries before submitting.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                String username = restName.getText().toString();
                String password = restPass.getText().toString();
                if (count == 5) {
                    Toast toast = Toast.makeText(RestaurantLogin.this, "Try 'Reset Password'?", Toast.LENGTH_SHORT);
                    toast.show();
                    count = 0;
                }
                else
                {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Restaurants")
                            .whereEqualTo("Email",username)
                            .whereEqualTo("Password",password).get().addOnCompleteListener(
                            new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.getResult().isEmpty())
                                    {
                                        // invalid credentials
                                        Toast toast = Toast.makeText(RestaurantLogin.this, "Invalid restaurant name or password.", Toast.LENGTH_SHORT);
                                        toast.show();
                                        count++;
                                    }
                                    else
                                    {
                                        // We know by the above if statement that at least one item must exist.
                                        name = task.getResult().getDocuments().get(0).getString("Name");

                                        Intent intent = new Intent(RestaurantLogin.this, RestaurantHomescreen.class);
                                        intent.putExtra("RestaurantName", name);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                    );
                }
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restName.getText().clear();
                restPass.getText().clear();
            }

        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantLogin.this, RestaurantRegister.class);
                intent.putExtra("Info", 2);
                startActivity(intent);
            }
        });

        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantLogin.this, ForgotPassword.class);
                intent.putExtra("RestaurantReset", 1);
                startActivity(intent);
            }
        });
    }
}