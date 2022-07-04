package com.example.instagram.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.commentActivity;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class postAdapter extends RecyclerView.Adapter<postAdapter.holder> {

   private List<post>  post_list_detail;
   private Context mContext;
   private  boolean homeFragment;
    private FirebaseUser firebaseUser;

    public postAdapter(List<post> post_list_detail, Context mContext, boolean homeFragment) {
        this.post_list_detail = post_list_detail;
        this.mContext = mContext;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.newsfeedpost,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        post Post= post_list_detail.get(position);
        Picasso.get().load(Post.getUrl()).into(holder.image_post);
        holder.description.setText(Post.getDescription());

        FirebaseDatabase.getInstance().getReference().child("users").child(Post.getUploader())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                userList user=snapshot.getValue(userList.class);
                if(user==null){
                    holder.user_profile_pic.setImageResource(R.mipmap.ic_launcher_round);
                }
                else if(user.getImageUrl().equals("default")){
                    holder.user_profile_pic.setImageResource(R.mipmap.ic_launcher_round);
                }else {
                    Picasso.get().load(user.getImageUrl()).into(holder.user_profile_pic);
                }
                if(user!=null){
                    holder.user_name.setText(user.getUsername());
                    holder.username_left_description.setText(user.getUsername());
                }


            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.likes.getTag().equals("not liked")){
                    FirebaseDatabase.getInstance().getReference().child("likes").child(Post.getPostid())
                            .child(firebaseUser.getUid()).setValue(true);
                }else{
                    FirebaseDatabase.getInstance().getReference().child("likes").child(Post.getPostid())
                            .child(firebaseUser.getUid()).removeValue();
                }

            }
        });
        holder.numberof_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,commentActivity.class);
                intent.putExtra("userId",firebaseUser.getUid());
                intent.putExtra("postId",Post.getPostid());
                mContext.startActivity(intent);
            }
        });
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,commentActivity.class);
                intent.putExtra("userId",firebaseUser.getUid());
                intent.putExtra("postId",Post.getPostid());
                mContext.startActivity(intent);
            }
        });
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.save.getTag().equals("not saved")){
                    FirebaseDatabase.getInstance().getReference("saved").child(firebaseUser.getUid())
                            .child(Post.getPostid()).setValue(Post.getUrl());
                }else{
                    FirebaseDatabase.getInstance().getReference("saved").child(firebaseUser.getUid())
                            .child(Post.getPostid()).removeValue();
                }
            }
        });
        isLiked(Post.getPostid(), holder.likes);
        isSaved(Post.getPostid(),holder.save);
        likesCount(Post.getPostid(),holder.number0f_likes);
        commentCount(Post.getPostid(),holder.numberof_comments);


    }

    private void isSaved(String postid, ImageView save) {
        FirebaseDatabase.getInstance().getReference("saved").child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(postid).exists()){
                            save.setImageResource(R.drawable.ic_savedpic);
                            save.setTag("saved");
                        }else{
                            save.setImageResource(R.drawable.ic_save);
                            save.setTag("not saved");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });
    }

    private void commentCount(String postId, TextView numberof_comments) {
        FirebaseDatabase.getInstance().getReference().child("comments").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getChildrenCount()==0){
                            numberof_comments.setText("");
                        }else if(snapshot.getChildrenCount()==1){
                            numberof_comments.setText("view all "+snapshot.getChildrenCount()+" comment");
                        }else {
                            numberof_comments.setText("view all " + snapshot.getChildrenCount() + " comments");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });
    }

    private void isLiked(String postId, ImageView likes) {
        FirebaseDatabase.getInstance().getReference().child("likes").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(firebaseUser.getUid()).exists()){
                            likes.setImageResource(R.drawable.ic_redheart);
                            likes.setTag("already liked");
                        }else{
                            likes.setImageResource(R.drawable.ic_like);
                            likes.setTag("not liked");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });
    }
    private void likesCount(String postId,TextView number_likes){
        FirebaseDatabase.getInstance().getReference().child("likes").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        if((int) snapshot.getChildrenCount()<=1)
                        number_likes.setText(snapshot.getChildrenCount()+" like");
                        else{
                           number_likes.setText(snapshot.getChildrenCount()+" likes");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });
    }


    @Override
    public int getItemCount() {
        try {
            return post_list_detail.size();
        }catch (Exception e){
            return 0;
        }

    }

    public class holder extends RecyclerView.ViewHolder {

        CircleImageView user_profile_pic;
        TextView user_name;
        ImageView more_click;
        ImageView image_post;
        ImageView likes;
        ImageView comments;
        ImageView share;
        ImageView save;
        TextView number0f_likes;
        TextView numberof_comments;
        TextView username_left_description;
        TextView description;

        public holder(@NonNull View itemView) {
            super(itemView);

            user_profile_pic=itemView.findViewById(R.id.userProfile);
            user_name=itemView.findViewById(R.id.post_uploadername);
            more_click=itemView.findViewById(R.id.more_click);
            image_post=itemView.findViewById(R.id.imageView_newsfeed);
            likes=itemView.findViewById(R.id.likeButton);
            comments=itemView.findViewById(R.id.commentButton);
            share=itemView.findViewById(R.id.share_button);
            save=itemView.findViewById(R.id.save_button);
            number0f_likes=itemView.findViewById(R.id.like_textView);
            numberof_comments=itemView.findViewById(R.id.newsfeed_comment);
            username_left_description=itemView.findViewById(R.id.post_holder_name);
            description=itemView.findViewById(R.id.newsfeed_description);

        }
    }

}
