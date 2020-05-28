package com.example.ajoudongfe;

import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MemberManagementAdapter extends FragmentStatePagerAdapter {
    private int tabCount;

    public MemberManagementAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                Log.d("활성 프래그먼트", "신규회원");
                NewMemberFragment newMemberFragment = new NewMemberFragment();
                return newMemberFragment;
            case 1:
                Log.d("활성 프래그먼트", "전체회원");
                return new MemberFragment();
            default:
                Log.d("디폴트", "디폴트");
                return null;
        }
    }

    @Override
    public int getCount() {
        return 0;
    }
}
