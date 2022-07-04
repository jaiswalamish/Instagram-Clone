package com.example.instagram.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.model.post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class userpostedpic extends RecyclerView.Adapter<userpostedpic.holder> {
    private List<post> postDetail;
    private Context mContext;

    public userpostedpic(List<post> postDetail, Context mContext) {
        this.postDetail = postDetail;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.userpostedpics,parent,false);

        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userpostedpic.holder holder, int position) {
        post Post=postDetail.get(position);
        Picasso.get().load(Post.getUrl()).into(holder.userPostedPic);

    }

    @Override
    public int getItemCount() {
        return postDetail.size();
    }

    public class holder extends RecyclerView.ViewHolder{
        ImageView userPostedPic;

        public holder(@NonNull  View itemView) {
            super(itemView);
            userPostedPic=itemView.findViewById(R.id.userimage);
        }
    }
}
