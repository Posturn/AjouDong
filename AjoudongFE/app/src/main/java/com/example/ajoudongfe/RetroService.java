package com.example.ajoudongfe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RetroService {
    @POST("/login")
    Call<ResponseObject> login(@Body LoginObject loginObject);
  
    @POST("/sign-up/sameID")
    Call<ResponseObject> checkSameID(@Body CheckID checkID);

    @POST("/sign-up/emailverify")
    Call<ResponseObject> emailVerify(@Header("x-ncp-apigw-timestamp") String timestamp,
                                     @Header("x-ncp-iam-access-key") String accesskey,
                                     @Header("x-ncp-apigw-signature-v2") String signature,
                                     @Body  VerifyObject verifyObject);

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
  
    @GET ("/SERVER_APP/Major_affiliations")
    Call<ResponseObject>getMajorstr(@Query("majorCollege") String college);

    @GET ("/SERVER_APP/club")
    Call<List<ClubObject>>getClubGrid(@Query("clubCategory") String clubCategory);

    @GET ("/SERVER_APP/club")
    Call<List<ClubObject>>getClubGridAll();

}

