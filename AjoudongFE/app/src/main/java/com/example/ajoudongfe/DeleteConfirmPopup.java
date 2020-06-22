package com.example.ajoudongfe;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeleteConfirmPopup extends DialogFragment{
    private Retrofit retrofit;
    private int clubID;
    private int mode;//0 : 신청자, 1 :기존 회원
    private int uSchoolID;
    public static String BASE_URL= "http://10.0.2.2:8000";
    private Button deleteConfirmButton;
    private Button deleteCancelButton;

    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    static interface Listener {
        void returnData(int result);
    }

    public DeleteConfirmPopup(int clubID, int mode, int uSchoolID)
    {
        this.clubID = clubID;
        this.uSchoolID = uSchoolID;
        this.mode = mode;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_deleteconfirmpopup, container);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        deleteConfirmButton = (Button)v.findViewById(R.id.deleteConfirmButton);
        deleteCancelButton = (Button)v.findViewById(R.id.deleteCancelButton);


        deleteConfirmButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode == 0)//신청자 삭제
                {
                    Call<ResponseObject> call = deleteAppliedUser(uSchoolID, clubID);

                    call.enqueue(new Callback<ResponseObject>() {
                        @Override
                        public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                            ResponseObject data = response.body();
                            if(data.getResponse() != 1)
                            {
                                Log.e("Error", "User was not rejected");
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseObject> call, Throwable t) {
                            Log.e("Connection Error", "Bad Connection");
                        }
                    });

                    dismiss();
                }
                else//기존회원 삭제
                {
                    Call<ResponseObject> call = deleteMember(clubID, uSchoolID);

                    call.enqueue(new Callback<ResponseObject>() {
                        @Override
                        public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                            ResponseObject data = response.body();
                            if(data.getResponse() != 1) {
                                Log.e("Error", "User was not accepted");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseObject> call, Throwable t) {
                            Log.e("Connection Error", "Bad Connection");
                        }
                    });
                    dismiss();
                }
            }
        });

        return v;

    }

    private Call<ResponseObject> deleteMember(int clubID, int uSchoolID)
    {
        MemberObject memberObject = new MemberObject(clubID, uSchoolID);
        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.deleteMember(clubID, uSchoolID);
    }

    private Call<ResponseObject> deleteAppliedUser(int uSchoolID, int clubID) {
        MemberObject memberObject =  new MemberObject(clubID, uSchoolID);
        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.deleteAppliedUser(clubID, uSchoolID);
    }

}
