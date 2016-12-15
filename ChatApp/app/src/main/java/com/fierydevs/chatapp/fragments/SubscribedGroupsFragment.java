package com.fierydevs.chatapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fierydevs.chatapp.ChatActivity;
import com.fierydevs.chatapp.R;
import com.fierydevs.chatapp.adapters.SubscribedGroupsAdapter;
import com.fierydevs.chatapp.models.Group;
import com.fierydevs.chatapp.utils.IntentConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pranit on 07-12-2016.
 */

public class SubscribedGroupsFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private List<Group> groups;
    private SubscribedGroupsAdapter adapter;

    public SubscribedGroupsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subscribed_groups_fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.subscribed_recycler_view);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation()));
        recyclerView.setLayoutManager(linearLayoutManager);

        groups = new ArrayList<>();
        adapter = new SubscribedGroupsAdapter(getContext(), groups);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SubscribedGroupsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView groupId = (TextView) view.findViewById(R.id.group_id);
                TextView groupName = (TextView) view.findViewById(R.id.group_name);
                //Toast.makeText(getContext(), "group id: " + groupId.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra(IntentConstants.GROUP_ID, groupId.getText().toString());
                intent.putExtra(IntentConstants.GROUP_NAME, groupName.getText().toString());
                startActivity(intent);
            }
        });

        getGroups();

        return view;
    }

    private void getGroups() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference groupRef = databaseReference.child("groups");

        if(firebaseUser != null) {
            databaseReference.child("users").child(firebaseUser.getUid()).child("groups")
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            groupRef.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Group group = dataSnapshot.getValue(Group.class);
                                    groups.add(group);
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }
}
