package com.example.ajoudongfe;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeviceToken
{
    private String UserDeviceToken;
    private String userID;
    private Retrofit retrofit;
    final String BASE_URL = "http://10.0.2.2:8000";
    private RetroService retroService;

    protected void setRetrofit()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retroService = retrofit.create(RetroService.class);
    }

    public String gettingUserDeviceToken()
    {
        setRetrofit();
        final String[] token = new String[1];
        UserDeviceToken = "";
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token[0] = task.getResult().getToken();
                        Log.d("TAG", token[0]);
                    }
                });
        Call<ResponseObject> call = retroService.getIDbyToken(token[0]);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                ResponseObject data = response.body();
                if(data.getResponse() == 1)//해당 디바이스가 등록되어있을때
                {
                    userID = data.getMessage();
                }
                else if(data.getResponse() < 0)//해당 디바이스가 등록되어있지않을때
                {
                    //TODO 예외처리 논의 필요
                }
                else
                {
                    //TODO 예외처리 논의 필요2
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return userID;
    }


}
