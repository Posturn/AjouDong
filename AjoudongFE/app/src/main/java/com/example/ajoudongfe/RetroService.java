package com.example.ajoudongfe;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetroService {
    @FormUrlEncoded
    @POST("/login")
    Call<ResponseModel> login(@Field("uID") String ID, @Field("uPW") String PW);

}
