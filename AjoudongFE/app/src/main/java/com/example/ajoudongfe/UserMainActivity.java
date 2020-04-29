package com.example.ajoudongfe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;

public class UserMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        ConstraintLayout majorclub = (ConstraintLayout)findViewById(R.id.majorclubLayout);
        ConstraintLayout mainclub = (ConstraintLayout)findViewById(R.id.mainclubLayout);
        ConstraintLayout newclub = (ConstraintLayout)findViewById(R.id.newclubLayout);

        majorclub.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMajorClubListActivity.class);
                startActivity(intent);
                return false;
            }
        });

        mainclub.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMainClubListActivity.class);
                startActivity(intent);
                return false;
            }
        });

        newclub.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserNewClubListActivity.class);
                startActivity(intent);
                return false;
            }
        });
    }

}
