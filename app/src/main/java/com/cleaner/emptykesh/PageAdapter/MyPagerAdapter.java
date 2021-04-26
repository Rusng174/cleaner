package com.cleaner.emptykesh.PageAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cleaner.emptykesh.Fragments.BoosterPhoneFragment;
import com.cleaner.emptykesh.Fragments.CleanerJunkFragment;
import com.cleaner.emptykesh.Fragments.CoolerCPUFragment;
import com.cleaner.emptykesh.Fragments.SaverBatteryFragment;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public MyPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                BoosterPhoneFragment tab1 = new BoosterPhoneFragment();
                return tab1;
            case 1:
                SaverBatteryFragment tab2 = new SaverBatteryFragment();
                return tab2;
            case 2:
                CoolerCPUFragment tab3 = new CoolerCPUFragment();
                return tab3;
            case 3:
                CleanerJunkFragment tab4 = new CleanerJunkFragment();
                return tab4;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}