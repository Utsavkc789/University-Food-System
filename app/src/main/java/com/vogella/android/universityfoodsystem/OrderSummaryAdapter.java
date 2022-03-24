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

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.MyViewHolder> {


    Context context;
    ArrayList<OrderInfo> orderInfoArrayList;

    public OrderSummaryAdapter(Context context, ArrayList<OrderInfo> list) {
        this.context = context;
        this.orderInfoArrayList = list;

    }

    @NonNull
    @Override
    public OrderSummaryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.summaryitemlist,parent, false);
        return new OrderSummaryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSummaryAdapter.MyViewHolder holder, int position) {

        OrderInfo orderInfo = orderInfoArrayList.get(position);

        holder.summaryMenuItem.setText(orderInfo.getMenuItem());
        holder.summaryItemCounter.setText(String.valueOf(orderInfo.getItemQuantity()));

        //Calculate total price for each item based on number of items
        holder.totalPrice = orderInfo.itemPrice * orderInfo.itemQuantity;
        holder.summaryItemTotal.setText(String.valueOf(holder.totalPrice));

    }

    @Override
    public int getItemCount() {
        return orderInfoArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView summaryMenuItem, summaryItemCounter, summaryItemTotal;
        double totalPrice;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            summaryMenuItem = itemView.findViewById(R.id.summaryMenuItem);
            summaryItemCounter = itemView.findViewById(R.id.summaryItemCount);
            summaryItemTotal = itemView.findViewById(R.id.summaryItemTotalNum);

        }
    }
}

