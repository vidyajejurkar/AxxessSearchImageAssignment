package com.vidya.axxesssearchimageassignment.remote;


import com.vidya.axxesssearchimageassignment.interfaces.RNService;

public class ApiUtils {

    public static final String BASE_URL = "https://api.imgur.com/3/gallery/search/";

    public static RNService getRNService() {
        return RetrofitClient.getClient(BASE_URL).create(RNService.class);
    }
}
