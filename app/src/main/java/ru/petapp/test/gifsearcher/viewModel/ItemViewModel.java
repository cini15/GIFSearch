package ru.petapp.test.gifsearcher.viewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import ru.petapp.test.gifsearcher.R;
import ru.petapp.test.gifsearcher.model.giphy.GIFSData;
import ru.petapp.test.gifsearcher.view.ui.MoreInfActivity;

public class ItemViewModel extends ViewModel {
    private final GIFSData mPreviewGif;
    private final Context context;
    private int color=0;
    private static int num;

    public ItemViewModel(Context context, GIFSData gif) {
        this.context = context;
        this.mPreviewGif = gif;

    }

    private int setColor() {
        int[] colorArr = {
                context.getResources().getColor(R.color.yellow),
                context.getResources().getColor(R.color.blue),
                context.getResources().getColor(R.color.brown),
                context.getResources().getColor(R.color.green),
                context.getResources().getColor(R.color.ping),
                context.getResources().getColor(R.color.red),
        };
        if (num >= colorArr.length) {
            num = 0;
        }
       color=colorArr[num++];
        return color;
    }

    public String getDownsizedURL() {

        return mPreviewGif.getImages().getDownsized().getUrl();
    }

    public String getPreviewUri() {
        return this.mPreviewGif.getImages().getPreviewGif().getUrl();
    }

    public String getTitleGif() {
        return this.mPreviewGif.getTitle();
    }


    public View.OnClickListener onClickItem() {
        return v -> {
            Intent intent = new Intent(context, MoreInfActivity.class);
            intent.putExtra(MoreInfActivity.EXTRA_GIF, mPreviewGif.getId());
            context.startActivity(intent);
        };
    }

    public int getWidth() {
        return Integer.parseInt(this.mPreviewGif.getImages().getPreviewGif().getWidth()!=null ?
                this.mPreviewGif.getImages().getPreviewGif().getWidth(): "100" );
    }


    public int getHeight() {
        return Integer.parseInt(this.mPreviewGif.getImages().getPreviewGif().getHeight()!= null?
                this.mPreviewGif.getImages().getPreviewGif().getHeight(): "100");
    }

    public int getColor() {
        return color !=0 ? color: setColor();
    }

}
