package com.kindred.kindred;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by usman on 1/29/2018.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter{
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                PlaceOrderFragment placeOrderFragment = new PlaceOrderFragment();
                return placeOrderFragment;
            case 1:
                OrdersFragment ordersFragment = new OrdersFragment();
                return ordersFragment;
            case 2:
                OrdersConfirmedFragment ordersConfirmedFragment = new OrdersConfirmedFragment();
                return ordersConfirmedFragment;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "PLACE ORDERS";
            case 1:
                return "ORDERS";
            case 2:
                return "CONFIRMED";
            default:
                return null;
        }
    }
}
