package ru.petapp.test.gifsearcher.model.rep;

import retrofit2.Call;
import ru.petapp.test.gifsearcher.BuildConfig;
import ru.petapp.test.gifsearcher.model.giphy.DataContainer;


public  class GiphyRepoImpl implements IGiphyRepo {

    private static String API_KEY= BuildConfig.API_KEY;

    @Override
    public Call<DataContainer> getMore(int offset) {
        return API.getApi().getTrending(API_KEY,offset);
    }

    @Override
    public Call<DataContainer> getFind(String find,int offset) {
        return API.getApi().getFind(API_KEY,find,offset);
    }

    @Override
    public Call<DataContainer> getGifById(String id) {
        return API.getApi().getGifById(id,API_KEY);
    }

}
