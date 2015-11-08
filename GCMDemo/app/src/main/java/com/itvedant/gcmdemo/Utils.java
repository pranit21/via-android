package com.itvedant.gcmdemo;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Pranit on 11-07-2015.
 */
public class Utils {
    // give your server registration url here
    static final String SERVER_URL = "http://192.168.1.104/gcm_server/register.php";

    // Google project id
    static final String SENDER_ID = "stellar-mariner-100308";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "VIA GCM";

    static final String DISPLAY_MESSAGE_ACTION =
            "com.itvedant.gcmdemo.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
