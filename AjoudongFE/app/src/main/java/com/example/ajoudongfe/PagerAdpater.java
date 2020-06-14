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
                MemberFragment memberFragment = new MemberFragment();
                return memberFragment;

            default:
                return null;

        }

    }

    @Override
    public int getItemPosition(Object object)
    {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return num;
    }
}
