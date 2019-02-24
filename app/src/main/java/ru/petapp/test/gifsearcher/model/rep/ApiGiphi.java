package ru.petapp.test.gifsearcher.model.rep;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.petapp.test.gifsearcher.model.giphy.DataContainer;


public interface ApiGiphi {
    @GET("gifs/trending")
    Call<DataContainer> getTrending(@Query("api_key") String apiKey,@Query("offset")Integer offset);
    @GET("gifs/search")
    Call<DataContainer> getFind(@Query("api_key") String apiKey,@Query("q") String word,@Query("offset")Integer offset);
    @GET("gifs")
    Call<DataContainer> getGifById(@Query("ids") String id,@Query("api_key")String apiKey );
}
