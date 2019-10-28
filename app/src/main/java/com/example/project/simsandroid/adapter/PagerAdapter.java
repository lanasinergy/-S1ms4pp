package com.example.project.simsandroid.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.project.simsandroid.ui.home.DetailLeadFragment;
import com.example.project.simsandroid.ui.home.TenderSalesFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    public PagerAdapter(@NonNull FragmentManager fm, int tabCount) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                DetailLeadFragment tab1 = new DetailLeadFragment();
                return tab1;
            case 1:
                TenderSalesFragment tab2 = new TenderSalesFragment();
                return tab2;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
