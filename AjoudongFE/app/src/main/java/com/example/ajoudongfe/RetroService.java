package com.example.ajoudongfe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.DELETE;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RetroService {
    @POST("/login")
    Call<ResponseObject> login(@Body LoginObject loginObject);

    @POST("/sign-up")
    Call<ResponseObject> signup(@Body SignupObject signupObject);
  
    @POST("/sign-up/sameID")
    Call<ResponseObject> checkSameID(@Body CheckIDObject checkIDObject);

    @GET("management/memberlist/{clubID}")
    Call<UserListObject> getMemberList(@Path("clubID") int clubID);

    @GET("management/applieduserlist/{clubID}")
    Call<UserListObject> getAppliedUserList(@Path("clubID") int clubID);

    @POST("management/applieduserlist/newapplieduser/{clubID}/{uSchoolID}")
    Call<ResponseObject> newAppliedUser(@Path("clubID") int clubID, @Path("uSchoolID") int uSchoolID);

    @POST("management/applieduserlist/deleteapplieduser/{clubID}/{uSchoolID}")
    Call<ResponseObject> deleteAppliedUser(@Path("clubID") int clubID, @Path("uSchoolID") int uSchoolID);

    @POST("management/memberlist/newmember")
    Call<ResponseObject> newMember(@Body MemberObject newMemberObject);

    @POST("management/memberlist/deletemember/{clubID}/{uSchoolID}")
    Call<ResponseObject> deleteMember(@Path("clubID") int clubID, @Path("uSchoolID") int uSchoolID);

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

    @GET("/activities/grid/{clubID}/")
    Call<List<ClubActivityGridObject>> get_activitiesGrid(@Path("clubID") int clubID);
  
    @GET ("/SERVER_APP/Major_affiliations")
    Call<ResponseObject>getMajorstr(@Query("majorCollege") String college);

    @GET ("/clubs/{clubID}")
    Call<ClubObject>getClubGrid(@Path("clubID") int clubID);

    @GET ("/clublist/{club}/{category}/{sort}/")
    Call<List<ClubObject>>getClubGridAll(@Path("club") int club, @Path("category") String category, @Path("sort") int sort);

    @GET ("/clubsearch/{club}/{category}/{sort}/{search}")
    Call<List<ClubObject>>getClubGridSearch(@Path("club") int club, @Path("category") String category, @Path("sort") int sort, @Path("search") String search);


    @POST("/clubfiltering/")
    Call<List<ClubObject>> getClubGridFilter(@Body ClubFilterObject clubFilterObject);

    @GET ("/bookmarksearch/{schoolID}/")
    Call<List<BookmarkObject>>getBookmark(@Path("schoolID") int schoolID);

    @POST ("/postbookmark/")
    Call<BookmarkObject>postBookmark(@Body BookmarkObject bookmarkObject);

    @POST("/deletebookmark/{clubID}/{schoolID}")
    Call<BookmarkObject>deleteBookmark(@Path("clubID") int clubID, @Path("schoolID") int schoolID);

    @GET("/userInformation/{uschoolID}/")
    Call<UserObject>getUserInformation(@Path("uschoolID") int uschoolID);

    @POST("/clubApply/")
    Call<ResponseObject> clubApply(@Body ApplyObject applyObject);
}

