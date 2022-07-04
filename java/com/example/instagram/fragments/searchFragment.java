package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagram.R;
import com.example.instagram.adapter.searchAdapter;
import com.example.instagram.model.userList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;


public class searchFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<userList>   list;
    private searchAdapter searchAdapter;
    private SocialAutoCompleteTextView search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView=view.findViewById(R.id.recyclerViewUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list=new ArrayList<>();
        searchAdapter =new searchAdapter(list,getContext(),true);
        recyclerView.setAdapter(searchAdapter);
        search=view.findViewById(R.id.socialTextView);
        if(search.getText().length()==0) {
            adduser(search.getText().toString());
        }else{
            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchUser(s.toString());
                    searchAdapter.notifyDataSetChanged();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
        return view;

    }

    private void searchUser(String s) {
        Query query=FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").startAt(s);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    userList userName=dataSnapshot.getValue(userList.class);
                    list.add(userName);
                }
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void adduser(String s) {
        Query query = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").startAt(s);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                   for(DataSnapshot datasnapshot:snapshot.getChildren()){
                       userList userName=datasnapshot.getValue(userList.class);
                       list.add(userName);
                   }
                   searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}