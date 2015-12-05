package com.fierydevs.customlistview.adapters;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fierydevs.customlistview.AppController;
import com.fierydevs.customlistview.R;
import com.fierydevs.customlistview.models.FeedItem;

import java.util.List;

/**
 * Created by Pranit on 05-12-2015.
 */
public class FeedListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<FeedItem> items;
    private ImageLoader imageLoader;

    public FeedListAdapter(Context context, List<FeedItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.feed_item, null);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView profile_name = (TextView) convertView.findViewById(R.id.profile_name);
        TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
        TextView statusMsg = (TextView) convertView.findViewById(R.id.txt_status_msg);
        TextView url = (TextView) convertView.findViewById(R.id.txt_url);
        NetworkImageView profilePic = (NetworkImageView) convertView.findViewById(R.id.profile_pic);
        NetworkImageView feedImage = (NetworkImageView) convertView.findViewById(R.id.feed_image);

        FeedItem item = items.get(position);

        profile_name.setText(item.getName());

        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(item.getTimestamp()), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        timestamp.setText(timeAgo);

        if (!TextUtils.isEmpty(item.getStatus())) {
            statusMsg.setText(item.getStatus());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }

        if (item.getUrl() != null) {
            url.setText(Html.fromHtml("<a href='" + item.getUrl() + "'>" + item.getUrl() + "</a>"));
            //url.setMovementMethod(LinkMovementMethod.getInstance());
            url.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from view
            url.setVisibility(View.GONE);
        }

        profilePic.setImageUrl(item.getProfilePic(), imageLoader);

        if (item.getFeedImage() != null) {
            feedImage.setImageUrl(item.getFeedImage(), imageLoader);
            feedImage.setVisibility(View.VISIBLE);
        } else {
            feedImage.setVisibility(View.GONE);
        }

        return convertView;
    }
}
