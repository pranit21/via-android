package com.fd.fileuploaddemo.filepicker.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.fd.fileuploaddemo.R;
import com.fd.fileuploaddemo.filepicker.PickerManager;
import com.fd.fileuploaddemo.filepicker.models.Photo;
import com.fd.fileuploaddemo.filepicker.views.SmoothCheckBox;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Pranit on 12-09-2016.
 */

public class PhotoGridAdapter extends SelectableAdapter<PhotoGridAdapter.PhotoViewHolder, Photo>{

    private final Context context;
    private int imageSize;

    public final static int ITEM_TYPE_CAMERA = 100;
    public final static int ITEM_TYPE_PHOTO  = 101;
    private View.OnClickListener cameraOnClickListener;

    private SmoothCheckBox previousChecked;

    public PhotoGridAdapter(Context context, ArrayList<Photo> photos, ArrayList<String> selectedPaths)
    {
        super(photos, selectedPaths);
        this.context = context;
        setColumnNumber(context,3);
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_photo_layout, parent, false);

        return new PhotoViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? ITEM_TYPE_CAMERA : ITEM_TYPE_PHOTO;
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {
        if(getItemViewType(position) == ITEM_TYPE_PHOTO) {

            final Photo photo = getItems().get(position-1);
            Log.e("file_path", photo.getPath());
            ImageLoader.getInstance().displayImage("file:///" + photo.getPath(), holder.imageView);

            //FrescoFactory.getLoader().showImage(holder.imageView, Uri.fromFile(new File(photo.getPath())), FrescoFactory.newOption(imageSize, imageSize));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PickerManager.getInstance().getMaxCount() == 1) {
                        // if single file is to be selected then deselect previous file and select new file
                        if (previousChecked != null) {
                            previousChecked.setChecked(false, true);
                        }
                        holder.checkBox.setChecked(true, true);
                        previousChecked = holder.checkBox;
                    } else {
                        // for multiple files
                        if (holder.checkBox.isChecked() || PickerManager.getInstance().shouldAdd()) {
                            holder.checkBox.setChecked(!holder.checkBox.isChecked(), true);
                        }
                    }
                }
            });

            //in some cases, it will prevent unwanted situations
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!holder.checkBox.isChecked() && !PickerManager.getInstance().shouldAdd()) {
                        return;
                    }
                }
            });

            //if true, your checkbox will be selected, else unselected
            holder.checkBox.setChecked(isSelected(photo));

            holder.selectBg.setVisibility(isSelected(photo) ? View.VISIBLE : View.GONE);

            holder.checkBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                    toggleSelection(photo);
                    holder.selectBg.setVisibility(isChecked ? View.VISIBLE : View.GONE);

                    if (isChecked)
                        PickerManager.getInstance().add(photo);
                    else
                        PickerManager.getInstance().remove(photo);
                }
            });
        }
        else
        {
            //ImageLoader.getInstance().displayImage("drawable://" + R.drawable.ic_camera, holder.imageView);
            holder.imageView.setImageResource(R.drawable.ic_camera);
            //FrescoFactory.getLoader().showImage(holder.imageView,R.drawable.ic_camera,null);
            holder.checkBox.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(cameraOnClickListener);
        }
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = widthPixels / columnNum;
    }

    @Override
    public int getItemCount() {
        return getItems().size()+1;
    }

    public void setCameraListener(View.OnClickListener onClickListener)
    {
        this.cameraOnClickListener = onClickListener;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        SmoothCheckBox checkBox;

        ImageView imageView;

        View selectBg;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            checkBox = (SmoothCheckBox) itemView.findViewById(R.id.checkbox);
            imageView = (ImageView) itemView.findViewById(R.id.iv_photo);
            selectBg = itemView.findViewById(R.id.transparent_bg);
        }
    }
}
