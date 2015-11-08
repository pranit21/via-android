package com.itvedant.contactlist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.BitSet;
import java.util.List;

/**
 * Created by Pranit on 04-09-2015.
 */
public class CustomAdapter extends BaseAdapter {
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private List<MyContacts> contacts;

    public CustomAdapter(Activity mActivity, List<MyContacts> contacts) {
        this.mActivity = mActivity;
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(mLayoutInflater == null)
            mLayoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
            convertView = mLayoutInflater.inflate(R.layout.list_row, null);

        TextView contactName = (TextView) convertView.findViewById(R.id.contact_name);
        TextView contactMobile = (TextView) convertView.findViewById(R.id.contact_mobile);
        ImageView contactPhoto = (ImageView) convertView.findViewById(R.id.contact_photo);

        MyContacts c = contacts.get(position);
        contactName.setText(c.getContactName());
        contactMobile.setText(c.getContactMobile());

        InputStream u = c.getContactPhoto();
        if (u != null) {
            Bitmap photo = BitmapFactory.decodeStream(u);
            contactPhoto.setImageBitmap(photo);
        } else {
            contactPhoto.setImageResource(R.drawable.call);
        }

        return convertView;
    }
}
