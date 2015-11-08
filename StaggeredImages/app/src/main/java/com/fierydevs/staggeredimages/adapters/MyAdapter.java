package com.fierydevs.staggeredimages.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fierydevs.staggeredimages.R;
import com.fierydevs.staggeredimages.models.Photos;
import com.fierydevs.staggeredimages.utils.DynamicHeightImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Pranit on 11-09-2015.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    Context context;
    ArrayList<Photos> photos;
    private final Random mRandom;
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    OnItemClickListener onItemClickListener;

    public MyAdapter(Context context, ArrayList<Photos> photos) {
        this.context = context;
        this.photos = photos;
        this.mRandom = new Random();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        /*final Place place = new PlaceData().placeList().get(position);
        holder.placeName.setText(place.name);*/

        Photos photo = photos.get(position);
        String title = photo.getTitle();

        holder.imgName.setText((title.length() <= 20) ? title : (title.substring(0, 20) + "..."));

        String url = photo.getUrl();
        double positionHeight = getPositionRatio(position);

        holder.imgView.setHeightRatio(positionHeight);

        ImageLoader.getInstance().displayImage(url, holder.imgView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Palette.generateAsync(loadedImage, new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        int bgColor = palette.getMutedColor(context.getResources().getColor(android.R.color.black));
                        holder.nameHolder.setBackgroundColor(bgColor);
                        holder.nameHolder.setAlpha(0.8f);
                    }
                });
            }
        });
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            //Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }

    @Override
    public int getItemCount() {
        return this.photos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout mainHolder;
        public LinearLayout nameHolder;
        public TextView imgName;
        public DynamicHeightImageView imgView;

        public ViewHolder(View itemView) {
            super(itemView);

            mainHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            imgName = (TextView) itemView.findViewById(R.id.imgName);
            nameHolder = (LinearLayout) itemView.findViewById(R.id.nameHolder);
            imgView = (DynamicHeightImageView) itemView.findViewById(R.id.imgView);

            mainHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(itemView, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
