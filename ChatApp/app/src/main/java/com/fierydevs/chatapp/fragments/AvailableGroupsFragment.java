package com.fierydevs.chatapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fierydevs.chatapp.R;
import com.fierydevs.chatapp.models.Group;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pranit on 07-12-2016.
 */

public class AvailableGroupsFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Group, GroupViewHolder> adapter;

    public AvailableGroupsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.available_groups_fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.available_recycler_view);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation()));
        recyclerView.setLayoutManager(linearLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        adapter = new FirebaseRecyclerAdapter<Group, GroupViewHolder>(
                Group.class,
                R.layout.item_group,
                GroupViewHolder.class,
                databaseReference.child("groups")
        ) {
            @Override
            protected void populateViewHolder(GroupViewHolder viewHolder, Group model, int position) {
                viewHolder.groupName.setText(model.getName());
                viewHolder.groupId.setText(model.getGroupId());
            }
        };

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
            }
        });

        recyclerView.setAdapter(adapter);

        return view;
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName, groupId;

        public GroupViewHolder(View itemView) {
            super(itemView);
            groupName = (TextView) itemView.findViewById(R.id.group_name);
            groupId = (TextView) itemView.findViewById(R.id.group_id);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addUserToGroup(groupId.getText().toString());
                }
            });
        }

        private void addUserToGroup(final String groupId) {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            if (firebaseUser != null) {
                databaseReference.child("users").child(firebaseUser.getUid()).child("groups").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    //Group group = dataSnapshot.getValue(Group.class);
                                    Map<String, Object> groups = new HashMap<>();
                                    groups.put(groupId, true);
                                    databaseReference.child("users").child(firebaseUser.getUid()).child("groups").updateChildren(groups);
                                } else {
                                    //Log.e("groupId", groupId);
                                    Map<String, Boolean> groups = new HashMap<>();
                                    groups.put(groupId, true);
                                    databaseReference.child("users").child(firebaseUser.getUid()).child("groups").setValue(groups);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("TAG Error", "loadPost:onCancelled", databaseError.toException());
                            }
                        });
            }
        }
    }
}
