package com.example.ajoudongfe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewMemberFragment extends Fragment {
    private RecyclerView newMemberRecyclerView;
    private NewMemberRecyclerAdapter newMemberRecyclerAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_new_member, container, false);

        newMemberRecyclerView = (RecyclerView)rootView.findViewById(R.id.newMemberRecyclerView);
        newMemberRecyclerAdapter = new NewMemberRecyclerAdapter();
        newMemberRecyclerView.setAdapter(newMemberRecyclerAdapter);



        return rootView;
    }
}
