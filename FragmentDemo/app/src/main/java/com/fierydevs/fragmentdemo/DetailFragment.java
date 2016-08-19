package com.fierydevs.fragmentdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Pranit on 31-10-2015.
 */
public class DetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rssitem_detail, container, false);
		
		Bundle b = getArguments();
		if(b != null) {
			String str = b.getString(DetailActivity.EXTRA_URL);
			setText(str);
		}

        return view;
    }

    public void setText(String url) {
        TextView textView = (TextView) getView().findViewById(R.id.detailsText);
        textView.setText(url);
    }
}
