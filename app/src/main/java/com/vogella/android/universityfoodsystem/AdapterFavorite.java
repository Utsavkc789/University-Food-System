package com.vogella.android.universityfoodsystem;
import android.content.Context;
import android.icu.text.IDNA;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;



public class AdapterFavorite extends RecyclerView.Adapter<Adapter_Favorite> {

    private Context Class;
    ArrayList<Info_favitem> items;

    public AdapterFavorite(Context aClass, ArrayList<Info_favitem> items) {
        Class = aClass;
        this.items = items;
    }

    @NonNull
    @Override
    public Adapter_Favorite onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav,parent,false);
        return new Adapter_Favorite(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Favorite holder, int position) {
            Info_favitem adapter_ = items.get(position);
            holder.setDetails(adapter_);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}



class Adapter_Favorite extends RecyclerView.ViewHolder {
    private TextView name,price,description;
    private ImageButton favButton;
    private AdapterFavorite adapter;

    Adapter_Favorite(View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.name);
        price = itemView.findViewById(R.id.price);
        description = itemView.findViewById(R.id.description);
        favButton = itemView.findViewById(R.id.favButton);

        favButton.setOnClickListener(view ->{
            String item_name = name.getText().toString();
            //adapter.items.remove(getAdapterPosition());
            //adapter.notifyItemRemoved(getAdapterPosition());
            delete_item(item_name);
                }
                );

    }

    private void delete_item(String name) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child("Fav_item").child(name);
        ref.removeValue();
        Toast.makeText(itemView.getContext(),"Item removed!",Toast.LENGTH_SHORT).show();

    }

    void setDetails(Info_favitem item){
        name.setText(item.getName());
        description.setText(item.getDescription());
        price.setText(item.getPrice());
    }



}

