package com.fierydevs.mytablayout.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fierydevs.mytablayout.Tab1;
import com.fierydevs.mytablayout.Tab2;

/**
 * Created by Pranit on 11-09-2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    CharSequence[] titles; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int numbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence[] titles, int numbOfTabs) {
        super(fm);
        this.titles = titles;
        this.numbOfTabs = numbOfTabs;
    }

    // This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {    // if the position is 0 we are returning the First tab
            Tab1 tab1 = new Tab1();
            return tab1;
        } else {    // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            Tab2 tab2 = new Tab2();
            return tab2;
        }
    }

    @Override
    public int getCount() {
        return numbOfTabs;
    }

    // This method return the titles for the Tabs in the Tab Strip
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
