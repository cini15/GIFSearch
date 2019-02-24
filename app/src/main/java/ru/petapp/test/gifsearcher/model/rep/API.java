package ru.petapp.test.gifsearcher.model.rep;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class API extends Application {
    private Retrofit retrofit;
    private static ApiGiphi apiGiphi;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit=new Retrofit.Builder()
                .baseUrl("http://api.giphy.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiGiphi=retrofit.create(ApiGiphi.class);
    }

    public static ApiGiphi getApi(){
        return apiGiphi;
    }
}
