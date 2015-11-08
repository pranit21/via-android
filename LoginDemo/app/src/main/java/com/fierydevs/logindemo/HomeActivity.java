package com.fierydevs.logindemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private TextView display;
    private Button logout;
    SharedPreferences sharedPreferences;

    public static final int GET_PREFERENCES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // To set the title of the activity
        setTitle("Home");

        // To get general shared preferences
        sharedPreferences = getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE);

        boolean is_logged_in = sharedPreferences.getBoolean("is_logged_in", false);

        if(!is_logged_in) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        display = (TextView) findViewById(R.id.display);
        displayMessage();

        logout = (Button) findViewById(R.id.logoutBtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("is_logged_in", false);
                editor.putString("email", "");
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void displayMessage() {
        String displayText = sharedPreferences.getString("email", "");
        // To get settings shared preferences
        SharedPreferences settingsPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        displayText += "\nName: " + settingsPreferences.getString("username", "")
                + "\nUpdates: " + settingsPreferences.getBoolean("applicationUpdates", false)
                + "\nDownload Type: " + settingsPreferences.getString("downloadType", "1");
        display.setText("Hello " + displayText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivityForResult(intent, GET_PREFERENCES);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_PREFERENCES) {
            displayMessage();
        }
    }
}
