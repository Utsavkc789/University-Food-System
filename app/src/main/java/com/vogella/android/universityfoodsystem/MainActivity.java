//Login Screen; links to registration page and restaurant login/register page

package com.vogella.android.universityfoodsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    int studentID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout rel = findViewById(R.id.mainRel);

        Button bConfirm = findViewById(R.id.buttonConfirm);
        Button bCancel = findViewById(R.id.buttonCancel);
        Button bRegister = findViewById(R.id.buttonRegister);
        Button bForgot = findViewById(R.id.buttonForgotPass);
        Button restLogin = findViewById(R.id.restLogin);
        EditText userN = findViewById(R.id.userEntered);
        EditText passW = findViewById(R.id.passEntered);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        bConfirm.setOnClickListener(new View.OnClickListener() {
            int count = 0;

            @Override
            public void onClick(View view) {
                if(passW.isInEditMode() || userN.isInEditMode())
                {
                    Toast toast = Toast.makeText(MainActivity.this, "Please finish finalize entries before submitting.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                String username = userN.getText().toString();
                String password = passW.getText().toString();
                if (count == 5) {
                    Toast toast = Toast.makeText(MainActivity.this, "Try 'Reset Password'?", Toast.LENGTH_SHORT);
                    toast.show();
                    count = 0;
                }
                else
                {
                    studentID = -1;
                    try {
                        studentID = Integer.parseInt(username);
                    }
                    catch(Exception e){
                        Toast toast = Toast.makeText(MainActivity.this, "Invalid Student ID", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    db.collection("Users")
                            .whereEqualTo("ID",studentID)
                            .whereEqualTo("Password",password).get().addOnCompleteListener(
                            new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.getResult().isEmpty())
                                    {
                                        // invalid credentials
                                        Toast toast = Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT);
                                        toast.show();
                                        count++;
                                    }
                                    else
                                    {
                                        Intent intent = new Intent(MainActivity.this, Homescreen.class);
                                        intent.putExtra("UserID", studentID);
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
                userN.getText().clear();
                passW.getText().clear();
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

        bForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ForgotPassword.class);
                intent.putExtra("RestaurantReset", 0);
                startActivity(intent);
            }
        });

        restLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RestaurantLogin.class);
                startActivity(intent);
            }
        });
    }
    public static void checkDataEntered(EditText et)
    {
        if(isEmpty(et))
        {
            et.setError("Required");
        }
    }

    public static boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}