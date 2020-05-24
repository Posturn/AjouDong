package com.example.ajoudongfe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class ManagerMemberManagementActivity extends AppCompatActivity {
    private TabLayout memberManagementTabLayout;
    private ViewPager managementViewPager;
    private NewMemberTabPagerAdapter newMemberTabPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_member_management);

        managementViewPager = (ViewPager) findViewById(R.id.managementViewPager);
        memberManagementTabLayout = (TabLayout)findViewById(R.id.memberManagementTabLayout);

        newMemberTabPagerAdapter = new NewMemberTabPagerAdapter(getSupportFragmentManager(), memberManagementTabLayout.getTabCount());
        managementViewPager.setAdapter(newMemberTabPagerAdapter);
        managementViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(memberManagementTabLayout));

        memberManagementTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                managementViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
