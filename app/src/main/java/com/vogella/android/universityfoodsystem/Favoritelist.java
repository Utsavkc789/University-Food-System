package com.vogella.android.universityfoodsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Favoritelist extends AppCompatActivity {
    FirebaseDatabase mroot = FirebaseDatabase.getInstance();
    DatabaseReference musers = mroot.getReference("users");
    DatabaseReference mfav_item = musers.child("Fav_item");
    ListView listView;
    RecyclerView recyclerview;
    ArrayList<Info_favitem> myArrayList =new ArrayList<>();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritelist);

        ArrayAdapter<Info_favitem> myAdapter = new ArrayAdapter<Info_favitem>(Favoritelist.this, android.R.layout.simple_list_item_1,myArrayList);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        AdapterFavorite adapter = new AdapterFavorite(this,myArrayList);
        recyclerview.setAdapter(adapter);

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        mfav_item.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                myArrayList.add(snapshot.getValue(Info_favitem.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}