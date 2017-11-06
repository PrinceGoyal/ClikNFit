package com.cliknfit.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Prince on 3/3/2017.
 */

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static Gson gson;
    private static OkHttpClient okHttpClient;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit==null) {
           okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.MINUTES)
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .build();
            gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}
