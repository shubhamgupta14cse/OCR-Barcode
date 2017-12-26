package com.example.shubham_gupta.cameratest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;


public class ViewPagerAdapter extends FragmentPagerAdapter {

  ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> tabTiltles = new ArrayList<>();
    public ViewPagerAdapter(FragmentManager fm){
       super(fm);
    }
    public void addFragments(Fragment fragment, String titles){
        this.fragments.add(fragment);
        this.tabTiltles.add(titles);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTiltles.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}