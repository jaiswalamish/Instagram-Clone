package com.example.instagram.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.MainActivity;
import com.example.instagram.R;
import com.example.instagram.model.userList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.holder> {

   private List<userList> mUser;
    private Context context;
    private FirebaseUser firebaseUser;
    private boolean isFragment;

    public searchAdapter(List<userList> mUser, Context context, boolean isFragment) {
        this.mUser = mUser;
        this.context = context;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.userlayout,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        userList userProfile=mUser.get(position);
        holder.follow.setVisibility(View.VISIBLE);
        holder.username.setText(userProfile.getUsername());
        holder.fullname.setText(userProfile.getName());
        Picasso.get().load(userProfile.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.profilePic);
        if(firebaseUser.getUid().equals(userProfile.getId())){
            holder.follow.setVisibility(View.GONE);
        }
        isFollowed(userProfile.getId(),holder.follow);
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.follow.getText().toString().equals("follow")) {
                    FirebaseDatabase.getInstance().getReference().child("follow")
                            .child(firebaseUser.getUid()).child("following").
                            child(userProfile.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("follow")
                            .child(userProfile.getId()).child("followers").
                            child(firebaseUser.getUid()).setValue(true);

                }else{
                    FirebaseDatabase.getInstance().getReference().child("follow")
                            .child(firebaseUser.getUid()).child("following").
                            child(userProfile.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("follow")
                            .child(userProfile.getId()).child("followers").
                            child(firebaseUser.getUid()).removeValue();
                }
            }
        });
        holder.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MainActivity.class);
                intent.putExtra("publisher",userProfile.getId());
                context.startActivity(intent);
            }
        });

    }

    private void isFollowed(String userid, Button follow) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid())
                .child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(userid).exists()){
                    follow.setText("following");
                }else{
                    follow.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class holder extends RecyclerView.ViewHolder{
        CircleImageView profilePic;
        TextView fullname;
        TextView username;
        Button follow;

        public holder(@NonNull View itemView) {
            super(itemView);
            profilePic=itemView.findViewById(R.id.searchProfilePic);
            fullname=itemView.findViewById(R.id.fullnameText);
            username=itemView.findViewById(R.id.usernameText);
            follow=itemView.findViewById(R.id.button_follow);
        }
    }
}
