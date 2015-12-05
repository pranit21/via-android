package com.fierydevs.customgridview.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fierydevs.customgridview.R;
import com.fierydevs.customgridview.models.Photos;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Pranit on 06-12-2015.
 */
public class PhotoAdapter extends BaseAdapter {
    private Context context;
    private List<Photos> photos;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public PhotoAdapter(Context context, List<Photos> photos) {
        this.context = context;
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.txt_image);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Photos photo = photos.get(position);
        holder.title.setText(photo.getTitle());
        imageLoader.displayImage(photo.getUrl(), holder.imageView);

        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        ImageView imageView;
    }
}
