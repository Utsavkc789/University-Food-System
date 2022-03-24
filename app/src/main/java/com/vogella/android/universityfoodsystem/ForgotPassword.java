package com.vogella.android.universityfoodsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;

public class ForgotPassword extends AppCompatActivity {
    String questionText;
    String answerText;
    String emailAddress;
    void handleResetPassword(boolean isRestaurant)
    {
        String newPass = String.valueOf(new Random().nextInt(99999999));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(isRestaurant? "Restaurants" : "Users").whereEqualTo("Email",emailAddress).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                db.collection(isRestaurant? "Restaurants" : "Users").document(documentId).update("Password",newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // password updated, communication should email user
                        TemporaryPasswordHandler.SendTemporaryPassword(emailAddress,newPass,isRestaurant);
                        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                        builder.setTitle("Email Sent");
                        builder.setMessage("Please Reset Your Password Upon Login");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                        builder.show();
                    }
                });
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Intent intent = getIntent();

        boolean isRestaurant = intent.getIntExtra("RestaurantReset", 0) == 1;
        Button bConfirm1 = findViewById(R.id.buttonConfirm1);
        Button bConfirm2 = findViewById(R.id.buttonConfirm2);
        TextView question = findViewById(R.id.questionText);
        TextView emailText = findViewById(R.id.confirmEmailText);
        TextView info1 = findViewById(R.id.infoText1);
        TextView info2 = findViewById(R.id.infoText2);
        EditText answer = findViewById(R.id.answerEntered);
        EditText emailConfirm = findViewById(R.id.confirmEmailEntered);

        emailText.setVisibility(TextView.VISIBLE);
        emailConfirm.setVisibility(EditText.VISIBLE);
        bConfirm2.setVisibility(Button.VISIBLE);
        info2.setVisibility(Button.VISIBLE);

        question.setVisibility(TextView.GONE);
        answer.setVisibility(EditText.GONE);
        bConfirm1.setVisibility(Button.GONE);
        info1.setVisibility(Button.GONE);

        bConfirm1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                MainActivity.checkDataEntered(answer);
                String ans = answer.getText().toString();
                if(ans.equals(answerText))
                {
                    //communication sends reset email
                    handleResetPassword(false);
                }
                else
                {
                    Toast toast = Toast.makeText(ForgotPassword.this, "Incorrect Answer", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        bConfirm2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                MainActivity.checkDataEntered(emailConfirm);
                emailAddress = emailConfirm.getText().toString();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection(isRestaurant? "Restaurants" : "Users")
                        .whereEqualTo("Email",emailAddress).get().addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.getResult().isEmpty())
                                {
                                    // invalid credentials
                                    Toast toast = Toast.makeText(ForgotPassword.this, "Unknown email address", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                else
                                {
                                    if(isRestaurant)
                                    {
                                        //communication sends email to reset password
                                        handleResetPassword(true);
                                    }
                                    else
                                    {
                                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                        questionText = document.getString("SecurityQuestion");
                                        answerText = document.getString("SecurityAnswer");
                                        question.setText(questionText);
                                        emailText.setVisibility(TextView.GONE);
                                        emailConfirm.setVisibility(EditText.GONE);
                                        bConfirm2.setVisibility(Button.GONE);
                                        info2.setVisibility(Button.GONE);

                                        question.setVisibility(TextView.VISIBLE);
                                        answer.setVisibility(EditText.VISIBLE);
                                        bConfirm1.setVisibility(Button.VISIBLE);
                                        info1.setVisibility(Button.VISIBLE);
                                    }
                                }
                            }
                        }
                );
            }
        });
    }
}