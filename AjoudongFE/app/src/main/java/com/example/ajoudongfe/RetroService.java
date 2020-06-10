package com.example.ajoudongfe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.DELETE;
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

    @GET("application/result/{uSchoolID}")
    Call<ApplicationListObject> getApplicationResult(@Path("uSchoolID") int uSchoolID);

    @POST("application/result/{uSchoolID}/deleteApplication/{clubID}")
    Call<ResponseObject> deleteApplication(@Path("uSchoolID") int uSchoolID, @Path("clubID") int clubID);

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

    @GET("management/membercsv/{clubID}")
    Call<FileObject> getMemberCSV(@Path("clubID") int clubID);

    @POST("/sign-up/emailverify")
    Call<ResponseObject> emailVerify(@Body  VerifyInfoObject verifyInfoObject);

    @GET("manageraccount/{pk}/")
    Call<ManagerAccountObject> get_manageraccount_pk(@Path("pk") String pk);

    @PATCH("manageraccount/{pk}/")
    Call<ManagerAccountObject> patch_manageraccount_pk(@Path("pk") String pk, @Body ManagerAccountObject manager);

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

    @GET ("/clubsearch/{club}/{category}/{sort}/{search}/")
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

    @PATCH("/userInformation/{uschoolID}/")
    Call<UserObject>patchUserInformation(@Path("uschoolID") int uschoolID, @Body UserObject userObject);

    @GET("/nrecruitclubs/")
    Call<List<Integer>> getnRecruitClub();

    @POST("/clubApply/")
    Call<ResponseObject> clubApply(@Body ApplyObject applyObject);

    @GET("/managerfilter/{clubID}/")
    Call<List<ClubTagObject>>getClubTagObject(@Path("clubID") int clubID);

    @POST("/postfilter/")
    Call<ClubTagObject>postClubTagObject(@Body ClubTagObject tagObject);

    @DELETE("/deletefilter/{clubID}/")
    Call<ClubTagObject>deleteClubTagObject(@Path("clubID") int clubID);

    @PATCH("/clubs/{clubID}/")
    Call<ClubDetailObject>patchClubDetailObject(@Path("clubID") int clubID, @Body ClubDetailObject clubDetailObject);

    @GET ("/statisticSearch/{clubID}/")
    Call<StatisticObject>getClubStatistic(@Path("clubID") int clubID);

    @GET("/clubquestion/{club}/")
    Call<QuestionObject> getClubQuestion(@Path("club") int club);

    @GET("/clubmembers/{uSchoolID}/")
    Call<List<ClubMemberGridObject>> getClubMember(@Path("uSchoolID") int uSchoolID);
}

