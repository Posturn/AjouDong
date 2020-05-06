package com.example.ajoudongfe;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetroService {
    @POST("/login")
    Call<ResponseModel> login(@Body LoginObject loginObject);

}
