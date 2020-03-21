package com.example.weatherapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;
import java.util.List;

public class DetailPagerAdapter extends FragmentPagerAdapter {
    private String[] titles = {"TODAY", "WEEKLY", "PHOTOS"};
    private final List<Fragment> fragmentlist = new ArrayList<>();

    public DetailPagerAdapter(FragmentManager manager) {
        super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public void addFragment(Fragment fragment) {
        fragmentlist.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentlist.get(position);
    }

    @Override
    public int getCount() {
        return fragmentlist.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}