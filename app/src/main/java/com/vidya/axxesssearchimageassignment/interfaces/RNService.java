package com.vidya.axxesssearchimageassignment.interfaces;
import com.vidya.axxesssearchimageassignment.model.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface RNService {

//    @GET("1?q=vanilla")
//    Call<Example> getData();

    @GET("1?q=vanilla")
    Call<Example> getData(@Header("Authorization") String token);
}
