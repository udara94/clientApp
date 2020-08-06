package com.example.clientapp.model.rest;

import com.example.clientapp.BuildConfig;
import com.example.clientapp.common.constants.DomainConstants;
import com.example.clientapp.model.rest.exception.RxErrorHandlingCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JobAPIService {
    private JobAPI jobAPI;

    public JobAPIService() {
        String url = DomainConstants.SERVER_URL;
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(url)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .build();
        jobAPI = restAdapter.create(JobAPI.class);
    }

    private OkHttpClient getClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(); // Log Requests and Responses
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.addInterceptor(logging);
        }

        return client.build();
    }

    public JobAPI getApi(){
        return  jobAPI;
    }
}
