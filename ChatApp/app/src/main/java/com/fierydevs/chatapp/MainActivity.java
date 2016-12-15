package com.fierydevs.chatapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.fierydevs.chatapp.fragments.AvailableGroupsFragment;
import com.fierydevs.chatapp.fragments.SubscribedGroupsFragment;
import com.fierydevs.chatapp.models.Group;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseAnalytics firebaseAnalytics;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    String name = firebaseUser.getDisplayName();
                    String email = firebaseUser.getEmail();
                    Uri photoUrl = firebaseUser.getPhotoUrl();
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    return;
                }
            }
        };

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SubscribedGroupsFragment(), "Subscribed");
        adapter.addFragment(new AvailableGroupsFragment(), "Available");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                signOut();
                return true;
            case R.id.create_group:
                createGroup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Group");
        final EditText groupName = new EditText(this);
        groupName.setInputType(InputType.TYPE_CLASS_TEXT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        groupName.setLayoutParams(params);
        builder.setView(groupName);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String gName = groupName.getText().toString();
                Log.e("TAG Group Name", gName);
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                databaseReference = FirebaseDatabase.getInstance().getReference();

                String key = databaseReference.child("groups").push().getKey();

                Group group = new Group();
                group.setGroupId(key);
                group.setName(gName);
                group.setDesc(gName);
                Map<String, Object> groupValues = group.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/groups/" + key, groupValues);
                childUpdates.put("/users/" + firebaseUser.getUid() + "/groups/" + key, true);

                databaseReference.updateChildren(childUpdates);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create().show();
    }

    private void signOut() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            firebaseAuth.signOut();
        }

        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
        return;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitles.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }
}
