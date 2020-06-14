package com.example.ajoudongfe;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MemberFragment extends Fragment {
    private RecyclerView MemberRecyclerView;
    private MemberRecyclerAdapter MemberRecyclerAdapter;
    private int clubID;
    private List<MemberInfoObject> listData = new ArrayList<>();
    private Retrofit retrofit;
    private Retrofit csv_retrofit;
    public static String BASE_URL= "http://10.0.2.2:8000";
    private Button addNewMemberButton;
    private Button memberCSVButton;
    private DownloadManager downloadManager;
    private DownloadManager.Request request;
    private long downloadQueueID;
    private String filename;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        final ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_member, container, false);
        addNewMemberButton = (Button)rootView.findViewById(R.id.addNewMemberButton);
        memberCSVButton = (Button)rootView.findViewById(R.id.memberCSVButton);
        MemberRecyclerView = (RecyclerView) rootView.findViewById(R.id.memberRecyclerView);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        clubID = 134;

        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);



        addNewMemberButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                NewMemberPopup newMemberPopup = new NewMemberPopup(clubID);

                newMemberPopup.show(getFragmentManager(), "dialog");
            }
        });

        memberCSVButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadMemberCSV(BASE_URL + "/management/membercsv/" + Integer.toString(clubID));
            }
        });

        init();

        return rootView;
    }

    private void init() {
        Call<UserListObject> call = getMemberList(clubID);

        call.enqueue(new Callback<UserListObject>() {
            @Override
            public void onResponse(Call<UserListObject> call, Response<UserListObject> response) {
                UserListObject data = response.body();
                listData = data.getContent();
                for (int i = 0; i < listData.size(); i++) {
                    System.out.println(listData.get(i).getuSchoolID());
                }

                MemberRecyclerAdapter = new MemberRecyclerAdapter(getActivity(), listData, clubID);
                MemberRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));MemberRecyclerView.setAdapter(MemberRecyclerAdapter);

            }

            @Override
            public void onFailure(Call<UserListObject> call, Throwable t) {
                t.printStackTrace();
                Log.e("연결실패", "실패");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void downloadMemberCSV(String URL) {

        Log.d("URL", URL);
        request = new DownloadManager.Request(Uri.parse(URL))
                .setDescription("Downloaind")
                .setTitle("members.csv")
                .setVisibleInDownloadsUi(true)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "members.csv");
        Log.d("adf", "enas");
        downloadQueueID = downloadManager.enqueue(request);



    }

    private Call<FileObject> getMemberCSV(int clubID) {
        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.getMemberCSV(clubID);

    }

    private Call<UserListObject> getMemberList(int clubID)
    {
        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.getMemberList(clubID);
    }
}
