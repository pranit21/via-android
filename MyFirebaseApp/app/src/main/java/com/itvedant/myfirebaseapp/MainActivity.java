package com.itvedant.myfirebaseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseAnalytics firebaseAnalytics;
    private Button logFruit, logToken, subscribeToTopic;
    private String[] fruits = { "Apple", "Banana", "Mango", "Orange" };
    private Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        logFruit = (Button) findViewById(R.id.log_fruit);
        logToken = (Button) findViewById(R.id.log_token);
        subscribeToTopic = (Button) findViewById(R.id.subscribe_to_topic);

        logFruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int random = rand.nextInt(4);

                Bundle bundle = new Bundle();
                bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, random);
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, fruits[random]);

                // Logs an app event
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                // Sets the user Id
                firebaseAnalytics.setUserId(String.valueOf(random));

                // Sets the user property to a given value
                firebaseAnalytics.setUserProperty("Fruit", fruits[random]);
            }
        });

        // Handle data when notification is tapped from notification drawer
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.e(TAG, "Key: " + key + " Value: " + value);
            }
        }

        logToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.e(TAG, "Token: " + token);
            }
        });

        subscribeToTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseMessaging.getInstance().subscribeToTopic("news");
                Log.e(TAG, "Subscribed to topic news");

                Toast.makeText(MainActivity.this, "Subscribed to topic news", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
