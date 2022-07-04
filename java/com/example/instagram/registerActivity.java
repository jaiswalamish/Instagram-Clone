package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class registerActivity extends AppCompatActivity {

    private EditText name;
    private  EditText username;
    private   EditText email;
    private EditText password;
    private Button register;
    private TextView relogin;
    private EditText reEnterPassword;
   private FirebaseAuth auth;
   ProgressDialog pd;
   DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent=getIntent();

        name=findViewById(R.id.nameText);
        username=findViewById(R.id.usernameText);
        email=findViewById(R.id.emailText);
        password=findViewById(R.id.passwordText);
        register=findViewById(R.id.registerbutton);
        reEnterPassword=findViewById(R.id.rePasswordText);
        relogin=findViewById(R.id.alreadyUserText);

        databaseReference=FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        pd=new ProgressDialog(registerActivity.this);

        relogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registerActivity.this, loginActivity.class));
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name_=name.getText().toString();
                String username_=username.getText().toString();
                String email_=email.getText().toString();
                String pass=password.getText().toString();
                String rePass=reEnterPassword.getText().toString();
                if(name_.equals("") || username_.equals("") || pass.equals("") || rePass.equals("")){
                    Toast.makeText(registerActivity.this, "Invalid Format", Toast.LENGTH_SHORT).show();
                }else if(pass.length()<6 || rePass.length()<6){
                    Toast.makeText(registerActivity.this, "Password is too short", Toast.LENGTH_SHORT).show();
                }else if(!pass.equals(rePass)){
                    Toast.makeText(registerActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                }else {
                    pd.setMessage("Please Wait...");
                    pd.show();
                    registeruser(name_, username_, email_, pass);
                }
            }
        });
    }

    private void registeruser(String name_, String username_, String email_, String pass) {
        auth.createUserWithEmailAndPassword(email_,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Map<String,Object> map=new HashMap<>();
                map.put("name",name_);
                map.put("username",username_);
                map.put("email",email_);
                map.put("id",auth.getCurrentUser().getUid());
                map.put("Bio",null);
                map.put("imageUrl","default");
               databaseReference.child("users").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            pd.dismiss();
                            startActivity(new Intent(registerActivity.this, loginActivity.class));
                            finish();
                            Toast.makeText(registerActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(registerActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(registerActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });
            }
        });
    }
}