package com.example.ajoudongfe;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class NewMemberTabPagerAdapter extends FragmentStatePagerAdapter
{
    // Count number of tabs
    private int tabCount;

    public NewMemberTabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                NewMemberFragment newMemberFragment = new NewMemberFragment();
                return newMemberFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
