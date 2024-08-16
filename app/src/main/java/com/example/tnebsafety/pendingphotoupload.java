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
public interface pendingphotoupload {
    @Multipart
    @POST("usersafetyapppending")

    Call<ResponseBody> uploadpendingImage(@Part MultipartBody.Part image1,
                                   @Part("filename") RequestBody filename,
                                   @Part("filefullname") String filefullname);
}
