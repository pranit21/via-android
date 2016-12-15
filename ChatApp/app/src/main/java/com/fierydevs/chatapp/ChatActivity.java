package com.fierydevs.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fierydevs.chatapp.adapters.ChatAdapter;
import com.fierydevs.chatapp.models.Message;
import com.fierydevs.chatapp.utils.IntentConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private List<Message> messages;
    private ChatAdapter adapter;

    private EditText enterText;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent != null) {
            final String groupId = intent.getStringExtra(IntentConstants.GROUP_ID);
            String groupName = intent.getStringExtra(IntentConstants.GROUP_NAME);
            setTitle(groupName);

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference();

            recyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
            layoutManager = new LinearLayoutManager(this);
            layoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(layoutManager);

            messages = new ArrayList<>();
            adapter = new ChatAdapter(this, messages, firebaseUser.getUid());

            recyclerView.setAdapter(adapter);

            enterText = (EditText) findViewById(R.id.enter_text);
            send = (Button) findViewById(R.id.send);

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String key = databaseReference.child("messages").push().getKey();

                    Message message = new Message();
                    message.setMessageId(key);
                    message.setGroupId(groupId);
                    message.setSenderId(firebaseUser.getUid());
                    message.setSenderName(firebaseUser.getDisplayName());
                    message.setText(enterText.getText().toString());

                    databaseReference.child("messages").child(groupId).child(key).setValue(message);

                    enterText.setText("");
                }
            });

            getMessages(groupId);
        } else {
            finish();
            return;
        }
    }

    private void getMessages(String groupId) {
        Log.e("groupId", groupId);
        databaseReference.child("messages").child(groupId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getKey() != null) {
                            Message message = dataSnapshot.getValue(Message.class);
                            Log.e("onChildAdded", dataSnapshot.getKey() + " : " + message.toString());
                            messages.add(message);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getKey() != null) {
                            Log.e("onChildChanged", dataSnapshot.getKey());
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getKey() != null) {
                            Log.e("onChildRemoved", dataSnapshot.getKey());
                        }
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getKey() != null) {
                            Log.e("onChildMoved", dataSnapshot.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("onCancelled", databaseError.getMessage());
                    }
                });
    }
}
