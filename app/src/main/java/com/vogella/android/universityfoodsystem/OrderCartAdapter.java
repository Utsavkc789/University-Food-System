package com.vogella.android.universityfoodsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderCartAdapter extends RecyclerView.Adapter<OrderCartAdapter.MyViewHolder> {

    Context context;
    ArrayList<OrderInfo> orderInfoArrayList;
    ItemUpdateListener itemUpdateListener;

    public OrderCartAdapter(Context context, ArrayList<OrderInfo> list) {
        this.context = context;
        this.orderInfoArrayList = list;
    }

    public void setItemUpdateListener(ItemUpdateListener itemUpdateListener){
        this.itemUpdateListener = itemUpdateListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        OrderInfo orderInfo = orderInfoArrayList.get(position);

        holder.menuItem.setText(orderInfo.getMenuItem());
        holder.itemDescription.setText(orderInfo.getItemDescription());
        holder.itemQuantity.setText(String.valueOf(orderInfo.getItemQuantity()));
        holder.itemPrice.setText(String.valueOf(orderInfo.getItemPrice()));

        //Click and update totals when minus button is clicked
        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderInfo.itemQuantity -= 1;
                orderInfo.itemQuantity = Math.max(0,orderInfo.itemQuantity);
                holder.itemQuantity.setText(String.valueOf(orderInfo.getItemQuantity()));
                holder.totalPrice = Math.round(100 *orderInfo.itemPrice * orderInfo.itemQuantity)/100.0;
                holder.itemTotal.setText(String.valueOf(holder.totalPrice));

                if(itemUpdateListener != null){
                    itemUpdateListener.onItemUpdate(holder.totalPrice);
                }
            }
        });

        //Click and update totals when plus button is clicked
        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderInfo.itemQuantity += 1;
                holder.itemQuantity.setText(String.valueOf(orderInfo.getItemQuantity()));
                holder.totalPrice = Math.round(100 *orderInfo.itemPrice * orderInfo.itemQuantity)/100.0;
                holder.itemTotal.setText(String.valueOf(holder.totalPrice));

                if(itemUpdateListener != null){
                    itemUpdateListener.onItemUpdate(holder.totalPrice);
                }
            }
        });

        //Calculate total price for each item based on number of items
        holder.totalPrice = orderInfo.itemPrice * orderInfo.itemQuantity;
        holder.itemTotal.setText(String.valueOf(holder.totalPrice));

        if(itemUpdateListener != null){
            itemUpdateListener.onItemUpdate(holder.totalPrice);
        }
    }

    @Override
    public int getItemCount() {
        return orderInfoArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView menuItem, itemDescription, itemQuantity, itemPrice, itemTotal/*restName, firstName, lastName, emailAddress, orderNumber, userID, orderTax, deliveryFee, orderTotal*/;
        ImageView minusButton, plusButton;
        double totalPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemDescription = itemView.findViewById(R.id.itemDescriptionText);
            itemPrice = itemView.findViewById(R.id.itemPriceTop);
            itemQuantity = itemView.findViewById(R.id.itemCountText);
            menuItem = itemView.findViewById(R.id.menuItemText);
            itemTotal = itemView.findViewById(R.id.quantityPriceBottom);
            minusButton = itemView.findViewById(R.id.minusButtonImage);
            plusButton = itemView.findViewById(R.id.plusButtonImage);
        }
    }
}
