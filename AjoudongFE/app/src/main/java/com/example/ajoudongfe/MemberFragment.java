package com.example.ajoudongfe;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
    public static String BASE_URL= "http://10.0.2.2:8000";
    private Button addNewMemberButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        final ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_member, container, false);
        addNewMemberButton = (Button)rootView.findViewById(R.id.addNewMemberButton);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        clubID = 1;



        Call<UserListObject> call = getMemberList(clubID);

        call.enqueue(new Callback<UserListObject>() {
            @Override
            public void onResponse(Call<UserListObject> call, Response<UserListObject> response) {
                UserListObject data = response.body();
                listData = data.getContent();
                for (int i = 0; i < listData.size(); i++) {
                    System.out.println(listData.get(i).getuSchoolID());
                }
                MemberRecyclerView = (RecyclerView) rootView.findViewById(R.id.memberRecyclerView);
                MemberRecyclerAdapter = new MemberRecyclerAdapter(getActivity(), listData, clubID);
                MemberRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));MemberRecyclerView.setAdapter(MemberRecyclerAdapter);

            }

            @Override
            public void onFailure(Call<UserListObject> call, Throwable t) {
                t.printStackTrace();
                Log.e("연결실패", "실패");
            }
        });

        addNewMemberButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                NewMemberPopup newMemberPopup = new NewMemberPopup(clubID);

                newMemberPopup.show(getFragmentManager(), "dialog");
            }
        });

        return rootView;
    }

    private Call<UserListObject> getMemberList(int clubID)
    {
        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.getMemberList(clubID);
    }
}
