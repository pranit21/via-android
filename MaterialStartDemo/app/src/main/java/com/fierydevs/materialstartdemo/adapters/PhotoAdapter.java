package com.fierydevs.materialstartdemo.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fierydevs.materialstartdemo.R;
import com.fierydevs.materialstartdemo.models.Photos;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Pranit on 10-01-2016.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private List<Photos> photos;
    private Context context;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    // Unlike ListView, RecyclerView doesn’t come with an onItemClick interface,
    // so we have to implement one in the adapter.
    OnItemClickListener onItemClickListener;

    public PhotoAdapter(Context applicationContext, List<Photos> photos) {
        this.context = applicationContext;
        this.photos = photos;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_image_view, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Photos photo = photos.get(position);
        holder.textView.setText(photo.getTitle());
        imageLoader.displayImage(photo.getUrl(), holder.imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                // Generate background color for TextView using Palette API,
                // when Image loading is completed

                // Synchronous
                // Palette palette = Palette.from(bitmap).generate();

                // Asynchronous
                Palette.from(loadedImage).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        int color = palette.getMutedColor(context.getResources().getColor(android.R.color.black));
                        holder.textView.setBackgroundColor(color);
                    }
                });
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    // Custom ViewHolder class to bind all the Views inside each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public TextView textView;
        public LinearLayout mainHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            textView = (TextView) itemView.findViewById(R.id.text_view);
            mainHolder = (LinearLayout) itemView.findViewById(R.id.main_holder);

            // Set OnClickLister to whole item, so we can handle
            // click event of each element inside that item
            mainHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Handle onItemClick event
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(itemView, getAdapterPosition());
            }
        }
    }

    // Create OnItemClickListener interface that can be used by MainActivity
    // to handle each item click
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // Set OnItemClickListener interface that will communicate activity with this adapter's elements
    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
