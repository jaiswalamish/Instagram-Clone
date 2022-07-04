package com.example.instagram.fragments;

import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.MainActivity;
import com.example.instagram.R;
import com.example.instagram.adapter.savedpic;
import com.example.instagram.adapter.userpostedpic;
import com.example.instagram.loginActivity;
import com.example.instagram.model.post;
import com.example.instagram.model.userList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileFragment extends Fragment {
    CircleImageView profilePic;
    TextView username;
    TextView fullname;
    TextView no_posts;
    TextView no_following;
    TextView no_followers;
    TextView bio;
    Button editProfile;
    ImageView pic_section;
    ImageView save_section;
    RecyclerView pic_collection;
    RecyclerView save_collection;
    TextView logout;
    String profileId;
    private userpostedpic adapter;
    private ArrayList<post> postList;
    private savedpic savedpicadapter;
    private ArrayList<String> savedImageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        String data=getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profileId","none");
//        if(data.equals("none")) {
//            profileId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        }
//        else{
//            profileId=data;
//        }
        profileId=  FirebaseAuth.getInstance().getCurrentUser().getUid();

        profilePic=view.findViewById(R.id.profile_section_pic);
        username=view.findViewById(R.id.profile_section_username);
        fullname=view.findViewById(R.id.profile_section_name);
        no_posts=view.findViewById(R.id.no_of_posts);
        no_followers=view.findViewById(R.id.no_of_followers);
        no_following=view.findViewById(R.id.no_of_following);
        bio=view.findViewById(R.id.profile_section_bio);
        editProfile=view.findViewById(R.id.edit_profile);
        pic_section=view.findViewById(R.id.picture_section);
        save_section=view.findViewById(R.id.save_section);
        pic_collection=view.findViewById(R.id.image_recycle_view);
        logout=view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_lock_power_off)
                        .setTitle("logout")
                        .setMessage("Are You Sure ?")
                        .setPositiveButton( "Yes",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getContext(),loginActivity.class));
                                getActivity().finish();
                            }
                        })
//set negative button
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what should happen when negative button is clicked
                                Toast.makeText(getContext(),"Ok",Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(getContext(),loginActivity.class));
//                getActivity().finish();
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),EditProfileActivity.class));
            }
        });

        save_collection=view.findViewById(R.id.save_image_recycle_view);
        pic_collection.setHasFixedSize(true);

        postList=new ArrayList<>();
        adapter=new userpostedpic(postList,getContext());
        pic_collection.setLayoutManager(new GridLayoutManager(getContext(),3));
        pic_collection.setAdapter(adapter);
        addPostDeatil();

        savedImageUrl=new ArrayList<>();
        savedpicadapter=new savedpic(savedImageUrl,getContext());
        save_collection.setLayoutManager(new GridLayoutManager(getContext(),3));
        save_collection.setAdapter(savedpicadapter);


        save_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_collection.setVisibility(View.VISIBLE);
                pic_collection.setVisibility(View.GONE);
                save_section.setImageResource(R.drawable.ic_savedpic);
                addSavedPostPic();
            }
        });
        pic_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_collection.setVisibility(View.GONE);
                pic_collection.setVisibility(View.VISIBLE);
                save_section.setImageResource(R.drawable.ic_save);

            }
        });
        putUserProfile();
        postCount();
        followersAndfollowingCount();

        return view;
    }

    private void addSavedPostPic() {
        FirebaseDatabase.getInstance().getReference("saved").child(profileId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        savedImageUrl.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            String url= String.valueOf(dataSnapshot.getValue());
                            savedImageUrl.add(url);
                        }
                        Collections.reverse(savedImageUrl);
                        savedpicadapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void addPostDeatil() {
       DatabaseReference ref= FirebaseDatabase.getInstance().getReference("post");
              ref.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       postList.clear();
                       for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                           post Post=dataSnapshot.getValue(post.class);
                           if(Post.getUploader().equals(profileId)){
                               postList.add(Post);
                           }

                       }
                       Collections.reverse(postList);
                       adapter.notifyDataSetChanged();
                   }

                   @Override
                   public void onCancelled(@NonNull  DatabaseError error) {

                   }
               });
    }

    private void followersAndfollowingCount() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("follow")
                .child(profileId);
        ref.child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                    no_following.setText(" "+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                no_followers.setText(" "+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

    }

    private void postCount() {
        FirebaseDatabase.getInstance().getReference().child("post")

                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        int count=0;
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                         post Post=dataSnapshot.getValue(post.class);
                         if(Post.getUploader().equals(profileId)){
                             count++;
                         }
                        }
                        no_posts.setText(""+Integer.toString(count));
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });
    }

    private void putUserProfile() {
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(profileId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList userList=snapshot.getValue(userList.class);

                         if(userList!=null && !userList.getImageUrl().equals("dafault")){
                            Picasso.get().load(userList.getImageUrl()).into(profilePic);
                        }else{
                            profilePic.setImageResource(R.mipmap.ic_launcher_round);
                        }
                        if(userList!=null){
                            username.setText(userList.getUsername());
                            fullname.setText(userList.getName());
                            bio.setText(userList.getBio());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}