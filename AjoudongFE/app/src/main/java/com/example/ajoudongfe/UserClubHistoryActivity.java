package com.example.ajoudongfe;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserClubHistoryActivity extends AppCompatActivity {

    private int parameterclubID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_club_history_activity);

        parameterclubID = getIntent().getIntExtra("clubID", 0);
        Toast.makeText(this, parameterclubID, Toast.LENGTH_SHORT).show();
    }
}
