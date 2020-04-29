package com.example.ajoudongfe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class ManagerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_main);

        ConstraintLayout editclubinfo = (ConstraintLayout)findViewById(R.id.editclubinfoLayout);
        ConstraintLayout membermanagement = (ConstraintLayout)findViewById(R.id.managementclubmemberLayout);
        ConstraintLayout newevent = (ConstraintLayout)findViewById(R.id.neweventLayout);

        editclubinfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), ManagerClubInfoEdit.class);
                startActivity(intent);
                return false;
            }
        });

        membermanagement.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), ManagerMemberManagementActivity.class);
                startActivity(intent);
                return false;
            }
        });

        newevent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), ManagerNewEventActivity.class);
                startActivity(intent);
                return false;
            }
        });
    }
}
