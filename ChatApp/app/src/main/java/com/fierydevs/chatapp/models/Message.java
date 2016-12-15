package com.fierydevs.chatapp.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pranit on 10-12-2016.
 */

@IgnoreExtraProperties
public class Message {
    String messageId, text, senderId, senderName, groupId;

    public Message() {
    }

    public Message(String messageId, String text, String senderId, String senderName, String groupId) {
        this.messageId = messageId;
        this.text = text;
        this.senderId = senderId;
        this.senderName = senderName;
        this.groupId = groupId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("messageId", messageId);
        result.put("text", text);
        result.put("senderId", senderId);
        result.put("senderName", senderName);
        result.put("groupId", groupId);

        return result;
    }

    @Override
    public String toString() {
        return "text: " + text;
    }
}
