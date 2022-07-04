package com.example.instagram.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.MainActivity;
import com.example.instagram.R;
import com.example.instagram.model.userList;
import com.example.instagram.postActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView close;
    private ImageView check;
    private CircleImageView pic;
    private TextView pic_change;
    private MaterialEditText name;
    private MaterialEditText username;
    private MaterialEditText bio;
    private FirebaseUser firebaseUser;

    private Uri imageUri;
    private String Url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        close=findViewById(R.id.close_pic);
        check=findViewById(R.id.save_change);
        pic=findViewById(R.id.image_change);
        pic_change=findViewById(R.id.change_text);
        name=findViewById(R.id.name_change);
        username=findViewById(R.id.username_change);
        bio=findViewById(R.id.bio_change);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList user=snapshot.getValue(userList.class);
                        if(user!=null){
                            name.setText(user.getName());
                            username.setText(user.getUsername());
                            bio.setText(user.getBio());
                            Url=user.getImageUrl();
                            Picasso.get().load(user.getImageUrl()).into(pic);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        pic_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(EditProfileActivity.this);
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   imageupload();
            }
        });


    }

    private void imageupload() {
        ProgressDialog pd=new ProgressDialog(EditProfileActivity.this);
        pd.setMessage("changes saving...");
        pd.show();
        try {
            StorageReference reference = FirebaseStorage.getInstance().getReference("profilepicuploads")
                    .child(firebaseUser.getUid()).child(System.currentTimeMillis() + ".jpeg");
            StorageTask task = reference.putFile(imageUri);
            task.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri download = task.getResult();
                        Url = download.toString();

                            FirebaseDatabase.getInstance().getReference().child("users")
                                    .child(firebaseUser.getUid()).child("imageUrl").setValue(Url);
                            pd.dismiss();



                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("name", name.getText().toString());
        map.put("username", username.getText().toString());
        map.put("bio", bio.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(firebaseUser.getUid()).updateChildren(map);
        pd.dismiss();
        Intent intent=new Intent(getApplicationContext(),profileFragment.class);
        startService(intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri=result.getUri();
            pic.setImageURI(imageUri);
            pic.setTag("change");
        }else{
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            pic.setTag("not");
        }
    }
}