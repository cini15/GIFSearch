package ru.petapp.test.gifsearcher.model.rep;

import retrofit2.Call;
import ru.petapp.test.gifsearcher.model.giphy.DataContainer;


public interface IGiphyRepo {
    Call<DataContainer> getMore(int offset);
    Call<DataContainer> getFind(String find,int offset);
    Call<DataContainer> getGifById(String id);
}
