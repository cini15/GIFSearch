package ru.petapp.test.gifsearcher.view.ui;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.petapp.test.gifsearcher.R;
import ru.petapp.test.gifsearcher.model.giphy.DataContainer;
import ru.petapp.test.gifsearcher.model.giphy.GIFSData;
import ru.petapp.test.gifsearcher.model.rep.GiphyRepoImpl;
import ru.petapp.test.gifsearcher.model.rep.IGiphyRepo;
import ru.petapp.test.gifsearcher.view.adapter.PostsAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int LAYOUT_ACTIVITY = R.layout.activity_main;
    RecyclerView recyclerView;
    LinearLayout mProgressBar;

    private List<GIFSData> gifs;
    private IGiphyRepo api;
    private boolean isLoading=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT_ACTIVITY);
        setSupportActionBar(findViewById(R.id.toolbar));
        mProgressBar = findViewById(R.id.progress_indicator);

        gifs = new ArrayList<>();

        Display display= getWindowManager().getDefaultDisplay();
        Point point= new Point();
        display.getSize(point);

        recyclerView = findViewById(R.id.posts_recycle_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        PostsAdapter adapter = new PostsAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);
//        recyclerView.setItemViewCacheSize(10);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        api = new GiphyRepoImpl();

        loadGifs();
         recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
             @Override
             public void onScrolled(@NonNull RecyclerView recyclerView1, int dx, int dy) {
                 super.onScrolled(recyclerView1, dx, dy);

                 int pastVisibleItems=0;
                 int visibleItemCount = layoutManager.getChildCount();//смотрим сколько элементов на экране
                 final int totalItemCount = layoutManager.getItemCount();//сколько всего элементов
                 int[] firstVisibleItems =null;
                 firstVisibleItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItems);//какая позиция первого элемента

                 if(firstVisibleItems != null && firstVisibleItems.length > 0) {
                     pastVisibleItems = firstVisibleItems[0];
                 }

                 if (!isLoading){
                 if ((visibleItemCount+pastVisibleItems) >= totalItemCount){
                     isLoading=true;
                    loadGifs();
                 }
             }
             }
         });

    }

    private void loadGifs() {
        PostsAdapter adapter= (PostsAdapter) recyclerView.getAdapter();

        api.getMore(adapter.getItemCount())
                .enqueue(new Callback<DataContainer>() {
                    @Override
                    public void onResponse(Call<DataContainer> call, Response<DataContainer> response) {
                        if (response.body() != null) {
//                            gifs.addAll(response.body().getGIFSDataList());
                            adapter.addAll(response.body().getGIFSDataList());
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                        isLoading=false;
                        Log.d(TAG, "count of Gifs: " + gifs.size() + " count of load: " + response.body().getGIFSDataList().size());
                    }

                    @Override
                    public void onFailure(Call<DataContainer> call, Throwable t) {
                        Log.e(TAG, "Error to load Gifs info" + t.toString());
                        Toast.makeText(MainActivity.this, "ERROR LOAD", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
