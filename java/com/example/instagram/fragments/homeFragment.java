package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagram.R;

import com.example.instagram.adapter.postAdapter;
import com.example.instagram.model.post;
import com.example.instagram.model.userList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class homeFragment extends Fragment {
   private RecyclerView post_recycleView;
    private List<post> post_list;
    private postAdapter postadapter;
    private List<String> followingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        post_recycleView=view.findViewById(R.id.post_recycleView);
        post_recycleView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        post_recycleView.setLayoutManager(layoutManager);
        post_list=new ArrayList<>();
        followingList=new ArrayList<>();
        checkingFollowing();
       postadapter=new postAdapter(post_list,getContext(),true);

        post_recycleView.setAdapter(postadapter);



        return view;
    }
    String user_id=FirebaseAuth.getInstance().getCurrentUser().getUid();
    private void checkingFollowing() {
        FirebaseDatabase.getInstance().getReference().child("follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        followingList.clear();
                        followingList.add(user_id);
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            followingList.add(dataSnapshot.getKey());
                        }
                        readPost();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void readPost() {

        FirebaseDatabase.getInstance().getReference().child("post")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        post_list.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            post Post=dataSnapshot.getValue(post.class);
                            for(String id: followingList){
                                if(Post.getUploader().equals(id)){
                                    post_list.add(Post);
                                }
                            }
                            postadapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}