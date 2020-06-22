package com.example.ajoudongfe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdpater extends FragmentStatePagerAdapter {
    int num;
    int clubID;
    public PagerAdpater(FragmentManager fm, int num, int clubID) {
        super(fm);
        this.num = num;
        this.clubID = clubID;
    }

    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putInt("clubID", clubID);

        switch (position)
        {
            case 0:
                NewMemberFragment newMemberFragment = new NewMemberFragment();
                newMemberFragment.setArguments(bundle);
                return newMemberFragment;
            case 1:
                MemberFragment memberFragment = new MemberFragment();
                memberFragment.setArguments(bundle);
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
