package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.EventLogTags;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.theartofdev.edmodo.cropper.CropImage.activity;
import static com.theartofdev.edmodo.cropper.CropImage.getActivityResult;
import static com.theartofdev.edmodo.cropper.CropImage.getCameraIntent;

public class postActivity extends AppCompatActivity {
    private ImageView uploadImage;
    private String imageUrl;
    private TextView postText;
    private EditText descriptionText;
    private ImageView closeImage;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        uploadImage = findViewById(R.id.uploadImage);
        postText = findViewById(R.id.postText);
        descriptionText = findViewById(R.id.descriptionText);
        closeImage = findViewById(R.id.closeImage);
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(postActivity.this, MainActivity.class));
                finish();
            }
        });
        postText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri!=null){
                    upload();
                }
                else{
                    Toast.makeText(postActivity.this, "Select an Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
        CropImage.activity().start(postActivity.this);
    }

    private void upload() {
        ProgressDialog pd=new ProgressDialog(postActivity.this);
        pd.setMessage("uploading...");
        pd.show();
        StorageReference reference=FirebaseStorage.getInstance().getReference("post").child(System.currentTimeMillis()+"."+getfileExtension(imageUri));
        StorageTask task=reference.putFile(imageUri);
        task.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                    return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task <Uri>task) {
                Uri download=task.getResult();
                imageUrl=download.toString();
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("post");
                String postId=databaseReference.push().getKey();
                Map<String,Object> map=new HashMap<>();
                map.put("postid",postId);
                map.put("url",imageUrl);
                map.put("description",descriptionText.getText().toString());
                map.put("uploader", FirebaseAuth.getInstance().getCurrentUser().getUid());
                databaseReference.child(postId).setValue(map);
//
//                DatabaseReference hashRefrence=FirebaseDatabase.getInstance().getReference().child("hashtags");
//                if(!hashTags.isEmpty()){
//                    for(String tags:hashTags){
//                        map.clear();
//                        map.put("hashtags",tags);
//                        map.put("postid",postId);
//                        hashRefrence.child(tags.toLowerCase()).setValue(map);
//                    }
//                }
                pd.dismiss();
                Toast.makeText(postActivity.this, "uploaded image", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(postActivity.this,MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(postActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getfileExtension(Uri imageUri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(imageUri));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            uploadImage.setImageURI(imageUri);
        }
        else{
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(postActivity.this,MainActivity.class));
            finish();
        }
    }
}

