package com.example.ajoudongfe;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetroService {
    @POST("/login")
    Call<ResponseModel> login(@Body LoginObject loginObject);

    @POST("/sign-up/sameID")
    Call<ResponseModel> checkSameID(@Body CheckID checkID);

    @POST("/sign-up/emailverify")
    Call<ResponseModel> emailVerify(@Header("x-ncp-apigw-timestamp") String timestamp,
                                    @Header("x-ncp-iam-access-key") String accesskey,
                                    @Header("x-ncp-apigw-signature-v2") String signature,
                                    @Body  VerifyObject verifyObject);

}
