package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {
    Button login;
    Button register;
    EditText username;
    EditText password;
   private FirebaseAuth auth;
   ImageView iconImage;
   ConstraintLayout loginLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        iconImage = findViewById(R.id.iconImage);

        loginLayout = findViewById(R.id.loginLayout);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(loginActivity.this, MainActivity.class));
            finish();
        } else {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String user = username.getText().toString();
                    String Pass = password.getText().toString();
                    login(user, Pass);
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(loginActivity.this, registerActivity.class));
                    finish();
                }
            });
            password.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.ACTION_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
                        String user = username.getText().toString();
                        String Pass = password.getText().toString();
                        login(user, Pass);
                    }
                    return false;
                }
            });
        }
    }


        private void login (String user, String pass){
            if (user.equals("")) {
                Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
            } else if (pass.equals("")) {
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            } else {
                auth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(loginActivity.this, "Loged In", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(loginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(loginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(loginActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(loginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
}