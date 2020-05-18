package com.example.ajoudongfe;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetroService {
    @POST("/login")
    Call<ResponseModel> login(@Body LoginObject loginObject);

    @GET("/promotions/{pk}/")
    Call<PromotionObject> get_promotions_pk(@Path("pk") int pk);

    @PATCH("/promotions/{pk}/")
    Call<PromotionObject> patch_promotions_pk(@Path("pk") int pk, @Body PromotionObject promo);

    @POST("/activities/")
    Call<ClubActivityObject> post_activities(@Body ClubActivityObject activity);

    @GET("/activities/{pk}/")
    Call<ClubActivityObject> get_activities_pk(@Path("pk") int pk);

    @PATCH("/activities/{pk}/")
    Call<ClubActivityObject> patch_activities_pk(@Path("pk") int pk, @Body ClubActivityObject activity);

    @DELETE("/activities/{pk}/")
    Call<ClubActivityObject> delete_activities_pk(@Path("pk") int pk);
}
