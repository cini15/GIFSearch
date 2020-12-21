package ru.petapp.test.gifsearcher.view.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.petapp.test.gifsearcher.R;
import ru.petapp.test.gifsearcher.databinding.ActivityMoreInfBinding;
import ru.petapp.test.gifsearcher.model.giphy.DataContainer;
import ru.petapp.test.gifsearcher.model.giphy.GIFSData;
import ru.petapp.test.gifsearcher.model.rep.GiphyRepoImpl;
import ru.petapp.test.gifsearcher.model.rep.IGiphyRepo;
import ru.petapp.test.gifsearcher.viewModel.ItemViewModel;

public class MoreInfActivity extends AppCompatActivity {
    private static final String TAG = MoreInfActivity.class.getSimpleName();
    public static final String EXTRA_GIF = "ru.petapp.test.gifsearcher.view.ui.MoreInfActivity.EXTRA_GIF";

    private String mGifID;
    private GIFSData mGifsData;
    ItemViewModel viewModel;
    ActivityMoreInfBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_inf);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mGifID = getIntent().getExtras().getString(EXTRA_GIF);
        Log.d(TAG, mGifID);
        if (mGifID == null) {
            Log.d(TAG, "ERROR");
        } else {
            loadGif();
        }


    }

    private void loadGif() {
        IGiphyRepo repo = new GiphyRepoImpl();
        repo.getGifById(mGifID).enqueue(new Callback<DataContainer>() {
            @Override
            public void onResponse(Call<DataContainer> call, Response<DataContainer> response) {
                if (response.body() != null) {
                    mGifsData = response.body().getGIFSDataList().get(0);
                    initBinding();

                    Log.d(TAG, "load Gif: " + mGifsData.getTitle());
                }

            }

            @Override
            public void onFailure(Call<DataContainer> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void initBinding() {

        binding= DataBindingUtil.setContentView(this,R.layout.activity_more_inf);

        viewModel = new ItemViewModel(this, mGifsData);
        binding.setGif(viewModel);
        binding.executePendingBindings();
        Log.d(TAG,mGifsData.getTitle()+mGifsData.getBitlyGifUrl());

        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) binding.gifImage.getLayoutParams();
        float width = this.getResources().getDisplayMetrics().widthPixels;
        float ratio = width / binding.getGif().getWidth() ;

        params.weight = binding.getGif().getWidth() * ratio;
        params.height = (int) (binding.getGif().getHeight() * ratio);

        binding.gifImage.setBackgroundColor(binding.getGif().getColor());


        Glide.with(this)
                .asGif()
                .load(viewModel.getDownsizedURL())
                .into(binding.gifImage);

        Button b = findViewById(R.id.save_gif);
        b.setOnClickListener(v -> {
            Log.println(Log.DEBUG,TAG,"SAVE");
        });

    }



}
