package ru.petapp.test.gifsearcher.view.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableEncoder;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.ByteBufferUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_more_inf);

        viewModel = new ItemViewModel(this, mGifsData);
        binding.setGif(viewModel);
        binding.executePendingBindings();
        Log.d(TAG, mGifsData.getTitle() + mGifsData.getBitlyGifUrl());

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.gifImage.getLayoutParams();
        float width = this.getResources().getDisplayMetrics().widthPixels;
        float ratio = width / binding.getGif().getWidth();

        params.weight = binding.getGif().getWidth() * ratio;
        params.height = (int) (binding.getGif().getHeight() * ratio);

        binding.gifImage.setBackgroundColor(binding.getGif().getColor());


        Glide.with(this)
                .asGif()
                .load(viewModel.getDownsizedURL())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(binding.gifImage);

        Button b = findViewById(R.id.save_gif);
        b.setOnClickListener(v -> {
                    Log.println(Log.DEBUG, TAG, "SAVE");
                    Glide.with(getBaseContext())
                            .asFile()
                            .load(viewModel.getDownsizedURL())
                            .into(new CustomTarget<File>() {
                                @Override
                                public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                    saveImage(resource, viewModel.getTitleGif() + ".gif");
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });
                }
        );


        Button share = findViewById(R.id.share_gif);
        share.setOnClickListener(v -> {
                    Log.println(Log.DEBUG, TAG, "Share");
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/gif");

                    Glide.with(getBaseContext())
                            .asFile()
                            .load(viewModel.getDownsizedURL())
                            .into(new CustomTarget<File>() {
                                @Override
                                public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                    File file = saveImage(resource, viewModel.getTitleGif() + ".gif");
                                    Uri uri = FileProvider.getUriForFile(MoreInfActivity.this,
                                            "ru.petapp.test.gifsearcher.provider", file);
                                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                                    startActivity(Intent.createChooser(intent, "Share Image"));
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });


//                    intent.putExtra(Intent.EXTRA_STREAM, sUri[0]);
                    startActivity(Intent.createChooser(intent, "Share Image"));
                }
        );

    }

    private File saveImage(File file, String imageFileName) {

        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/" + getString(R.string.app_name) + "/";

        final File dir = new File(dirPath);
        Log.println(Log.DEBUG, TAG, "Share " + dir.toString());
        boolean successDirCreated = true;
        if (!dir.exists()) {
            successDirCreated = dir.mkdirs();
        } else if (successDirCreated) {
            File imageFile = new File(dir, imageFileName);
            String savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);

                FileInputStream bis = new FileInputStream(file);
//                ByteBuffer byteBuffer = ByteBufferUtil.fromStream(bis);
//                ByteBuffer byteBuffer1 = ByteBufferUtil.fromFile(file);
//                fOut.write(byteBuffer1.array());
                byte[] buff = new byte[1024];
                int leng;
                while ((leng = bis.read(buff)) > 0) {
                    fOut.write(buff, 0, leng);
                    fOut.flush();
                }


                bis.close();
                fOut.close();
                Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
                return imageFile;
            } catch (Exception e) {
                Toast.makeText(this, "Error while saving image!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this.getApplicationContext(), "Failed to make folder!", Toast.LENGTH_SHORT).show();
        }
        return null;
    }


}
