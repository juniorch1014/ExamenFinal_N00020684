package com.upn.chuquilin.guerra.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    public static Retrofit build() {
        return new Retrofit.Builder()
                .baseUrl("https://64a6aa0e096b3f0fcc803ba1.mockapi.io/") // revisar
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
