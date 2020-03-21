package com.example.weatherapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SummaryPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentlist = new ArrayList<>();

    public SummaryPagerAdapter(FragmentManager manager) {
        super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public void addFragment(String location, String deg) {
        Iterator<Fragment> iterator = fragmentlist.iterator();
        while(iterator.hasNext()) {
            if (((summaryFragment) iterator.next()).location.equals(location)) return;
        }
        fragmentlist.add(new summaryFragment(location, deg));
        notifyDataSetChanged();
    }

    public void removeFragment(String location) {
        Iterator<Fragment> iterator = fragmentlist.iterator();
        while(iterator.hasNext()) {
            if (((summaryFragment) iterator.next()).location.equals(location)) {
                iterator.remove();
                notifyDataSetChanged();
                break;
            }
        }
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
        return "●";
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}