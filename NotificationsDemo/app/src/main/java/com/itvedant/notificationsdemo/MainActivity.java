package com.itvedant.notificationsdemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;
    private int notificationID = 100;
    private int numMessages = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startBtn = (Button) findViewById(R.id.start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                displayNotification();
            }
        });

        Button cancelBtn = (Button) findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cancelNotification();
            }
        });

        Button updateBtn = (Button) findViewById(R.id.update);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                updateNotification();
            }
        });

        Button displayProgress = (Button) findViewById(R.id.display_progress);
        displayProgress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                displayProgress();
            }
        });
    }

    protected void displayNotification() {
        Log.i("Start", "notification");

        /* Invoking the default notification service */
        mBuilder = new NotificationCompat.Builder(this);

        // Show controls on lock screen even when user hides sensitive content.
        //mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        mBuilder.setContentTitle("New Message");
        mBuilder.setContentText("You've received new message.");
        mBuilder.setTicker("New Message Alert!");
        mBuilder.setSmallIcon(R.drawable.ic_notifications_black_48dp);
        mBuilder.setAutoCancel(true);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        /* Increase notification number every time a new notification arrives */
        mBuilder.setNumber(++numMessages);

        //mBuilder.setOngoing(true);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, NotificationView.class);

        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(NotificationView.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        // set big picture image
        Bitmap woman = BitmapFactory.decodeResource(getResources(), R.drawable.woman);
        mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(woman));

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        /* notificationID allows you to update the notification later on. */
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

    protected void cancelNotification() {
        Log.i("Cancel", "notification");
        mNotificationManager.cancelAll();//cancel(notificationID);
    }

    protected void updateNotification() {
        Log.i("Update", "notification");

        /* Invoking the default notification service */
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("Updated Message");
        mBuilder.setContentText("You've got updated message.");
        mBuilder.setTicker("Updated Message Alert!");
        mBuilder.setSmallIcon(R.drawable.ic_notifications_black_48dp);
        mBuilder.setAutoCancel(true);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        /* Increase notification number every time a new notification arrives */
        mBuilder.setNumber(++numMessages);

        /* Add Big View Specific Configuration */
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        String[] events = new String[6];
        events[0] = "This is first line....";
        events[1] = "This is second line...";
        events[2] = "This is third line...";
        events[3] = "This is 4th line...";
        events[4] = "This is 5th line...";
        events[5] = "This is 6th line...";

        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("Big Title Details:");
        // Moves events into the big view
        for (String event : events) {
            inboxStyle.addLine(event);
        }
        mBuilder.setStyle(inboxStyle);

        /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(this, NotificationView.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationView.class);

        /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        // Set actions
        Intent intent = new Intent(getApplicationContext(), NotificationView.class);
        PendingIntent pDownload = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
        mBuilder.addAction(R.drawable.ic_file_download_black_48dp, "Download", pDownload);

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        /* Update the existing notification using same notification ID */
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

    protected void displayProgress() {
        /* Invoking the default notification service */
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_file_download_black_48dp);

        // Start a lengthy operation in a background thread
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int incr;
                        // Do the "lengthy" operation 20 times
                        for (incr = 0; incr <= 100; incr += 5) {
                            // Sets the progress indicator to a max value, the
                            // current completion percentage, and "determinate" state
                            mBuilder.setProgress(100, incr, false);
                            mBuilder.setOngoing(true);
                            // Displays the progress bar for the first time.
                            mNotificationManager.notify(0, mBuilder.build());
                            // Sleeps the thread, simulating an operation that takes time
                            try {
                                // Sleep for 500 milliseconds
                                Thread.sleep(5 * 100);
                            } catch (InterruptedException e) {
                                Log.d("Sleep fail", "sleep failure");
                            }
                        }
                        // When the loop is finished, updates the notification
                        mBuilder.setContentText("Download complete")
                                // Removes the progress bar
                                .setProgress(0, 0, false)
                                .setOngoing(false);
                        mNotificationManager.notify(0, mBuilder.build());
                    }
                }
                // Starts the thread by calling the run() method in its Runnable
        ).start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
