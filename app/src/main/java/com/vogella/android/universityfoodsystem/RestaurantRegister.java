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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestaurantRegister extends AppCompatActivity {
    String openTime;
    String closeTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Intent intent = getIntent();

        Button bSubmit = findViewById(R.id.regConfirm);
        Button bCancel = findViewById(R.id.regCancel);
        EditText restName = findViewById(R.id.restNameEntered);
        EditText location = findViewById(R.id.locationEntered);
        EditText email = findViewById(R.id.emailREntered);
        EditText price = findViewById(R.id.priceREntered);
        EditText password = findViewById(R.id.passREntered1);

       Spinner spinnerOpen=findViewById(R.id.timeOpenSpinner);
        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(this, R.array.restaurantTimes, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerOpen.setAdapter(adapter1);

        spinnerOpen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                openTime = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        Spinner spinnerClose=findViewById(R.id.timeCloseSpinner);
        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(this, R.array.restaurantTimes, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerClose.setAdapter(adapter2);
        spinnerClose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                closeTime = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.checkDataEntered(restName);
                MainActivity.checkDataEntered(location);
                MainActivity.checkDataEntered(email);
                MainActivity.checkDataEntered(price);
                MainActivity.checkDataEntered(password);
                //getting db reference
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                // checking if the user ID already exists in the database
                Map<String,Object> processedData = new HashMap<String,Object>();
                int price1 = -1;
                try {
                    price1 = Integer.parseInt(price.getText().toString());
                }
                catch(Exception e){
                    Toast toast = Toast.makeText(RestaurantRegister.this, "Invalid price: " + price.getText().toString(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                processedData.put("Price",price1);
                String pass = password.getText().toString();
                String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,20}$";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(pass);

                if(m.matches()) {
                    processedData.put("Password",pass);
                }
                else{
                    Toast toast = Toast.makeText(RestaurantRegister.this, "Password must be between 8 ad 20 digits and contain at least one digit, "+" " +
                            "at least one uppercase letter, at least one lowercase letter, "+
                            "at least one special character, and no whitespace", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                EditText[] textfields = new EditText[]{
                        location,email,restName
                };
                String[] fieldNames = new String[]{
                        "Location","Email","Name"
                };
                for(int i = 0; i < fieldNames.length; i++)
                    processedData.put(fieldNames[i],textfields[i].getText().toString());
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.WEEK_OF_YEAR,-10);

                processedData.put("AdCreationDateTime",(double)cal.getTimeInMillis());
                processedData.put("AdDiscount",-1);
                processedData.put("AdFees",0);
                processedData.put("OpenTime",openTime);
                processedData.put("CloseTime",closeTime);
                db.collection("Restaurants").whereEqualTo("Name",restName.getText().toString()).get().addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.getResult().isEmpty())
                                {
                                    db.collection("Restaurants").whereEqualTo("Email",email.getText().toString()).get().addOnCompleteListener(
                                            new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.getResult().isEmpty())
                                                    {
                                                        db.collection("Restaurants").add(processedData);
                                                    }
                                                    else
                                                    {
                                                        Toast toast = Toast.makeText(RestaurantRegister.this,
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
                                    Toast toast = Toast.makeText(RestaurantRegister.this,
                                            "Restaurant already registered, have you forgotten your password?",
                                            Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        }
                );

                Intent intent = new Intent(RestaurantRegister.this, RestaurantLogin.class);
                intent.putExtra("Info", 2);
                startActivity(intent);
            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restName.getText().clear();
                location.getText().clear();
                email.getText().clear();
                price.getText().clear();
            }
        });

    }
}