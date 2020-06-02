package com.example.ajoudongfe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public static String BASE_URL= "http://10.0.2.2:8000";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        final ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_new_member, container, false);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        clubID = 134;
        Call<UserListObject> call = getAppliedUserList(clubID);

        call.enqueue(new Callback<UserListObject>() {
            @Override
            public void onResponse(Call<UserListObject> call, Response<UserListObject> response) {
                UserListObject data = response.body();
                listData = data.getContent();
                for(int i= 0; i<listData.size();i++)
                {
                    System.out.println(listData.get(i).getuSchoolID());
                }

                newMemberRecyclerView = (RecyclerView)rootView.findViewById(R.id.newMemberRecyclerView);
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



        return rootView;
    }

    private Call<UserListObject> getAppliedUserList(int clubID)
    {
        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.getAppliedUserList(clubID);
    }
}
