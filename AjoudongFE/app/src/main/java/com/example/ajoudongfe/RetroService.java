package com.example.ajoudongfe;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetroService {
    @POST("/login")
    Call<ResponseModel> login(@Body LoginObject loginObject);

    @GET ("/SERVER_APP/Major_affiliations")
    Call<ResponseModel>getMajorstr(@Query("majorCollege") String college);

    @GET ("/SERVER_APP/club")
    Call<List<ClubModel>>getClubGrid(@Query("clubCategory") String clubCategory);

    @GET ("/SERVER_APP/club")
    Call<List<ClubModel>>getClubGridAll();

}

