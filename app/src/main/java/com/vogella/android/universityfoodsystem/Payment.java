//Links to credit payment, meal plan payment, and gift card payment
//Should receive order total, student ID and order to display after delivery chosen
package com.vogella.android.universityfoodsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Payment extends AppCompatActivity {
    double orderCost;
    String orderID;
    String restID;
    private static final int NUM_PAGES = 2;
    public static ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private String[] titles = new String[]{"Pay With Credit", "Pay With Meal Plan"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String TAG = Payment.class.getName();
        try {
            super.onCreate(savedInstanceState);
            Intent intent = getIntent();
            orderID = intent.getStringExtra("orderID");
            FragmentActivity myself = this;
            FirebaseFirestore.getInstance().collection("UserOrder").document(orderID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    orderCost = documentSnapshot.getDouble("Cost");
                    restID = documentSnapshot.getString("Restaurant");
                    setContentView(R.layout.activity_payment);

                    viewPager = findViewById(R.id.mypager);
                    pagerAdapter = new MyPagerAdapter(myself);
                    viewPager.setAdapter(pagerAdapter);
                    TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
                    new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(titles[position])).attach();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "onCreateView", e);
            throw e;
        }



    }

    private class MyPagerAdapter extends FragmentStateAdapter {

        public MyPagerAdapter(FragmentActivity fa) {
            super(fa);
        }


        @Override
        public Fragment createFragment(int pos) {
            switch (pos) {
                case 0: {
                    return CreditPayment.newInstance("fragment 1", orderCost, orderID, restID);
                }
                case 1: {

                    return MealPlanPayment.newInstance("fragment 2", orderCost, orderID, restID);
                }
                default:
                    return CreditPayment.newInstance("fragment 1, Default", orderCost, orderID, restID);
            }
        }
        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }


    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) { super.onBackPressed(); }
        else { viewPager.setCurrentItem(viewPager.getCurrentItem() - 1); }
    }
}