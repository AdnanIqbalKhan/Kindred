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
                return new PlaceOrderFragment();
            case 1:
                return new OrdersFragment();
            case 2:
                return new OrdersConfirmedFragment();
            case 3:
               return  new OrdersPostedFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    public CharSequence getPageTitle(int position){
        return "";
    }

}
