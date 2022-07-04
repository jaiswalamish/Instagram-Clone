package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.adapter.commentAdapter;
import com.example.instagram.model.comments;
import com.example.instagram.model.post;
import com.example.instagram.model.userList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class commentActivity extends AppCompatActivity {

    private ImageView comment_back;
    private CircleImageView comment_profile_pic;
    private TextView comment_post;
    private EditText comment;
    private String postId;
    private String userId;
    private RecyclerView comment_section_recycleView;
    private commentAdapter adapter;
    private List<comments> commentsList;
    private String url;
    private String  username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        comment_back=findViewById(R.id.back_arrow);
        comment_section_recycleView=findViewById(R.id.comment_recycleView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        comment_section_recycleView.setLayoutManager(layoutManager);
        commentsList=new ArrayList<>();
        adapter=new commentAdapter(commentsList,this);
        comment_section_recycleView.setAdapter(adapter);

        comment_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        comment_profile_pic=findViewById(R.id.comment_profile_pic);
        comment_post=findViewById(R.id.post_comment);
        comment=findViewById(R.id.comment_text);
        Intent intent=getIntent();
        postId=intent.getStringExtra("postId");
        userId=intent.getStringExtra("userId");
        url=intent.getStringExtra("url");
        putUserProfilePic();
        comment_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment.getText().toString().equals("")){
                    Toast.makeText(commentActivity.this, "Put a comment", Toast.LENGTH_SHORT).show();
                }else{
                    putComment();
                }
            }
        });
        addComments();
    }

    private void addComments() {
        FirebaseDatabase.getInstance().getReference().child("comments").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        commentsList.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            comments comment=dataSnapshot.getValue(comments.class);
                            commentsList.add(comment);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });
    }

    private void putComment() {
        ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("uploading...");
        pd.show();
        Map<String,String> map=new HashMap<>();
        map.put("comment",comment.getText().toString());
        map.put("publisher",userId);
        map.put("url",url);
        map.put("username",username);

        FirebaseDatabase.getInstance().getReference().child("comments").child(postId).push().setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull  Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(commentActivity.this,"uploaded",Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(commentActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void putUserProfilePic() {
        FirebaseDatabase.getInstance().getReference().child("users").child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        userList user=snapshot.getValue(userList.class);
                        if(user==null){
                            comment_profile_pic.setImageResource(R.mipmap.ic_launcher_round);
                        }
                        else if( user.getImageUrl().equals("default")){
                           comment_profile_pic.setImageResource(R.mipmap.ic_launcher_round);
                        }else{
                            url=user.getImageUrl();
                            username=user.getUsername();
                            Picasso.get().load(user.getImageUrl()).into(comment_profile_pic);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });
        }
}