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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddItem extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner category_spinner;
    private static final String[] paths = {"Breakfast", "Lunch", "Dinner"};
    ArrayAdapter<String> adapter;
    String category_selected;
    String rname;
    Boolean is_promo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            rname = extras.getString("key_5RNAME");
            //The key argument here must match that used in the other activity
        }

        category_spinner = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(AddItem.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(adapter);
        category_spinner.setOnItemSelectedListener(this);

        EditText item_name_EditText = findViewById(R.id.item_name_EditText);
        EditText item_price_EditText = findViewById(R.id.item_price_EditText);
        EditText item_description_EditText = findViewById(R.id.item_description_EditText);

        Button cancel_add_Button = (Button) findViewById(R.id.cancel_add_Button);
        cancel_add_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AddItem.this, ModifyMenu.class);
                intent.putExtra("RestaurantName", rname);
                startActivity(intent);
            }
        });

        Button add_item_Button = (Button) findViewById(R.id.confirm_add_Button);
        add_item_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item_name = item_name_EditText.getText().toString();
                String item_price = item_price_EditText.getText().toString();
                String item_description = item_description_EditText.getText().toString();

                if(item_name.matches("") || item_price.matches(""))
                {
                    Toast.makeText(getApplicationContext(), "Please fill required fields *", Toast.LENGTH_SHORT).show();
                    return;
                }

                try{
                    Double.parseDouble(item_price);
                } catch (NumberFormatException e)
                {
                    Toast.makeText(getApplicationContext(), "Invalid Item Price", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ADD TO INFORMATION TO DATABASE
                Map<String, Object> data = new HashMap<>();
                data.put("Category", category_selected);
                data.put("Description", item_description);
                data.put("Name", item_name);
                data.put("Price", Double.parseDouble(item_price));
                data.put("Restaurant", rname);

                if(is_promo)
                {
                    data.put("Promo","Yes");
                }
                else
                {
                    data.put("Promo","No");
                }

                db.collection("MenuItem")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // Do nothing if successful
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Maybe add error statement on add failure
                            }
                        });

                item_name_EditText.getText().clear();
                item_description_EditText.getText().clear();
                item_price_EditText.getText().clear();
                category_spinner.setSelection(0);

                Toast.makeText(getApplicationContext(), "Item Added Successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        category_selected = parent.getItemAtPosition(pos).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_ispromo:
                if (checked)
                    is_promo = true;
                if(!checked)
                    is_promo = false;
                break;
        }
    }
}
