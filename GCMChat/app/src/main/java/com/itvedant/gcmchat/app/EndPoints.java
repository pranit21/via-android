package com.itvedant.gcmchat.app;

/**
 * Created by Pranit on 10-04-2016.
 */
public class EndPoints {
    // localhost url -
    public static final String BASE_URL = "http://192.168.1.104/gcm_chat/v1";
    public static final String LOGIN = BASE_URL + "/user/login";
    public static final String USER = BASE_URL + "/user/_ID_";
    public static final String CHAT_ROOMS = BASE_URL + "/chat_rooms";
    public static final String CHAT_THREAD = BASE_URL + "/chat_rooms/_ID_";
    public static final String CHAT_ROOM_MESSAGE = BASE_URL + "/chat_rooms/_ID_/message";
}
