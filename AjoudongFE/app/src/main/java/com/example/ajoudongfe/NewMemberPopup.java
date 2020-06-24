package com.example.ajoudongfe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewMemberPopup extends DialogFragment implements View.OnClickListener {
    private TextInputEditText newMemberNameInputText;
    private TextInputEditText newMemberSchoolIDInputText;
    private Button newMemberButton;
    private Retrofit retrofit;
    private int clubID;
    public static String BASE_URL= Keys.getServerUrl();

    public NewMemberPopup(int clubID)
    {
        this.clubID = clubID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_newmemberpopup, container);
        newMemberButton = (Button)v.findViewById(R.id.newMemberButton);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        newMemberSchoolIDInputText = (TextInputEditText)v.findViewById(R.id.newMemberSchoolIDInputText);

        newMemberButton.setOnClickListener(this);

        return v;

    }

    private Call<ResponseObject> newMember(int clubID, int uSchoolID)
    {
        MemberObject memberObject = new MemberObject(clubID, uSchoolID);
        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.newMember(memberObject);
    }


    @Override
    public void onClick(View view) {
        Call<ResponseObject> call = newMember(clubID, Integer.parseInt(newMemberSchoolIDInputText.getText().toString()));

        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                ResponseObject data = response.body();
                if(data.getResponse() == 1)
                {
                    Log.d("통신결과", "추가완료");
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                t.printStackTrace();

            }
        });
        dismiss();

    }
}
