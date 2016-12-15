package com.fierydevs.chatapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fierydevs.chatapp.R;
import com.fierydevs.chatapp.models.Group;

import java.util.List;

/**
 * Created by Pranit on 08-12-2016.
 */

public class SubscribedGroupsAdapter extends RecyclerView.Adapter<SubscribedGroupsAdapter.GroupViewHolder> {
    private Context context;
    private List<Group> groups;
    OnItemClickListener onItemClickListener;

    public SubscribedGroupsAdapter(Context context, List<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);

        return new GroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        Group group = groups.get(position);
        holder.groupName.setText(group.getName());
        holder.groupId.setText(group.getGroupId());
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName, groupId;

        public GroupViewHolder(View itemView) {
            super(itemView);
            groupName = (TextView) itemView.findViewById(R.id.group_name);
            groupId = (TextView) itemView.findViewById(R.id.group_id);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
