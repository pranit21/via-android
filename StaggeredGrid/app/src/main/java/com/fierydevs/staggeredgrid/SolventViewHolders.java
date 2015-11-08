package com.fierydevs.staggeredgrid;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Pranit on 10-09-2015.
 */
public class SolventViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView myName;
    public ImageView myPhoto;

    public SolventViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        myName = (TextView) itemView.findViewById(R.id.my_name);
        myPhoto = (ImageView) itemView.findViewById(R.id.my_photo);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), "Clicked Position: " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}
