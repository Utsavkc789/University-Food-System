package com.vogella.android.universityfoodsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifyAttributes extends AppCompatActivity {

    String rname;
    ArrayList<String> infoStuff = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyattributes);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            rname = extras.getString("key_7RNAME");
            //The key argument here must match that used in the other activity
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        TextView name_TV = (TextView) findViewById(R.id.MA_name_TextView);

        EditText loc_ET = (EditText) findViewById(R.id.MA_loc_EditText);
        EditText open_ET = (EditText) findViewById(R.id.MA_open_EditText);
        EditText close_ET = (EditText) findViewById(R.id.MA_close_EditText);

        db.collection("Restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                String curr_rest = document.getString("Name");

                                if(curr_rest.matches(rname))
                                {
                                    String rloc = document.getString("Location");
                                    String rOpen = document.getString("OpenTime");
                                    String rClose = document.getString("CloseTime");
                                    //rtime = document.getString("Time")

                                    name_TV.setText("Name (Unchangeable): " + rname);
                                    loc_ET.setText(rloc);
                                    open_ET.setText(rOpen);
                                    close_ET.setText(rClose);
                                }
                            }
                        }
                    }
                });

        Button MA_exit_Button = (Button) findViewById(R.id.MA_exit_Button);
        MA_exit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModifyAttributes.this, ModifyMenu.class);
                intent.putExtra("RestaurantName", rname);
                startActivity(intent);
            }
        });

        Button confirmChange_Button = (Button) findViewById(R.id.confirmChange_Button);
        confirmChange_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String new_loc = loc_ET.getText().toString();
                String new_open = open_ET.getText().toString();
                String new_close = close_ET.getText().toString();

                Pattern regex = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]");
                Matcher matcher1 = regex.matcher(new_open);
                Matcher matcher2 = regex.matcher(new_close);
                Boolean correctform = matcher1.find() && matcher2.find();
                if(!correctform)
                {
                    Toast.makeText(getApplicationContext(), "Invalid Time Format", Toast.LENGTH_SHORT).show();
                    return;
                }

                db.collection("Restaurants")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    for (QueryDocumentSnapshot document : task.getResult())
                                    {
                                        String curr_rest = document.getString("Name");

                                        if(curr_rest.matches(rname))
                                        {

                                            document.getReference().update("Location",new_loc);
                                            document.getReference().update("OpenTime",new_open);
                                            document.getReference().update("CloseTime",new_close);
                                        }
                                    }
                                }
                            }
                        });

                Toast.makeText(getApplicationContext(), "Changes Applied Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
