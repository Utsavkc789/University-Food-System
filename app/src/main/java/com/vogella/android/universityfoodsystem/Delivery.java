//Links to pickup and delivery
//Should display confirmed order after successfully completing delivery selection
package com.vogella.android.universityfoodsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Delivery extends AppCompatActivity {
    private static final int NUM_PAGES = 2;
    public static ViewPager2 viewPager;
    private String orderID;
    private FragmentStateAdapter pagerAdapter2;
    private String[] titles = new String[]{"Delivery", "Pickup" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        Intent intent = getIntent();
        orderID = intent.getStringExtra("orderID");
        viewPager = findViewById(R.id.mypager2);
        pagerAdapter2 = new MyPagerAdapter((FragmentActivity) this, orderID);
        viewPager.setAdapter(pagerAdapter2);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout2);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(titles[position])).attach();
    }


    private class MyPagerAdapter extends FragmentStateAdapter {

        private String orderID;
        public MyPagerAdapter(FragmentActivity fa, String orderID)
        {
            super(fa);
            this.orderID = orderID;
        }

        @Override
        public Fragment createFragment(int pos) {
            switch (pos) {
                case 0: {
                    return DeliveryFragment.newInstance("fragment 1",orderID);
                }
                case 1: {

                    return PickupFragment.newInstance("fragment 2",orderID);
                }
                default:
                    return DeliveryFragment.newInstance("fragment 1, Default",orderID);
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