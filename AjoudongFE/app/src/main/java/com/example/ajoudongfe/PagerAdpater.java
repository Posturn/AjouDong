package com.example.ajoudongfe;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdpater extends FragmentStatePagerAdapter {
    int num;
    public PagerAdpater(FragmentManager fm, int num) {
        super(fm);
        this.num = num;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                NewMemberFragment newMemberFragment = new NewMemberFragment();
                return newMemberFragment;
            case 1:
                TabFragment2 tab2 = new TabFragment2();
                return tab2;

            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return num;
    }
}
