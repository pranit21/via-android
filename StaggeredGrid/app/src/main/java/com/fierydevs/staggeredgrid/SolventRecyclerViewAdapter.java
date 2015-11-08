package com.fierydevs.staggeredgrid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Pranit on 10-09-2015.
 */
public class SolventRecyclerViewAdapter extends RecyclerView.Adapter<SolventViewHolders> {
    private List<ItemObjects> itemList;
    private Context context;

    public SolventRecyclerViewAdapter(Context context, List<ItemObjects> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public SolventViewHolders onCreateViewHolder(ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.solvent_list, null);
        SolventViewHolders rcv = new SolventViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(SolventViewHolders solventViewHolders, int position) {
        solventViewHolders.myName.setText(itemList.get(position).getName());
        solventViewHolders.myPhoto.setImageResource(itemList.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
