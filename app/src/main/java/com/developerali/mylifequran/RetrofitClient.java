package com.developerali.mylifequran;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BaseUrl = "https://api.alquran.cloud";
    private static RetrofitClient clientObject;
    private static Retrofit retrofit;


    public static synchronized RetrofitClient getInstance(){
        if (clientObject == null){
            clientObject = new RetrofitClient();
        }

        return clientObject;
    }

    public apiset getapi(){
        return retrofit.create(apiset.class);
    }

    RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


}
