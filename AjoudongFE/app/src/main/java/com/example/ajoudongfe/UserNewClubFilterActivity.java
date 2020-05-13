package com.example.ajoudongfe;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserNewClubFilterActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_new_club_filter);
        final TextView recruit_y = (TextView) findViewById(R.id.recruit_y);
        final TextView recruit_n = (TextView) findViewById(R.id.recruit_n);
        final TextView recruit_interview_n = (TextView) findViewById(R.id.recruit_interview_n);
        final TextView recruit_interview_y = (TextView) findViewById(R.id.recruit_interview_y);
        final TextView recruit_limit_n = (TextView) findViewById(R.id.recruit_limit_n);
        recruit_y.setOnClickListener(this);
        recruit_n.setOnClickListener(this);
        recruit_interview_n.setOnClickListener(this);
        recruit_interview_y.setOnClickListener(this);
        recruit_limit_n.setOnClickListener(this);

        final TextView atmosphere_show = (TextView) findViewById(R.id.atmosphere_show);
        final TextView atmosphere_competition = (TextView) findViewById(R.id.atmosphere_competition);
        final TextView atmosphere_regular = (TextView) findViewById(R.id.atmosphere_regular);
        final TextView atmosphere_contest = (TextView) findViewById(R.id.atmosphere_contest);
        final TextView atmosphere_obyb = (TextView) findViewById(R.id.atmosphere_obyb);
        final TextView atmosphere_afterparty = (TextView) findViewById(R.id.atmosphere_afterparty);
        final TextView atmosphere_study = (TextView) findViewById(R.id.atmosphere_study);
        final TextView atmosphere_friendship = (TextView) findViewById(R.id.atmosphere_friendship);
        atmosphere_show.setOnClickListener(this);
        atmosphere_competition.setOnClickListener(this);
        atmosphere_regular.setOnClickListener(this);
        atmosphere_contest.setOnClickListener(this);
        atmosphere_obyb.setOnClickListener(this);
        atmosphere_afterparty.setOnClickListener(this);
        atmosphere_study.setOnClickListener(this);
        atmosphere_friendship.setOnClickListener(this);

        final TextView moeny_51 = (TextView) findViewById(R.id.moeny_51);
        final TextView moeny_12 = (TextView) findViewById(R.id.moeny_12);
        final TextView moeny_23 = (TextView) findViewById(R.id.moeny_23);
        final TextView moeny_3 = (TextView) findViewById(R.id.moeny_3);
        final TextView moeny_1time = (TextView) findViewById(R.id.moeny_1time);
        final TextView moeny_no = (TextView) findViewById(R.id.moeny_no);
        moeny_51.setOnClickListener(this);
        moeny_12.setOnClickListener(this);
        moeny_23.setOnClickListener(this);
        moeny_3.setOnClickListener(this);
        moeny_1time.setOnClickListener(this);
        moeny_no.setOnClickListener(this);

        final TextView day_mon = (TextView) findViewById(R.id.day_mon);
        final TextView day_tue = (TextView) findViewById(R.id.day_tue);
        final TextView day_wed = (TextView) findViewById(R.id.day_wed);
        final TextView day_thu = (TextView) findViewById(R.id.day_thu);
        final TextView day_fri = (TextView) findViewById(R.id.day_fri);
        final TextView day_sat = (TextView) findViewById(R.id.day_sat);
        final TextView day_sun = (TextView) findViewById(R.id.day_sun);
        final TextView day_every = (TextView) findViewById(R.id.day_every);
        day_mon.setOnClickListener(this);
        day_tue.setOnClickListener(this);
        day_wed.setOnClickListener(this);
        day_thu.setOnClickListener(this);
        day_fri.setOnClickListener(this);
        day_sat.setOnClickListener(this);
        day_sun.setOnClickListener(this);
        day_every.setOnClickListener(this);


        final TextView time_am = (TextView) findViewById(R.id.time_am);
        final TextView time_pm = (TextView) findViewById(R.id.time_pm);
        final TextView time_black = (TextView) findViewById(R.id.time_black);
        final TextView time_night = (TextView) findViewById(R.id.time_night);
        final TextView time_all = (TextView) findViewById(R.id.time_all);
        time_am.setOnClickListener(this);
        time_pm.setOnClickListener(this);
        time_black.setOnClickListener(this);
        time_night.setOnClickListener(this);
        time_all.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar) ;
        ActionBar actionBar = getSupportActionBar() ;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        Button filterButton = (Button) findViewById(R.id.mainfilterbtn);
        filterButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(),"버튼눌림",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
            case R.id.toolbarFilter:
                Toast.makeText(getApplicationContext(),"초기화",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item) ;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.isSelected()) {
            v.setSelected(false);
        } else {
            v.setSelected(true);
        }
    }
}
