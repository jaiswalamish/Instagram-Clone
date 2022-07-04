package com.example.instagram.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class savedpic extends RecyclerView.Adapter<savedpic.holder> {
    private ArrayList<String> arr;
    private Context mContext;

    public savedpic(ArrayList<String> arr, Context mContext) {
        this.arr = arr;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.userpostedpics,parent,false);

        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  savedpic.holder holder, int position) {
        String url=arr.get(position);
        Picasso.get().load(url).into(holder.savedImage);
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class holder extends RecyclerView.ViewHolder{
        ImageView savedImage;

        public holder(@NonNull  View itemView) {
            super(itemView);
            savedImage=itemView.findViewById(R.id.userimage);

        }
    }
}
