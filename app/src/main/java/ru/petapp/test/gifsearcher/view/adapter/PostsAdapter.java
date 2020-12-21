package ru.petapp.test.gifsearcher.view.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.petapp.test.gifsearcher.databinding.PostItemBinding;
import ru.petapp.test.gifsearcher.model.giphy.GIFSData;
import ru.petapp.test.gifsearcher.viewModel.ItemViewModel;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ItemViewHolder> {

    private static final String TAG = PostsAdapter.class.getSimpleName();

    private List<GIFSData> gifs;
    private final Context context;


    public void setGifs(List<GIFSData> gifs) {
        this.gifs = gifs;
    }

    public PostsAdapter(Context context, List<GIFSData> GIFSData) {
        this.gifs = GIFSData;
        this.context = context;
    }

    public void addAll(List<GIFSData> gifs) {
        this.gifs.addAll(gifs);
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        PostItemBinding binding = PostItemBinding.inflate(layoutInflater, viewGroup, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder viewHolder, int i) {
        PostItemBinding binding = viewHolder.binding;

        ItemViewModel viewModel = new ItemViewModel(context, gifs.get(i));
        binding.setGif(viewModel);

        viewHolder.bind(binding);
        binding.executePendingBindings();

        Glide.with(context)
                    .asGif()
                    .load(binding.getGif().getPreviewUri())
                    .into(binding.gifImage).clearOnDetach();

    }

    @Override
    public int getItemCount() {
        if (gifs == null)
            return 0;
        return gifs.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        PostItemBinding binding;

        ItemViewHolder(PostItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(PostItemBinding binding) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.gifImage.getLayoutParams();
            float width = context.getResources().getDisplayMetrics().widthPixels;
            float ratio = width / binding.getGif().getWidth() / 2;

            params.weight = binding.getGif().getWidth() * ratio;
            params.height = (int) (binding.getGif().getHeight() * ratio);

            binding.gifImage.setBackgroundColor(binding.getGif().getColor());

        }
    }
}
