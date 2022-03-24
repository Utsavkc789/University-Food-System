//Returns to login after successful registration
package com.vogella.android.universityfoodsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String securityQuestion = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();

        Button bSubmit = findViewById(R.id.registerConfirm);
        Button bCancel = findViewById(R.id.registerCancel);
        EditText firstName = findViewById(R.id.fNameEntered);
        EditText lastName = findViewById(R.id.lNameEntered);
        EditText email = findViewById(R.id.emailEntered);
        EditText phone = findViewById(R.id.phoneEntered);
        EditText studentID = findViewById(R.id.idEntered);
        EditText streetAddr = findViewById(R.id.streetAddressEntered);
        EditText city = findViewById(R.id.cityEntered);
        EditText zip = findViewById(R.id.zipEntered);
        EditText password = findViewById(R.id.passEntered);
        EditText spinnerEntered = findViewById(R.id.spinnerEntered);

        Spinner spinnerSecurity=findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.securityQuestions, android.R.layout.simple_spinner_item);

        spinnerSecurity.setAdapter(adapter);
        spinnerSecurity.setOnItemSelectedListener(this);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.checkDataEntered(firstName);
                MainActivity.checkDataEntered(lastName);
                MainActivity.checkDataEntered(email);
                MainActivity.checkDataEntered(phone);
                MainActivity.checkDataEntered(studentID);
                MainActivity.checkDataEntered(streetAddr);
                MainActivity.checkDataEntered(city);
                MainActivity.checkDataEntered(zip);
                MainActivity.checkDataEntered(password);
                MainActivity.checkDataEntered(spinnerEntered);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                // checking if the user ID already exists in the database
                Map<String,Object> processedData = new HashMap<String,Object>();
                processedData.put("FirstName",firstName.getText().toString());
                //storing phone number as string to prevent overflow
                int studentId = -1;
                try {
                    studentId = Integer.parseInt(studentID.getText().toString());
                }
                catch(Exception e){
                    Toast toast = Toast.makeText(Register.this, "Invalid Student ID", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                processedData.put("ID",studentId);
                int zipCode = -1;
                try {
                    zipCode = Integer.parseInt(zip.getText().toString());
                }
                catch(Exception e){
                    Toast toast = Toast.makeText(Register.this, "Invalid Zip Code", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(zip.getText().toString().length() != 5)
                {
                    Toast toast = Toast.makeText(Register.this, "Invalid Zip Code", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                processedData.put("ZipCode",zipCode);
                String pass = password.getText().toString();
                String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,20}$";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(pass);

                if(m.matches()) {
                    processedData.put("Password",pass);
                }
                else{
                    Toast toast = Toast.makeText(Register.this, "Password must be between 8 and 20 digits and contain at least one digit, "+" " +
                            "at least one uppercase letter, at least one lowercase letter, "+
                            "at least one special character, and no whitespace", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                EditText[] textfields = new EditText[]{
                        firstName,lastName,email,streetAddr,city, phone
                };
                String[] fieldNames = new String[]{
                        "FirstName","LastName","Email","StreetAddress","City", "PhoneNumber"
                };
                for(int i = 0; i < fieldNames.length; i++)
                    processedData.put(fieldNames[i],textfields[i].getText().toString());
                if(securityQuestion == null)
                {
                    Toast toast = Toast.makeText(Register.this, "Select Security Question", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                processedData.put("SecurityQuestion",securityQuestion);
                processedData.put("SecurityAnswer", spinnerEntered.getText().toString());
                db.collection("Users").whereEqualTo("ID",studentId).get().addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.getResult().isEmpty())
                                {
                                    db.collection("Users").whereEqualTo("Email",email.getText().toString()).get().addOnCompleteListener(
                                            new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.getResult().isEmpty())
                                                    {
                                                        db.collection("Users").add(processedData);
                                                    }
                                                    else
                                                    {
                                                        Toast toast = Toast.makeText(Register.this,
                                                                "Email is already registered, have you forgotten your password?",
                                                                Toast.LENGTH_SHORT);
                                                        toast.show();
                                                    }
                                                }
                                            }
                                    );
                                }
                                else
                                {
                                    Toast toast = Toast.makeText(Register.this,
                                            "Student ID already registered, have you forgotten your password?",
                                            Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        }
                );

                Intent intent = new Intent(Register.this, MainActivity.class);
                intent.putExtra("Info", 2);
                startActivity(intent);
            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName.getText().clear();
                lastName.getText().clear();
                email.getText().clear();
                phone.getText().clear();
                studentID.getText().clear();
                streetAddr.getText().clear();
                city.getText().clear();
                zip.getText().clear();
            }
        });

    }
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        securityQuestion = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}