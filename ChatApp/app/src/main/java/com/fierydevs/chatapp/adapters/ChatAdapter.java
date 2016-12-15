package com.fierydevs.chatapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fierydevs.chatapp.R;
import com.fierydevs.chatapp.models.Message;

import java.util.List;

/**
 * Created by Pranit on 10-12-2016.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Message> messages;
    private String ownerId;

    private static final int ME = 1;
    private static final int OTHER = 2;

    public ChatAdapter(Context context, List<Message> messages, String ownerId) {
        this.context = context;
        this.messages = messages;
        this.ownerId = ownerId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ME) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_me, parent, false);
            return new ViewHolderMe(view);
        } else {// if (viewType == OTHER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_other, parent, false);
            return new ViewHolderOther(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if (holder.getItemViewType() == ME) {
            ViewHolderMe viewHolderMe = (ViewHolderMe) holder;
            viewHolderMe.chatTextMe.setText(message.getText());
            viewHolderMe.senderNameMe.setText(message.getSenderName());
            //Glide.with(this.context).load(message.getSenderImage()).into(viewHolderMe.profileMe);
        } else if (holder.getItemViewType() == OTHER) {
            ViewHolderOther viewHolderOther = (ViewHolderOther) holder;
            viewHolderOther.chatTextOther.setText(message.getText());
            viewHolderOther.senderNameOther.setText(message.getSenderName());
            //Glide.with(this.context).load(message.getSenderImage()).into(viewHolderOther.profileOther);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolderMe extends RecyclerView.ViewHolder {
        private TextView chatTextMe, senderNameMe;
        private ImageView profileMe;

        public ViewHolderMe(View itemView) {
            super(itemView);

            chatTextMe = (TextView) itemView.findViewById(R.id.chat_text_me);
            senderNameMe = (TextView) itemView.findViewById(R.id.sender_name_me);
            profileMe = (ImageView) itemView.findViewById(R.id.profile_me);
        }
    }

    public class ViewHolderOther extends RecyclerView.ViewHolder {
        private TextView chatTextOther, senderNameOther;
        private ImageView profileOther;

        public ViewHolderOther(View itemView) {
            super(itemView);

            chatTextOther = (TextView) itemView.findViewById(R.id.chat_text_other);
            senderNameOther = (TextView) itemView.findViewById(R.id.sender_name_other);
            profileOther = (ImageView) itemView.findViewById(R.id.profile_other);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message != null && message.getSenderId().equals(this.ownerId)) {
            return ME;
        } else if (message != null){
            return OTHER;
        } else {
            return 0;
        }
    }
}
