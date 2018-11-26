package com.kimvan.hung.vocabulary.testWord.learningGraph;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by h on 22/02/2018.
 */

public class StaePagerGraphAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> listFragment = new ArrayList<>();

    public StaePagerGraphAdapter(FragmentManager fm){
        super(fm);
        listFragment.add(new DatePagerFragment());
        listFragment.add(new MonthPagerFragment());
        listFragment.add(new YearPagerFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }
}
