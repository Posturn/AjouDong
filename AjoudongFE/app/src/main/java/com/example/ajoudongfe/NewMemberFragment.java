package com.example.ajoudongfe;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
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

public class NewMemberFragment extends Fragment {
    private RecyclerView newMemberRecyclerView;
    private NewMemberRecyclerAdapter newMemberRecyclerAdapter;
    private int clubID;
    private List<MemberInfoObject> listData = new ArrayList<>();
    private Retrofit retrofit;
    public static String BASE_URL = Keys.getServerUrl();
    private Button appliedUserCSVButton;
    private DownloadManager.Request request;
    private DownloadManager downloadManager;
    private long downloadQueueID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_new_member, container, false);
        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        appliedUserCSVButton = (Button) rootView.findViewById(R.id.appliedUserCSVButton);
        newMemberRecyclerView = (RecyclerView) rootView.findViewById(R.id.newMemberRecyclerView);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Bundle bundle = getArguments();
        clubID = bundle.getInt("clubID");


        appliedUserCSVButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadAppliedUserCSV(BASE_URL + "/management/appliedusercsv/" + Integer.toString(clubID));
            }
        });

        init();

        return rootView;
    }

    private void init() {
        Call<UserListObject> call = getAppliedUserList(clubID);


        call.enqueue(new Callback<UserListObject>() {
            @Override
            public void onResponse(Call<UserListObject> call, Response<UserListObject> response) {
                UserListObject data = response.body();
                listData = data.getContent();
                for (int i = 0; i < listData.size(); i++) {
                    System.out.println(listData.get(i).getuSchoolID());
                }


                newMemberRecyclerAdapter = new NewMemberRecyclerAdapter(getActivity(), listData, clubID);
                newMemberRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                newMemberRecyclerView.setAdapter(newMemberRecyclerAdapter);
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


    private void downloadAppliedUserCSV(String URL) {
        Log.d("URL", URL);
        request = new DownloadManager.Request(Uri.parse(URL))
                .setDescription("Downloaind appliedUser list")
                .setTitle("appliedUsers.csv")
                .setVisibleInDownloadsUi(true)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "appliedUsers.csv");
        Log.d("adf", "enas");
        downloadQueueID = downloadManager.enqueue(request);

    }

    private Call<UserListObject> getAppliedUserList(int clubID) {
        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.getAppliedUserList(clubID);
    }

}
