package com.example.tnebsafety;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Passvaluesstore {

    @Multipart
    @POST("usersafetyapp")

    Call<ResponseBody> uploadImage(@Part MultipartBody.Part imagePart1,
                                   @Part MultipartBody.Part imagePart2,
                                   @Part("filename") RequestBody filename,
                                   @Part("usname") String usname,
                                   @Part("urvalue") String urvalue,
                                   @Part("circlevalue") String circlevalue,
                                   @Part("sectionvalue") String sectionvalue,
                                   @Part("staffvalue") String staffvalue,
                                   @Part("staffwork") String staffwork,
                                   @Part("timevalue") String timevalue,
                                   @Part("latval") String latval,
                                   @Part("lngval") String lngval,
                                   @Part("accval") String accval,
                                   @Part("userid") String Userid
                                  );
}
