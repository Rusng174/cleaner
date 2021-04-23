package com.cleaner.emptykesh.PageAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cleaner.emptykesh.Fragments.BoosterPhone;
import com.cleaner.emptykesh.Fragments.CleanerJunk;
import com.cleaner.emptykesh.Fragments.CoolerCPU;
import com.cleaner.emptykesh.Fragments.SaverBattery;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public MyPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                BoosterPhone tab1 = new BoosterPhone();
                return tab1;
            case 1:
                SaverBattery tab2 = new SaverBattery();
                return tab2;
            case 2:
                CoolerCPU tab3 = new CoolerCPU();
                return tab3;
            case 3:
                CleanerJunk tab4 = new CleanerJunk();
                return tab4;

//      case 4:
//        SubscrationFragment tab5 = new SubscrationFragment();
//        return tab5;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

