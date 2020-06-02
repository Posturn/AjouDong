package com.example.ajoudongfe;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClubMemberChartActivity extends AppCompatActivity {

    public static String BASE_URL= "http://10.0.2.2:8000";
    private Retrofit retrofit;
    private int parameterclubID;
    private StatisticObject stat;

    private RetroService retroService;

    PieChart genderchart;
    PieChart majorchart;
    BarChart schoolchart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_chart);

        parameterclubID = getIntent().getIntExtra("clubID", 0);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retroService = retrofit.create(RetroService.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.clubcharttoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("회원 비율");

        genderchart=findViewById(R.id.piechart1);
        majorchart=findViewById(R.id.piechart2);
        schoolchart=findViewById(R.id.barchart);

        makegenderchart(parameterclubID);
        makemajorchart(parameterclubID);
        makeschoolchart(parameterclubID);

    }

    protected void makegenderchart(int paraclubID){
        Call<StatisticObject> call = retroService.getClubStatistic(paraclubID);
        call.enqueue(new Callback<StatisticObject>() {

            @Override
            public void onResponse(Call<StatisticObject> call, Response<StatisticObject> response) {
                stat=response.body();

                Log.v("남자", String.valueOf(stat.getMenNumber()));
                Log.v("여자", String.valueOf(stat.getWomenNumber()));

                genderchart.addPieSlice(new PieModel("남", stat.getMenNumber(), Color.parseColor("#005BAC")));
                genderchart.addPieSlice(new PieModel("여", stat.getMenNumber(), Color.parseColor("#EC1CDD")));

                genderchart.startAnimation();
            }

            @Override
            public void onFailure(Call<StatisticObject> call, Throwable t) {
                Log.v("실패", "실패");
            }

        });
    }

    protected void makemajorchart(int paraclubID){
        Call<StatisticObject> call = retroService.getClubStatistic(paraclubID);
        call.enqueue(new Callback<StatisticObject>() {

            @Override
            public void onResponse(Call<StatisticObject> call, Response<StatisticObject> response) {
                stat=response.body();

                majorchart.addPieSlice(new PieModel("공과대학", stat.getEngineeringRatio(), Color.parseColor("#005BAC")));
                majorchart.addPieSlice(new PieModel("정보통신대학", stat.getITRatio(), Color.parseColor("#B8DDFF")));
                majorchart.addPieSlice(new PieModel("자연과학대학", stat.getNaturalscienceRatio(), Color.parseColor("#F5A21E")));
                majorchart.addPieSlice(new PieModel("경영대학", stat.getManagementRatio(), Color.parseColor("#FBCB7E")));
                majorchart.addPieSlice(new PieModel("인문대학", stat.getHumanitiesRatio(), Color.parseColor("#707070")));
                majorchart.addPieSlice(new PieModel("사회과학대학", stat.getSocialscienceRatio(), Color.parseColor("#54B486")));
                majorchart.addPieSlice(new PieModel("간호대학", stat.getNurseRatio(), Color.parseColor("#C9C642")));

                majorchart.startAnimation();
            }

            @Override
            public void onFailure(Call<StatisticObject> call, Throwable t) {
                Log.v("실패", "실패");
            }

        });
    }

    protected void makeschoolchart(int paraclubID){
        Call<StatisticObject> call = retroService.getClubStatistic(paraclubID);
        call.enqueue(new Callback<StatisticObject>() {

            @Override
            public void onResponse(Call<StatisticObject> call, Response<StatisticObject> response) {
                stat=response.body();

                schoolchart.addBar(new BarModel("12이상", stat.getOverRatio12(), 0xFF56B7F1));
                schoolchart.addBar(new BarModel("13학번", stat.getRatio13(), 0xFF56B7F1));
                schoolchart.addBar(new BarModel("14학번", stat.getRatio14(), 0xFF56B7F1));
                schoolchart.addBar(new BarModel("15학번", stat.getRatio15(), 0xFF56B7F1));
                schoolchart.addBar(new BarModel("16학번", stat.getRatio16(), 0xFF56B7F1));
                schoolchart.addBar(new BarModel("17학번", stat.getRatio17(), 0xFF56B7F1));
                schoolchart.addBar(new BarModel("18학번", stat.getRatio18(), 0xFF56B7F1));
                schoolchart.addBar(new BarModel("19학번", stat.getRatio19(), 0xFF56B7F1));

                majorchart.startAnimation();
            }

            @Override
            public void onFailure(Call<StatisticObject> call, Throwable t) {
                Log.v("실패", "실패");
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{//뒤로가기
                finish();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
