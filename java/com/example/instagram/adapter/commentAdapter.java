package com.example.instagram.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.MainActivity;
import com.example.instagram.R;
import com.example.instagram.model.comments;
import com.example.instagram.model.userList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.holder> {

    private List<comments> commentsList;
    private Context mContext;

    public commentAdapter(List<comments> commentsList, Context mContext) {
        this.commentsList = commentsList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.comment,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  commentAdapter.holder holder, int position) {
       comments comment=commentsList.get(position);
        holder.comment_section_comment.setText(comment.getComment());
                        if(comment==null){
                            holder.comment_section_pic.setImageResource(R.mipmap.ic_launcher_round);
                        }
                       else {
                            Picasso.get().load(comment.getUrl()).into(holder.comment_section_pic);
                        }
                        holder.comment_section_username.setText(comment.getUsername());

        holder.comment_section_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, MainActivity.class);
                intent.putExtra("ProfileId",comment.getPublisher());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        private CircleImageView comment_section_pic;
        private TextView comment_section_username;
        private TextView comment_section_comment;

        public holder(@NonNull View itemView) {
            super(itemView);
            comment_section_pic=itemView.findViewById(R.id.comment_section_pic);
            comment_section_username=itemView.findViewById(R.id.comment_section_username);
            comment_section_comment=itemView.findViewById(R.id.comment_section_comment);

        }
    }

}

